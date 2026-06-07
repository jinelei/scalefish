package com.jinelei.scalefish.webdav;

import com.jinelei.scalefish.entity.AddressBook;
import com.jinelei.scalefish.entity.CalendarEntity;
import com.jinelei.scalefish.entity.CalendarEvent;
import com.jinelei.scalefish.entity.Contact;
import com.jinelei.scalefish.entity.User;
import com.jinelei.scalefish.repository.AddressBookRepository;
import com.jinelei.scalefish.repository.CalendarEventRepository;
import com.jinelei.scalefish.repository.CalendarRepository;
import com.jinelei.scalefish.repository.ContactRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/webdav")
public class WebDavController {

    private final CalendarRepository calendarRepository;
    private final CalendarEventRepository calendarEventRepository;
    private final AddressBookRepository addressBookRepository;
    private final ContactRepository contactRepository;

    public WebDavController(CalendarRepository calendarRepository,
                             CalendarEventRepository calendarEventRepository,
                             AddressBookRepository addressBookRepository,
                             ContactRepository contactRepository) {
        this.calendarRepository = calendarRepository;
        this.calendarEventRepository = calendarEventRepository;
        this.addressBookRepository = addressBookRepository;
        this.contactRepository = contactRepository;
    }

    private User currentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof User user) {
            return user;
        }
        return null;
    }

    @RequestMapping(value = "/**")
    public void handle(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = currentUser();
        if (user == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String method = request.getMethod();
        String path = extractPath(request.getRequestURI());

        switch (method.toUpperCase()) {
            case "OPTIONS" -> handleOptions(response);
            case "PROPFIND" -> handlePropfind(request, response, path, user);
            case "GET" -> handleGet(response, path, user);
            case "PUT" -> handlePut(request, response, path, user);
            case "DELETE" -> handleDelete(response, path, user);
            case "REPORT" -> handleReport(request, response, path, user);
            case "MKCOL", "MKCALENDAR" -> handleMkcol(request, response, path, user);
            default -> response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        }
    }

    private void handleOptions(HttpServletResponse response) {
        response.setHeader("DAV", "1, 2, 3, calendar-access, addressbook-access");
        response.setHeader("Allow", "OPTIONS, PROPFIND, GET, PUT, DELETE, REPORT, MKCOL, MKCALENDAR");
        response.setHeader("MS-Author-Via", "DAV");
    }

    private void handlePropfind(HttpServletRequest request, HttpServletResponse response,
                                 String path, User user) throws IOException {
        String depth = request.getHeader("Depth");
        if (depth == null) depth = "1";

        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        xml.append("<multistatus xmlns=\"DAV:\"");
        xml.append(" xmlns:C=\"urn:ietf:params:xml:ns:caldav\"");
        xml.append(" xmlns:CR=\"urn:ietf:params:xml:ns:carddav\">");

        String baseUrl = request.getScheme() + "://" + request.getHeader("Host") + "/webdav";

        if (path.isEmpty() || path.equals("/")) {
            addResponse(xml, baseUrl + "/", "collection", null);
            addResponse(xml, baseUrl + "/calendars/", "collection", null);
            addResponse(xml, baseUrl + "/contacts/", "collection", null);
            if (!"0".equals(depth)) {
                listUserCalendars(xml, user, baseUrl, depth);
                listUserAddressBooks(xml, user, baseUrl, depth);
            }
        } else if (path.startsWith("/calendars")) {
            handleCalendarPropfind(xml, path, user, baseUrl, depth);
        } else if (path.startsWith("/contacts")) {
            handleContactPropfind(xml, path, user, baseUrl, depth);
        }

        xml.append("</multistatus>");
        response.setContentType("application/xml; charset=utf-8");
        response.setStatus(207);
        response.getWriter().write(xml.toString());
    }

    private void listUserCalendars(StringBuilder xml, User user, String baseUrl, String depth) {
        for (var cal : calendarRepository.findByUserIdOrderByCreatedAtAsc(user.getId())) {
            String calUrl = baseUrl + "/calendars/" + cal.getId() + "/";
            addResponse(xml, calUrl, "calendar", cal.getName());
            if ("infinity".equals(depth)) {
                for (var event : calendarEventRepository.findByCalendarIdOrderByStartTimeAsc(cal.getId())) {
                    addResponse(xml, calUrl + event.getId() + ".ics", null, event.getTitle());
                }
            }
        }
    }

    private void listUserAddressBooks(StringBuilder xml, User user, String baseUrl, String depth) {
        for (var ab : addressBookRepository.findByUserIdOrderByCreatedAtAsc(user.getId())) {
            String abUrl = baseUrl + "/contacts/" + ab.getId() + "/";
            addResponse(xml, abUrl, "addressbook", ab.getName());
            if ("infinity".equals(depth)) {
                for (var contact : contactRepository.findByAddressBookIdOrderByNameAsc(ab.getId())) {
                    addResponse(xml, abUrl + contact.getId() + ".vcf", null, contact.getName());
                }
            }
        }
    }

    private void handleCalendarPropfind(StringBuilder xml, String path, User user,
                                         String baseUrl, String depth) {
        String rest = path.substring("/calendars".length());
        if (rest.isEmpty() || rest.equals("/")) {
            addResponse(xml, baseUrl + "/calendars/", "collection", null);
            if (!"0".equals(depth)) {
                listUserCalendars(xml, user, baseUrl, depth);
            }
            return;
        }
        String clean = rest.startsWith("/") ? rest.substring(1) : rest;
        if (clean.endsWith("/")) clean = clean.substring(0, clean.length() - 1);

        if (clean.endsWith(".ics")) {
            String eid = clean.substring(0, clean.length() - 4);
            try {
                Long eventId = Long.parseLong(eid);
                calendarEventRepository.findById(eventId).ifPresent(event -> {
                    if (event.getCalendar().getUser().getId().equals(user.getId())) {
                        addResponse(xml, baseUrl + path, null, event.getTitle());
                    }
                });
            } catch (NumberFormatException ignored) {}
            return;
        }

        try {
            Long calId = Long.parseLong(clean);
            calendarRepository.findById(calId).ifPresent(cal -> {
                if (cal.getUser().getId().equals(user.getId())) {
                    String calUrl = baseUrl + "/calendars/" + calId + "/";
                    addResponse(xml, calUrl, "calendar", cal.getName());
                    if (!"0".equals(depth)) {
                        for (var event : calendarEventRepository.findByCalendarIdOrderByStartTimeAsc(calId)) {
                            addResponse(xml, calUrl + event.getId() + ".ics", null, event.getTitle());
                        }
                    }
                }
            });
        } catch (NumberFormatException ignored) {}
    }

    private void handleContactPropfind(StringBuilder xml, String path, User user,
                                        String baseUrl, String depth) {
        String rest = path.substring("/contacts".length());
        if (rest.isEmpty() || rest.equals("/")) {
            addResponse(xml, baseUrl + "/contacts/", "collection", null);
            if (!"0".equals(depth)) {
                listUserAddressBooks(xml, user, baseUrl, depth);
            }
            return;
        }
        String clean = rest.startsWith("/") ? rest.substring(1) : rest;
        if (clean.endsWith("/")) clean = clean.substring(0, clean.length() - 1);

        if (clean.endsWith(".vcf")) {
            String cid = clean.substring(0, clean.length() - 4);
            try {
                Long contactId = Long.parseLong(cid);
                contactRepository.findById(contactId).ifPresent(contact -> {
                    if (contact.getAddressBook().getUser().getId().equals(user.getId())) {
                        addResponse(xml, baseUrl + path, null, contact.getName());
                    }
                });
            } catch (NumberFormatException ignored) {}
            return;
        }

        try {
            Long abId = Long.parseLong(clean);
            addressBookRepository.findById(abId).ifPresent(ab -> {
                if (ab.getUser().getId().equals(user.getId())) {
                    String abUrl = baseUrl + "/contacts/" + abId + "/";
                    addResponse(xml, abUrl, "addressbook", ab.getName());
                    if (!"0".equals(depth)) {
                        for (var contact : contactRepository.findByAddressBookIdOrderByNameAsc(abId)) {
                            addResponse(xml, abUrl + contact.getId() + ".vcf", null, contact.getName());
                        }
                    }
                }
            });
        } catch (NumberFormatException ignored) {}
    }

    private void addResponse(StringBuilder xml, String href, String resType, String displayName) {
        xml.append("<response><href>").append(escapeXml(href)).append("</href><propstat><prop>");
        xml.append("<displayname>").append(escapeXml(displayName != null ? displayName : "")).append("</displayname>");
        xml.append("<resourcetype>");
        if (resType != null) {
            xml.append("<collection/>");
            if ("calendar".equals(resType)) {
                xml.append("<C:calendar/>");
            } else if ("addressbook".equals(resType)) {
                xml.append("<CR:addressbook/>");
            }
        }
        xml.append("</resourcetype>");
        xml.append("<getcontenttype>").append("httpd/unix-directory".equals(resType) || "collection".equals(resType) || resType == null || "calendar".equals(resType) || "addressbook".equals(resType) ? "" : "").append("</getcontenttype>");
        xml.append("<getetag>\"scalefish-").append(System.currentTimeMillis()).append("\"</getetag>");
        xml.append("</prop><status>HTTP/1.1 200 OK</status></propstat></response>");
    }

    private void handleGet(HttpServletResponse response, String path, User user) throws IOException {
        if (path.endsWith(".ics")) {
            Matcher m = Pattern.compile("/calendars/(\\d+)/(\\d+)\\.ics").matcher(path);
            if (m.find()) {
                Long eventId = Long.parseLong(m.group(2));
                var opt = calendarEventRepository.findById(eventId);
                if (opt.isPresent() && opt.get().getCalendar().getUser().getId().equals(user.getId())) {
                    String ical = opt.get().getIcalData();
                    if (ical == null) ical = rebuildIcal(opt.get());
                    response.setContentType("text/calendar; charset=utf-8");
                    response.setHeader("ETag", "\"sf-" + eventId + "\"");
                    response.getWriter().write(ical);
                    return;
                }
            }
        } else if (path.endsWith(".vcf")) {
            Matcher m = Pattern.compile("/contacts/(\\d+)/(\\d+)\\.vcf").matcher(path);
            if (m.find()) {
                Long contactId = Long.parseLong(m.group(2));
                var opt = contactRepository.findById(contactId);
                if (opt.isPresent() && opt.get().getAddressBook().getUser().getId().equals(user.getId())) {
                    String vcard = opt.get().getVcardData();
                    if (vcard == null) vcard = rebuildVcard(opt.get());
                    response.setContentType("text/vcard; charset=utf-8");
                    response.setHeader("ETag", "\"sf-" + contactId + "\"");
                    response.getWriter().write(vcard);
                    return;
                }
            }
        }
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    private void handlePut(HttpServletRequest request, HttpServletResponse response,
                            String path, User user) throws IOException {
        String body = readBody(request.getInputStream());

        if (path.endsWith(".ics")) {
            Matcher m = Pattern.compile("/calendars/(\\d+)/(\\d+)\\.ics").matcher(path);
            if (m.find()) {
                Long calId = Long.parseLong(m.group(1));
                Long eventId = Long.parseLong(m.group(2));
                var calOpt = calendarRepository.findById(calId);
                if (calOpt.isEmpty() || !calOpt.get().getUser().getId().equals(user.getId())) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN);
                    return;
                }
                var opt = calendarEventRepository.findById(eventId);
                if (opt.isPresent()) {
                    var event = opt.get();
                    event.setIcalData(body);
                    parseIcalIntoEvent(body, event);
                    calendarEventRepository.save(event);
                    response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                } else {
                    var event = new CalendarEvent();
                    event.setCalendar(calOpt.get());
                    event.setIcalData(body);
                    parseIcalIntoEvent(body, event);
                    if (event.getTitle() == null) event.setTitle("Event");
                    calendarEventRepository.save(event);
                    response.setStatus(HttpServletResponse.SC_CREATED);
                }
                response.setHeader("ETag", "\"sf-" + eventId + "\"");
                return;
            }
        } else if (path.endsWith(".vcf")) {
            Matcher m = Pattern.compile("/contacts/(\\d+)/(\\d+)\\.vcf").matcher(path);
            if (m.find()) {
                Long contactId = Long.parseLong(m.group(2));
                var opt = contactRepository.findById(contactId);
                if (opt.isPresent()) {
                    var contact = opt.get();
                    contact.setVcardData(body);
                    parseVcardIntoContact(body, contact);
                    contactRepository.save(contact);
                    response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                    return;
                }
            }
        }
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    private void handleDelete(HttpServletResponse response, String path, User user) throws IOException {
        if (path.endsWith(".ics")) {
            Matcher m = Pattern.compile("/calendars/\\d+/(\\d+)\\.ics").matcher(path);
            if (m.find()) {
                Long eventId = Long.parseLong(m.group(1));
                var opt = calendarEventRepository.findById(eventId);
                if (opt.isPresent() && opt.get().getCalendar().getUser().getId().equals(user.getId())) {
                    calendarEventRepository.delete(opt.get());
                    response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                    return;
                }
            }
        } else if (path.endsWith(".vcf")) {
            Matcher m = Pattern.compile("/contacts/\\d+/(\\d+)\\.vcf").matcher(path);
            if (m.find()) {
                Long contactId = Long.parseLong(m.group(1));
                var opt = contactRepository.findById(contactId);
                if (opt.isPresent() && opt.get().getAddressBook().getUser().getId().equals(user.getId())) {
                    contactRepository.delete(opt.get());
                    response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                    return;
                }
            }
        }
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    private void handleReport(HttpServletRequest request, HttpServletResponse response,
                               String path, User user) throws IOException {
        String baseUrl = request.getScheme() + "://" + request.getHeader("Host") + "/webdav";
        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        xml.append("<multistatus xmlns=\"DAV:\"");
        xml.append(" xmlns:C=\"urn:ietf:params:xml:ns:caldav\"");
        xml.append(" xmlns:CR=\"urn:ietf:params:xml:ns:carddav\">");

        if (path.startsWith("/calendars")) {
            Matcher m = Pattern.compile("/calendars/(\\d+)/").matcher(path);
            if (m.find()) {
                Long calId = Long.parseLong(m.group(1));
                for (var event : calendarEventRepository.findByCalendarIdOrderByStartTimeAsc(calId)) {
                    String url = baseUrl + "/calendars/" + calId + "/" + event.getId() + ".ics";
                    addResponse(xml, url, null, event.getTitle());
                }
            }
        } else if (path.startsWith("/contacts")) {
            Matcher m = Pattern.compile("/contacts/(\\d+)/").matcher(path);
            if (m.find()) {
                Long abId = Long.parseLong(m.group(1));
                for (var contact : contactRepository.findByAddressBookIdOrderByNameAsc(abId)) {
                    String url = baseUrl + "/contacts/" + abId + "/" + contact.getId() + ".vcf";
                    addResponse(xml, url, null, contact.getName());
                }
            }
        }

        xml.append("</multistatus>");
        response.setContentType("application/xml; charset=utf-8");
        response.setStatus(207);
        response.getWriter().write(xml.toString());
    }

    private void handleMkcol(HttpServletRequest request, HttpServletResponse response,
                              String path, User user) throws IOException {
        if (path.startsWith("/calendars/")) {
            String name = path.substring("/calendars/".length());
            if (name.endsWith("/")) name = name.substring(0, name.length() - 1);
            if (!name.isEmpty()) {
                var cal = new CalendarEntity();
                cal.setName(name);
                cal.setUser(user);
                calendarRepository.save(cal);
                response.setStatus(HttpServletResponse.SC_CREATED);
                return;
            }
        }
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }

    private String extractPath(String requestUri) {
        int idx = requestUri.indexOf("/webdav");
        if (idx < 0) return "/";
        String path = requestUri.substring(idx + "/webdav".length());
        if (path.isEmpty()) path = "/";
        try {
            path = URLDecoder.decode(path, StandardCharsets.UTF_8);
        } catch (Exception ignored) {}
        return path;
    }

    private String readBody(InputStream in) throws IOException {
        try (var reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            var sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\r\n");
            }
            return sb.toString();
        }
    }

    private String escapeXml(String s) {
        return s.replace("&", "&amp;").replace("<", "&lt;")
                .replace(">", "&gt;").replace("\"", "&quot;");
    }

    private void parseIcalIntoEvent(String icalData, CalendarEvent event) {
        String title = extractValue(icalData, "SUMMARY");
        String desc = extractValue(icalData, "DESCRIPTION");
        String loc = extractValue(icalData, "LOCATION");
        String dtstart = extractValue(icalData, "DTSTART");
        String dtend = extractValue(icalData, "DTEND");
        String rrule = extractValue(icalData, "RRULE");
        String status = extractValue(icalData, "STATUS");
        String priority = extractValue(icalData, "PRIORITY");
        String categories = extractValue(icalData, "CATEGORIES");
        String url = extractValue(icalData, "URL");
        String sequence = extractValue(icalData, "SEQUENCE");

        if (title != null) event.setTitle(title.replace("\\,", ",").replace("\\n", "\n"));
        if (desc != null) event.setDescription(desc.replace("\\n", "\n"));
        if (loc != null) event.setLocation(loc.replace("\\,", ","));
        if (dtstart != null) {
            try { event.setStartTime(parseIcalDt(dtstart)); } catch (Exception ignored) {}
        }
        if (dtend != null) {
            try { event.setEndTime(parseIcalDt(dtend)); } catch (Exception ignored) {}
        }
        if (rrule != null) event.setRrule(rrule);
        if (status != null) event.setStatus(status);
        if (priority != null) {
            try { event.setPriority(Integer.parseInt(priority)); } catch (Exception ignored) {}
        }
        if (categories != null) event.setCategories(categories);
        if (url != null) event.setUrl(url);
        if (sequence != null) {
            try { event.setSequence(Integer.parseInt(sequence)); } catch (Exception ignored) {}
        }
    }

    private String extractValue(String data, String key) {
        Pattern p = Pattern.compile("(?m)^" + key + "(?:;[^:]*)?:(.+?)(\\r?\\n|$)");
        Matcher m = p.matcher(data);
        return m.find() ? m.group(1).trim() : null;
    }

    private LocalDateTime parseIcalDt(String value) {
        String v = value.replace("Z", "");
        if (v.length() >= 15) {
            return LocalDateTime.parse(v.substring(0, 15),
                DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss"));
        }
        return LocalDateTime.parse(v, DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

    private String rebuildIcal(CalendarEvent event) {
        var sb = new StringBuilder();
        sb.append("BEGIN:VCALENDAR\r\nVERSION:2.0\r\nPRODID:-//Scalefish//Calendar//EN\r\n");
        sb.append("CALSCALE:GREGORIAN\r\n");
        sb.append("BEGIN:VEVENT\r\n");
        sb.append("UID:").append(event.getId()).append("@scalefish\r\n");
        sb.append("DTSTART:").append(event.getStartTime().format(DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss"))).append("\r\n");
        sb.append("DTEND:").append(event.getEndTime().format(DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss"))).append("\r\n");
        sb.append("SUMMARY:").append(event.getTitle()).append("\r\n");
        if (event.getDescription() != null)
            sb.append("DESCRIPTION:").append(event.getDescription().replace("\n", "\\n")).append("\r\n");
        if (event.getLocation() != null)
            sb.append("LOCATION:").append(event.getLocation()).append("\r\n");
        if (event.getRrule() != null)
            sb.append("RRULE:").append(event.getRrule()).append("\r\n");
        if (event.getStatus() != null)
            sb.append("STATUS:").append(event.getStatus()).append("\r\n");
        if (event.getPriority() > 0)
            sb.append("PRIORITY:").append(event.getPriority()).append("\r\n");
        if (event.getCategories() != null)
            sb.append("CATEGORIES:").append(event.getCategories()).append("\r\n");
        if (event.getUrl() != null)
            sb.append("URL:").append(event.getUrl()).append("\r\n");
        sb.append("SEQUENCE:").append(event.getSequence()).append("\r\n");
        sb.append("DTSTAMP:").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss"))).append("\r\n");
        sb.append("END:VEVENT\r\nEND:VCALENDAR\r\n");
        return sb.toString();
    }

    private void parseVcardIntoContact(String vcardData, Contact contact) {
        String fn = extractValue(vcardData.replace("\r\n", "\n"), "FN");
        String email = extractValue(vcardData.replace("\r\n", "\n"), "EMAIL");
        String tel = extractValue(vcardData.replace("\r\n", "\n"), "TEL");
        String org = extractValue(vcardData.replace("\r\n", "\n"), "ORG");
        if (fn != null) contact.setName(fn);
        if (email != null) contact.setEmail(email);
        if (tel != null) contact.setPhone(tel);
        if (org != null) contact.setOrganization(org);
    }

    private String rebuildVcard(Contact contact) {
        var sb = new StringBuilder();
        sb.append("BEGIN:VCARD\r\nVERSION:3.0\r\nFN:").append(contact.getName()).append("\r\n");
        sb.append("N:").append(contact.getName()).append(";;;\r\n");
        if (contact.getEmail() != null)
            sb.append("EMAIL;TYPE=INTERNET:").append(contact.getEmail()).append("\r\n");
        if (contact.getPhone() != null)
            sb.append("TEL:").append(contact.getPhone()).append("\r\n");
        if (contact.getOrganization() != null)
            sb.append("ORG:").append(contact.getOrganization()).append("\r\n");
        sb.append("END:VCARD\r\n");
        return sb.toString();
    }
}
