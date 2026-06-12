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
import com.jinelei.scalefish.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HexFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/webdav")
public class WebDavController {

    private static final Logger log = LoggerFactory.getLogger(WebDavController.class);

    private static final DateTimeFormatter ICAL_DT_FMT =
        DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss");
    private static final DateTimeFormatter ICAL_DATE_FMT =
        DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final DateTimeFormatter HTTP_DATE_FMT =
        DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss 'GMT'")
            .withZone(ZoneId.of("GMT"));

    private final CalendarRepository calendarRepository;
    private final CalendarEventRepository calendarEventRepository;
    private final AddressBookRepository addressBookRepository;
    private final ContactRepository contactRepository;
    private final UserRepository userRepository;

    public WebDavController(CalendarRepository calendarRepository,
                             CalendarEventRepository calendarEventRepository,
                             AddressBookRepository addressBookRepository,
                             ContactRepository contactRepository,
                             UserRepository userRepository) {
        this.calendarRepository = calendarRepository;
        this.calendarEventRepository = calendarEventRepository;
        this.addressBookRepository = addressBookRepository;
        this.contactRepository = contactRepository;
        this.userRepository = userRepository;
    }

    private User currentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return null;
        Object principal = auth.getPrincipal();
        if (principal instanceof User user) {
            return user;
        }
        if (principal instanceof UserDetails userDetails) {
            return userRepository.findByUsername(userDetails.getUsername()).orElse(null);
        }
        return null;
    }

    @RequestMapping(value = "/**")
    public void handle(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String method = request.getMethod();
        String path = extractPath(request.getRequestURI());
        log.debug("→ {} {} (remote={}, user-agent={})",
            method, path, request.getRemoteAddr(), request.getHeader("User-Agent"));

        User user = currentUser();
        if (user == null) {
            log.warn("Authentication failed for {} {}: user not found or not authenticated", method, path);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        switch (method.toUpperCase()) {
            case "OPTIONS" -> handleOptions(response);
            case "PROPFIND" -> handlePropfind(request, response, path, user);
            case "GET" -> handleGet(request, response, path, user);
            case "HEAD" -> handleGet(request, response, path, user);
            case "PUT" -> handlePut(request, response, path, user);
            case "DELETE" -> handleDelete(response, path, user);
            case "REPORT" -> handleReport(request, response, path, user);
            case "MKCOL", "MKCALENDAR" -> handleMkcol(request, response, path, user);
            case "PROPPATCH" -> handleProppatch(request, response, path);
            case "MOVE" -> {
                log.debug("MOVE not implemented: {}", path);
                response.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED);
            }
            case "COPY" -> {
                log.debug("COPY not implemented: {}", path);
                response.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED);
            }
            case "LOCK" -> handleLock(response, path);
            case "UNLOCK" -> {
                log.debug("UNLOCK {}", path);
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            }
            default -> {
                log.debug("Unsupported method {} for {}", method, path);
                response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            }
        }
    }

    private void handleOptions(HttpServletResponse response) {
        response.setHeader("DAV", "1, 2, 3, calendar-access, addressbook-access");
        response.setHeader("Allow", "OPTIONS, PROPFIND, GET, HEAD, PUT, DELETE, REPORT, MKCOL, MKCALENDAR, PROPPATCH, MOVE, COPY, LOCK, UNLOCK");
        response.setHeader("MS-Author-Via", "DAV");
    }

    // ──────────────────────────────────────────────
    // PROPFIND
    // ──────────────────────────────────────────────

    private void handlePropfind(HttpServletRequest request, HttpServletResponse response,
                                 String path, User user) throws IOException {
        String depth = request.getHeader("Depth");
        if (depth == null) depth = "1";
        log.debug("PROPFIND {} depth={}", path, depth);

        String baseUrl = request.getScheme() + "://" + request.getHeader("Host") + "/webdav";

        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        xml.append("<multistatus xmlns=\"DAV:\"");
        xml.append(" xmlns:C=\"urn:ietf:params:xml:ns:caldav\"");
        xml.append(" xmlns:CR=\"urn:ietf:params:xml:ns:carddav\"");
        xml.append(" xmlns:CS=\"http://calendarserver.org/ns/\">");

        if (path.isEmpty() || path.equals("/")) {
            addRootResponse(xml, baseUrl, user);
            if (!"0".equals(depth)) {
                addResponse(xml, baseUrl + "/calendars/", "collection", "Calendars", null, null, null);
                listUserCalendarsPropfind(xml, user, baseUrl, depth);
                addResponse(xml, baseUrl + "/contacts/", "collection", "Contacts", null, null, null);
                listUserAddressBooksPropfind(xml, user, baseUrl, depth);
            }
        } else if (path.startsWith("/calendars")) {
            handleCalendarPropfind(xml, path, user, baseUrl, depth);
        } else if (path.startsWith("/contacts")) {
            handleContactPropfind(xml, path, user, baseUrl, depth);
        } else if (path.startsWith("/principal")) {
            addPrincipalResponse(xml, baseUrl, user);
        }

        xml.append("</multistatus>");
        response.setContentType("application/xml; charset=utf-8");
        response.setStatus(207);
        response.getWriter().write(xml.toString());
    }

    private void addRootResponse(StringBuilder xml, String baseUrl, User user) {
        xml.append("<response><href>").append(escapeXml(baseUrl + "/")).append("</href><propstat><prop>");
        xml.append("<resourcetype><collection/></resourcetype>");
        xml.append("<displayname>Scalefish</displayname>");
        xml.append("<current-user-principal><href>").append(escapeXml(baseUrl + "/principal/")).append("</href></current-user-principal>");
        xml.append("<principal-collection-set><href>").append(escapeXml(baseUrl + "/principal/")).append("</href></principal-collection-set>");
        xml.append("<calendar-home-set><href>").append(escapeXml(baseUrl + "/calendars/")).append("</href></calendar-home-set>");
        xml.append("<addressbook-home-set><href>").append(escapeXml(baseUrl + "/contacts/")).append("</href></addressbook-home-set>");
        xml.append("<CS:getctag>").append(ctag(user)).append("</CS:getctag>");
        xml.append("</prop><status>HTTP/1.1 200 OK</status></propstat></response>");
    }

    private void addPrincipalResponse(StringBuilder xml, String baseUrl, User user) {
        xml.append("<response><href>").append(escapeXml(baseUrl + "/principal/")).append("</href><propstat><prop>");
        xml.append("<resourcetype><collection/></resourcetype>");
        xml.append("<displayname>Principal</displayname>");
        xml.append("<current-user-principal><href>").append(escapeXml(baseUrl + "/principal/")).append("</href></current-user-principal>");
        xml.append("<calendar-home-set><href>").append(escapeXml(baseUrl + "/calendars/")).append("</href></calendar-home-set>");
        xml.append("<addressbook-home-set><href>").append(escapeXml(baseUrl + "/contacts/")).append("</href></addressbook-home-set>");
        xml.append("</prop><status>HTTP/1.1 200 OK</status></propstat></response>");
    }

    private String eventUrl(String baseUrl, Long calId, CalendarEvent event) {
        String filename = event.getUid() != null ? event.getUid() : String.valueOf(event.getId());
        return baseUrl + "/calendars/" + calId + "/" + filename + ".ics";
    }

    private void listUserCalendarsPropfind(StringBuilder xml, User user, String baseUrl, String depth) {
        for (var cal : calendarRepository.findByUserIdOrderByCreatedAtAsc(user.getId())) {
            String calUrl = baseUrl + "/calendars/" + cal.getId() + "/";
            addCalendarResponse(xml, calUrl, cal);
            if ("infinity".equals(depth)) {
                for (var event : calendarEventRepository.findByCalendarIdOrderByStartTimeAsc(cal.getId())) {
                    addEventResponse(xml, eventUrl(baseUrl, cal.getId(), event), event);
                }
            }
        }
    }

    private void listUserAddressBooksPropfind(StringBuilder xml, User user, String baseUrl, String depth) {
        for (var ab : addressBookRepository.findByUserIdOrderByCreatedAtAsc(user.getId())) {
            String abUrl = baseUrl + "/contacts/" + ab.getId() + "/";
            addAddressBookResponse(xml, abUrl, ab);
            if ("infinity".equals(depth)) {
                for (var contact : contactRepository.findByAddressBookIdOrderByNameAsc(ab.getId())) {
                    addContactResponse(xml, abUrl + contact.getId() + ".vcf", contact);
                }
            }
        }
    }

    private java.util.Optional<CalendarEvent> findEventByFilename(Long calId, String filename) {
        try {
            Long eventId = Long.parseLong(filename);
            var byId = calendarEventRepository.findById(eventId);
            if (byId.isPresent() && byId.get().getCalendar().getId().equals(calId)) {
                return byId;
            }
        } catch (NumberFormatException ignored) {}
        return calendarEventRepository.findByUidAndCalendarId(filename, calId);
    }

    private void handleCalendarPropfind(StringBuilder xml, String path, User user,
                                         String baseUrl, String depth) {
        String rest = path.substring("/calendars".length());
        if (rest.isEmpty() || rest.equals("/")) {
            addResponse(xml, baseUrl + "/calendars/", "collection", "Calendars", null, null, null);
            if (!"0".equals(depth)) {
                listUserCalendarsPropfind(xml, user, baseUrl, depth);
            }
            return;
        }
        String clean = rest.startsWith("/") ? rest.substring(1) : rest;
        if (clean.endsWith("/")) clean = clean.substring(0, clean.length() - 1);

        if (clean.endsWith(".ics")) {
            String filename = clean.substring(0, clean.length() - 4);
            int slashIdx = filename.lastIndexOf('/');
            if (slashIdx >= 0) {
                String calIdStr = filename.substring(0, slashIdx);
                String eventFile = filename.substring(slashIdx + 1);
                try {
                    Long calId = Long.parseLong(calIdStr);
                    findEventByFilename(calId, eventFile).ifPresent(event -> {
                        if (event.getCalendar().getUser().getId().equals(user.getId())) {
                            addEventResponse(xml, baseUrl + path, event);
                        }
                    });
                } catch (NumberFormatException ignored) {}
            }
            return;
        }

        try {
            Long calId = Long.parseLong(clean);
            calendarRepository.findById(calId).ifPresent(cal -> {
                if (cal.getUser().getId().equals(user.getId())) {
                    String calUrl = baseUrl + "/calendars/" + calId + "/";
                    addCalendarResponse(xml, calUrl, cal);
                    if (!"0".equals(depth)) {
                        for (var event : calendarEventRepository.findByCalendarIdOrderByStartTimeAsc(calId)) {
                            addEventResponse(xml, eventUrl(baseUrl, calId, event), event);
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
            addResponse(xml, baseUrl + "/contacts/", "collection", "Contacts", null, null, null);
            if (!"0".equals(depth)) {
                listUserAddressBooksPropfind(xml, user, baseUrl, depth);
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
                        addContactResponse(xml, baseUrl + path, contact);
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
                    addAddressBookResponse(xml, abUrl, ab);
                    if (!"0".equals(depth)) {
                        for (var contact : contactRepository.findByAddressBookIdOrderByNameAsc(abId)) {
                            addContactResponse(xml, abUrl + contact.getId() + ".vcf", contact);
                        }
                    }
                }
            });
        } catch (NumberFormatException ignored) {}
    }

    // ──────────────────────────────────────────────
    // PROPFIND response builders
    // ──────────────────────────────────────────────

    private void addResponse(StringBuilder xml, String href, String resType,
                              String displayName, String contentType, Long contentLength, String etag) {
        xml.append("<response><href>").append(escapeXml(href)).append("</href><propstat><prop>");
        xml.append("<displayname>").append(escapeXml(displayName != null ? displayName : "")).append("</displayname>");
        xml.append("<resourcetype>");
        if (resType != null) {
            xml.append("<collection/>");
        }
        xml.append("</resourcetype>");
        if (contentType != null) {
            xml.append("<getcontenttype>").append(escapeXml(contentType)).append("</getcontenttype>");
        }
        if (contentLength != null) {
            xml.append("<getcontentlength>").append(contentLength).append("</getcontentlength>");
        }
        if (etag != null) {
            xml.append("<getetag>").append(etag).append("</getetag>");
        }
        xml.append("</prop><status>HTTP/1.1 200 OK</status></propstat></response>");
    }

    private void addCalendarResponse(StringBuilder xml, String href, CalendarEntity cal) {
        String etag = "\"" + cal.getId() + "-" + cal.getUpdatedAt().format(ICAL_DT_FMT) + "\"";
        xml.append("<response><href>").append(escapeXml(href)).append("</href><propstat><prop>");
        xml.append("<displayname>").append(escapeXml(cal.getName())).append("</displayname>");
        xml.append("<resourcetype><collection/><C:calendar/></resourcetype>");
        xml.append("<getcontenttype>httpd/unix-directory</getcontenttype>");
        xml.append("<getetag>").append(etag).append("</getetag>");
        xml.append("<C:calendar-description>").append(escapeXml(cal.getDescription() != null ? cal.getDescription() : "")).append("</C:calendar-description>");
        xml.append("<C:calendar-color>").append(escapeXml(cal.getDisplayColor() != null ? cal.getDisplayColor() : "#1C6DD0")).append("</C:calendar-color>");
        xml.append("<C:supported-calendar-component-set><C:comp name=\"VEVENT\"/></C:supported-calendar-component-set>");
        xml.append("<CS:getctag>").append(etag).append("</CS:getctag>");
        xml.append("<getlastmodified>").append(formatHttpDate(cal.getUpdatedAt())).append("</getlastmodified>");
        xml.append("<creationdate>").append(formatHttpDate(cal.getCreatedAt())).append("</creationdate>");
        xml.append("</prop><status>HTTP/1.1 200 OK</status></propstat></response>");
    }

    private void addEventResponse(StringBuilder xml, String href, CalendarEvent event) {
        String etag = "\"" + event.getId() + "-" + event.getUpdatedAt().format(ICAL_DT_FMT) + "\"";
        String content = event.getIcalData();
        if (content == null) content = rebuildIcal(event);
        long len = content.getBytes(StandardCharsets.UTF_8).length;
        xml.append("<response><href>").append(escapeXml(href)).append("</href><propstat><prop>");
        xml.append("<displayname>").append(escapeXml(event.getTitle())).append("</displayname>");
        xml.append("<resourcetype/>");
        xml.append("<getcontenttype>text/calendar; charset=utf-8</getcontenttype>");
        xml.append("<getcontentlength>").append(len).append("</getcontentlength>");
        xml.append("<getetag>").append(etag).append("</getetag>");
        xml.append("<getlastmodified>").append(formatHttpDate(event.getUpdatedAt())).append("</getlastmodified>");
        xml.append("<creationdate>").append(formatHttpDate(event.getCreatedAt())).append("</creationdate>");
        xml.append("</prop><status>HTTP/1.1 200 OK</status></propstat></response>");
    }

    private void addAddressBookResponse(StringBuilder xml, String href, AddressBook ab) {
        String etag = "\"" + ab.getId() + "-" + ab.getUpdatedAt().format(ICAL_DT_FMT) + "\"";
        xml.append("<response><href>").append(escapeXml(href)).append("</href><propstat><prop>");
        xml.append("<displayname>").append(escapeXml(ab.getName())).append("</displayname>");
        xml.append("<resourcetype><collection/><CR:addressbook/></resourcetype>");
        xml.append("<getcontenttype>httpd/unix-directory</getcontenttype>");
        xml.append("<getetag>").append(etag).append("</getetag>");
        xml.append("<CR:supported-addressbook-component-set><CR:comp name=\"VCARD\"/></CR:supported-addressbook-component-set>");
        xml.append("<CS:getctag>").append(etag).append("</CS:getctag>");
        xml.append("<getlastmodified>").append(formatHttpDate(ab.getUpdatedAt())).append("</getlastmodified>");
        xml.append("<creationdate>").append(formatHttpDate(ab.getCreatedAt())).append("</creationdate>");
        xml.append("</prop><status>HTTP/1.1 200 OK</status></propstat></response>");
    }

    private void addContactResponse(StringBuilder xml, String href, Contact contact) {
        String etag = "\"" + contact.getId() + "-" + contact.getUpdatedAt().format(ICAL_DT_FMT) + "\"";
        String content = contact.getVcardData();
        if (content == null) content = rebuildVcard(contact);
        long len = content.getBytes(StandardCharsets.UTF_8).length;
        xml.append("<response><href>").append(escapeXml(href)).append("</href><propstat><prop>");
        xml.append("<displayname>").append(escapeXml(contact.getName())).append("</displayname>");
        xml.append("<resourcetype/>");
        xml.append("<getcontenttype>text/vcard; charset=utf-8</getcontenttype>");
        xml.append("<getcontentlength>").append(len).append("</getcontentlength>");
        xml.append("<getetag>").append(etag).append("</getetag>");
        xml.append("<getlastmodified>").append(formatHttpDate(contact.getUpdatedAt())).append("</getlastmodified>");
        xml.append("<creationdate>").append(formatHttpDate(contact.getCreatedAt())).append("</creationdate>");
        xml.append("</prop><status>HTTP/1.1 200 OK</status></propstat></response>");
    }

    // ──────────────────────────────────────────────
    // GET / HEAD
    // ──────────────────────────────────────────────

    private void handleGet(HttpServletRequest request, HttpServletResponse response,
                            String path, User user) throws IOException {
        boolean isHead = "HEAD".equals(request.getMethod());
        log.debug("{} {} (user={})", isHead ? "HEAD" : "GET", path, user.getUsername());

        if (path.endsWith(".ics")) {
            CalendarEvent event = resolveEventFromPath(path, user);
            if (event != null) {
                log.debug("GET .ics found: eventId={}", event.getId());
                String ical = event.getIcalData();
                if (ical == null) ical = rebuildIcal(event);
                byte[] bytes = ical.getBytes(StandardCharsets.UTF_8);
                String etag = "\"" + event.getId() + "-" + event.getUpdatedAt().format(ICAL_DT_FMT) + "\"";
                response.setContentType("text/calendar; charset=utf-8");
                response.setContentLength(bytes.length);
                response.setHeader("ETag", etag);
                response.setHeader("Last-Modified", formatHttpDate(event.getUpdatedAt()));
                if (!isHead) {
                    response.getWriter().write(ical);
                }
                return;
            }
            log.debug("GET .ics not found: {}", path);
        } else if (path.endsWith(".vcf")) {
            Matcher m = Pattern.compile("/contacts/(\\d+)/(\\d+)\\.vcf").matcher(path);
            if (m.find()) {
                Long contactId = Long.parseLong(m.group(2));
                var opt = contactRepository.findById(contactId);
                if (opt.isPresent() && opt.get().getAddressBook().getUser().getId().equals(user.getId())) {
                    log.debug("GET .vcf found: contactId={}", contactId);
                    String vcard = opt.get().getVcardData();
                    if (vcard == null) vcard = rebuildVcard(opt.get());
                    byte[] bytes = vcard.getBytes(StandardCharsets.UTF_8);
                    String etag = "\"" + opt.get().getId() + "-" + opt.get().getUpdatedAt().format(ICAL_DT_FMT) + "\"";
                    response.setContentType("text/vcard; charset=utf-8");
                    response.setContentLength(bytes.length);
                    response.setHeader("ETag", etag);
                    response.setHeader("Last-Modified", formatHttpDate(opt.get().getUpdatedAt()));
                    if (!isHead) {
                        response.getWriter().write(vcard);
                    }
                    return;
                }
                log.debug("GET .vcf not found or access denied: contactId={}", contactId);
            }
        }
        log.debug("GET resource not found: {}", path);
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    // ──────────────────────────────────────────────
    // PUT
    // ──────────────────────────────────────────────

    private void handlePut(HttpServletRequest request, HttpServletResponse response,
                            String path, User user) throws IOException {
        String body = readBody(request.getInputStream());
        String ifNoneMatch = request.getHeader("If-None-Match");
        log.debug("PUT {} bodySize={} If-None-Match={} (user={})",
            path, body.length(), ifNoneMatch, user.getUsername());

        if (path.endsWith(".ics")) {
            Matcher m = Pattern.compile("/calendars/(\\d+)/([^/]+)\\.ics").matcher(path);
            if (m.find()) {
                Long calId = Long.parseLong(m.group(1));
                String filename = m.group(2);
                var calOpt = calendarRepository.findById(calId);
                if (calOpt.isEmpty() || !calOpt.get().getUser().getId().equals(user.getId())) {
                    log.debug("PUT calendar not found or forbidden: calId={}", calId);
                    response.sendError(HttpServletResponse.SC_FORBIDDEN);
                    return;
                }
                var opt = findEventByFilename(calId, filename);
                if (opt.isPresent() && "*".equals(ifNoneMatch)) {
                    log.debug("PUT precondition failed (If-None-Match: *): event already exists");
                    response.setStatus(HttpServletResponse.SC_PRECONDITION_FAILED);
                    return;
                }
                if (opt.isPresent()) {
                    var event = opt.get();
                    event.setIcalData(body);
                    parseIcalIntoEvent(body, event);
                    calendarEventRepository.save(event);
                    log.debug("PUT updated eventId={}", event.getId());
                    response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                    response.setHeader("ETag", "\"" + event.getId() + "-" + event.getUpdatedAt().format(ICAL_DT_FMT) + "\"");
                } else {
                    var event = new CalendarEvent();
                    event.setCalendar(calOpt.get());
                    event.setIcalData(body);
                    parseIcalIntoEvent(body, event);
                    if (event.getUid() == null && !isNumeric(filename)) {
                        event.setUid(filename);
                    }
                    if (event.getTitle() == null) event.setTitle("Event");
                    calendarEventRepository.save(event);
                    log.debug("PUT created eventId={} uid={}", event.getId(), event.getUid());
                    response.setStatus(HttpServletResponse.SC_CREATED);
                    response.setHeader("ETag", "\"" + event.getId() + "-" + event.getUpdatedAt().format(ICAL_DT_FMT) + "\"");
                }
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
                    log.debug("PUT updated contactId={}", contactId);
                    response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                    response.setHeader("ETag", "\"" + contact.getId() + "-" + contact.getUpdatedAt().format(ICAL_DT_FMT) + "\"");
                    return;
                }
                log.debug("PUT contact not found: contactId={}", contactId);
            }
        }
        log.debug("PUT resource not found: {}", path);
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    // ──────────────────────────────────────────────
    // DELETE
    // ──────────────────────────────────────────────

    private void handleDelete(HttpServletResponse response, String path, User user) throws IOException {
        log.debug("DELETE {} (user={})", path, user.getUsername());
        if (path.endsWith(".ics")) {
            Matcher m = Pattern.compile("/calendars/(\\d+)/([^/]+)\\.ics").matcher(path);
            if (m.find()) {
                Long calId = Long.parseLong(m.group(1));
                String filename = m.group(2);
                var opt = findEventByFilename(calId, filename);
                if (opt.isPresent() && opt.get().getCalendar().getUser().getId().equals(user.getId())) {
                    calendarEventRepository.delete(opt.get());
                    log.debug("DELETE ics success: eventId={}", opt.get().getId());
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
                    log.debug("DELETE vcf success: contactId={}", contactId);
                    response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                    return;
                }
            }
        }
        log.debug("DELETE resource not found: {}", path);
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    // ──────────────────────────────────────────────
    // REPORT
    // ──────────────────────────────────────────────

    private void handleReport(HttpServletRequest request, HttpServletResponse response,
                               String path, User user) throws IOException {
        String body = readBody(request.getInputStream());
        String baseUrl = request.getScheme() + "://" + request.getHeader("Host") + "/webdav";
        log.debug("REPORT {} bodySize={} (user={})", path, body.length(), user.getUsername());

        if (body.contains("addressbook-multiget")) {
            log.debug("REPORT type=addressbook-multiget");
            handleAddressbookMultiget(request, response, path, user, baseUrl, body);
            return;
        }
        if (body.contains("addressbook-query")) {
            log.debug("REPORT type=addressbook-query");
            handleAddressbookQuery(request, response, path, user, baseUrl, body);
            return;
        }
        if (body.contains("calendar-multiget")) {
            log.debug("REPORT type=calendar-multiget");
            handleCalendarMultiget(request, response, path, user, baseUrl, body);
            return;
        }
        if (body.contains("calendar-query")) {
            log.debug("REPORT type=calendar-query");
            handleCalendarQuery(request, response, path, user, baseUrl, body);
            return;
        }
        if (body.contains("principal-property-search") || body.contains("principal-search-property-set")) {
            log.debug("REPORT type=principal-property-search");
            handlePrincipalReport(response);
            return;
        }

        log.debug("REPORT type=unknown, not implemented");
        response.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED);
    }

    private void handleCalendarQuery(HttpServletRequest request, HttpServletResponse response,
                                      String path, User user, String baseUrl, String body) throws IOException {
        String depth = request.getHeader("Depth");
        if (depth == null) depth = "1";

        String calIdStr = extractPathSegment(path, "/calendars/");
        if (calIdStr == null) {
            log.debug("REPORT calendar-query: no calendar id in path: {}", path);
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        if (calIdStr.endsWith("/")) calIdStr = calIdStr.substring(0, calIdStr.length() - 1);

        Long calId;
        try {
            calId = Long.parseLong(calIdStr);
        } catch (NumberFormatException e) {
            log.debug("REPORT calendar-query: invalid calendar id: {}", calIdStr);
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        List<CalendarEvent> events;
        String timeStart = extractXmlValue(body, "start");
        String timeEnd = extractXmlValue(body, "end");

        if (timeStart != null || timeEnd != null) {
            LocalDateTime start = timeStart != null ? parseIcalDt(timeStart) : LocalDateTime.MIN;
            LocalDateTime end = timeEnd != null ? parseIcalDt(timeEnd) : LocalDateTime.MAX;
            log.debug("REPORT calendar-query calId={} timeRange=[{}, {}]", calId, start, end);
            events = calendarEventRepository.findByCalendarIdAndStartTimeBetweenOrderByStartTimeAsc(calId, start, end);
        } else {
            log.debug("REPORT calendar-query calId={} (no time filter)", calId);
            events = calendarEventRepository.findByCalendarIdOrderByStartTimeAsc(calId);
        }
        log.debug("REPORT calendar-query: found {} events", events.size());

        boolean wantData = body.contains("calendar-data");
        boolean wantEtag = body.contains("getetag");

        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        xml.append("<multistatus xmlns=\"DAV:\" xmlns:C=\"urn:ietf:params:xml:ns:caldav\">");

        for (var event : events) {
            if (!event.getCalendar().getUser().getId().equals(user.getId())) continue;
            String url = eventUrl(baseUrl, calId, event);
            String etag = "\"" + event.getId() + "-" + event.getUpdatedAt().format(ICAL_DT_FMT) + "\"";

            xml.append("<response><href>").append(escapeXml(url)).append("</href><propstat><prop>");
            if (wantEtag) {
                xml.append("<getetag>").append(etag).append("</getetag>");
            }
            if (wantData) {
                String ical = event.getIcalData();
                if (ical == null) ical = rebuildIcal(event);
                xml.append("<C:calendar-data>").append(escapeXml(ical)).append("</C:calendar-data>");
            }
            xml.append("</prop><status>HTTP/1.1 200 OK</status></propstat></response>");
        }

        xml.append("</multistatus>");
        response.setContentType("application/xml; charset=utf-8");
        response.setStatus(207);
        response.getWriter().write(xml.toString());
    }

    private void handleCalendarMultiget(HttpServletRequest request, HttpServletResponse response,
                                         String path, User user, String baseUrl, String body) throws IOException {
        log.debug("REPORT calendar-multiget path={}", path);

        boolean wantData = body.contains("calendar-data");
        boolean wantEtag = body.contains("getetag");

        Pattern hrefPattern = Pattern.compile("<D:href>(.*?)</D:href>");
        Matcher m = hrefPattern.matcher(body);

        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        xml.append("<multistatus xmlns=\"DAV:\" xmlns:C=\"urn:ietf:params:xml:ns:caldav\">");

        while (m.find()) {
            String href = m.group(1).trim();
            Matcher pathMatcher = Pattern.compile("/calendars/(\\d+)/([^/]+)\\.ics").matcher(href);
            if (!pathMatcher.find()) {
                xml.append("<response><href>").append(escapeXml(href)).append("</href><status>HTTP/1.1 404 Not Found</status></response>");
                continue;
            }
            Long calId = Long.parseLong(pathMatcher.group(1));
            String filename = pathMatcher.group(2);
            var eventOpt = findEventByFilename(calId, filename);
            if (eventOpt.isEmpty() || !eventOpt.get().getCalendar().getUser().getId().equals(user.getId())) {
                xml.append("<response><href>").append(escapeXml(href)).append("</href><status>HTTP/1.1 404 Not Found</status></response>");
                continue;
            }
            var event = eventOpt.get();
            String etag = "\"" + event.getId() + "-" + event.getUpdatedAt().format(ICAL_DT_FMT) + "\"";

            xml.append("<response><href>").append(escapeXml(href)).append("</href><propstat><prop>");
            if (wantEtag) {
                xml.append("<getetag>").append(etag).append("</getetag>");
            }
            if (wantData) {
                String ical = event.getIcalData();
                if (ical == null) ical = rebuildIcal(event);
                xml.append("<C:calendar-data>").append(escapeXml(ical)).append("</C:calendar-data>");
            }
            xml.append("</prop><status>HTTP/1.1 200 OK</status></propstat></response>");
        }

        xml.append("</multistatus>");
        response.setContentType("application/xml; charset=utf-8");
        response.setStatus(207);
        response.getWriter().write(xml.toString());
    }

    private void handleAddressbookMultiget(HttpServletRequest request, HttpServletResponse response,
                                            String path, User user, String baseUrl, String body) throws IOException {
        boolean wantData = body.contains("address-data");
        boolean wantEtag = body.contains("getetag");
        log.debug("REPORT addressbook-multiget path={} wantData={} wantEtag={}", path, wantData, wantEtag);

        Pattern hrefPattern = Pattern.compile("<D:href>(.*?)</D:href>");
        Matcher m = hrefPattern.matcher(body);

        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        xml.append("<multistatus xmlns=\"DAV:\" xmlns:CR=\"urn:ietf:params:xml:ns:carddav\">");

        while (m.find()) {
            String href = m.group(1).trim();
            Matcher idMatcher = Pattern.compile("/(\\d+)\\.vcf").matcher(href);
            if (!idMatcher.find()) continue;
            Long contactId = Long.parseLong(idMatcher.group(1));
            var opt = contactRepository.findById(contactId);
            if (opt.isEmpty() || !opt.get().getAddressBook().getUser().getId().equals(user.getId())) continue;
            var contact = opt.get();

            String etag = "\"" + contact.getId() + "-" + contact.getUpdatedAt().format(ICAL_DT_FMT) + "\"";
            xml.append("<response><href>").append(escapeXml(href)).append("</href><propstat><prop>");
            if (wantEtag) {
                xml.append("<getetag>").append(etag).append("</getetag>");
            }
            if (wantData) {
                String vcard = contact.getVcardData();
                if (vcard == null) vcard = rebuildVcard(contact);
                xml.append("<CR:address-data>").append(escapeXml(vcard)).append("</CR:address-data>");
            }
            xml.append("</prop><status>HTTP/1.1 200 OK</status></propstat></response>");
        }

        xml.append("</multistatus>");
        response.setContentType("application/xml; charset=utf-8");
        response.setStatus(207);
        response.getWriter().write(xml.toString());
    }

    private void handleAddressbookQuery(HttpServletRequest request, HttpServletResponse response,
                                         String path, User user, String baseUrl, String body) throws IOException {
        String abIdStr = extractPathSegment(path, "/contacts/");
        if (abIdStr == null) {
            log.debug("REPORT addressbook-query: no addressbook id in path: {}", path);
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        if (abIdStr.endsWith("/")) abIdStr = abIdStr.substring(0, abIdStr.length() - 1);

        Long abId;
        try {
            abId = Long.parseLong(abIdStr);
        } catch (NumberFormatException e) {
            log.debug("REPORT addressbook-query: invalid addressbook id: {}", abIdStr);
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        boolean wantData = body.contains("address-data");
        boolean wantEtag = body.contains("getetag");
        log.debug("REPORT addressbook-query abId={} wantData={} wantEtag={}", abId, wantData, wantEtag);

        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        xml.append("<multistatus xmlns=\"DAV:\" xmlns:CR=\"urn:ietf:params:xml:ns:carddav\">");

        for (var contact : contactRepository.findByAddressBookIdOrderByNameAsc(abId)) {
            if (!contact.getAddressBook().getUser().getId().equals(user.getId())) continue;
            String url = baseUrl + "/contacts/" + abId + "/" + contact.getId() + ".vcf";
            String etag = "\"" + contact.getId() + "-" + contact.getUpdatedAt().format(ICAL_DT_FMT) + "\"";

            xml.append("<response><href>").append(escapeXml(url)).append("</href><propstat><prop>");
            if (wantEtag) {
                xml.append("<getetag>").append(etag).append("</getetag>");
            }
            if (wantData) {
                String vcard = contact.getVcardData();
                if (vcard == null) vcard = rebuildVcard(contact);
                xml.append("<CR:address-data>").append(escapeXml(vcard)).append("</CR:address-data>");
            }
            xml.append("</prop><status>HTTP/1.1 200 OK</status></propstat></response>");
        }

        xml.append("</multistatus>");
        response.setContentType("application/xml; charset=utf-8");
        response.setStatus(207);
        response.getWriter().write(xml.toString());
    }

    private void handlePrincipalReport(HttpServletResponse response) throws IOException {
        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        xml.append("<multistatus xmlns=\"DAV:\">");
        xml.append("<response><href>/webdav/principal/</href>");
        xml.append("<propstat><prop><resourcetype><collection/></resourcetype></prop><status>HTTP/1.1 200 OK</status></propstat>");
        xml.append("</response></multistatus>");
        response.setContentType("application/xml; charset=utf-8");
        response.setStatus(207);
        response.getWriter().write(xml.toString());
    }

    // ──────────────────────────────────────────────
    // MKCOL / MKCALENDAR
    // ──────────────────────────────────────────────

    private void handleMkcol(HttpServletRequest request, HttpServletResponse response,
                              String path, User user) throws IOException {
        String body = readBody(request.getInputStream());
        log.debug("MKCOL/MKCALENDAR {} bodySize={} (user={})", path, body.length(), user.getUsername());

        if (path.startsWith("/calendars/")) {
            String name = extractCalendarName(body);
            if (name == null) {
                name = path.substring("/calendars/".length());
                if (name.endsWith("/")) name = name.substring(0, name.length() - 1);
            }
            if (!name.isEmpty()) {
                String color = extractCalendarColor(body);
                String desc = extractCalendarDesc(body);
                log.debug("MKCOL creating calendar: name={}, color={}", name, color);
                var cal = new CalendarEntity();
                cal.setName(name);
                cal.setDescription(desc != null ? desc : name);
                cal.setDisplayColor(color != null ? color : "#1C6DD0");
                cal.setUser(user);
                calendarRepository.save(cal);
                log.debug("MKCOL calendar created: id={}", cal.getId());
                response.setStatus(HttpServletResponse.SC_CREATED);
                return;
            }
            log.debug("MKCOL empty calendar name");
        }
        log.debug("MKCOL not allowed for path: {}", path);
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }

    // ──────────────────────────────────────────────
    // PROPPATCH
    // ──────────────────────────────────────────────

    private void handleProppatch(HttpServletRequest request, HttpServletResponse response,
                                  String path) throws IOException {
        String body = readBody(request.getInputStream());
        log.debug("PROPPATCH {} bodySize={}", path, body.length());

        boolean hasDisplayName = body.contains("displayname");
        boolean hasCalendarColor = body.contains("calendar-color");
        boolean hasCalendarDesc = body.contains("calendar-description");

        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        xml.append("<multistatus xmlns=\"DAV:\" xmlns:C=\"urn:ietf:params:xml:ns:caldav\">");

        if (path.startsWith("/calendars/")) {
            String clean = path.replace("/calendars/", "").replace("/", "");
            try {
                Long calId = Long.parseLong(clean);
                var opt = calendarRepository.findById(calId);
                if (opt.isPresent()) {
                    var cal = opt.get();
                    if (hasDisplayName) {
                        String name = extractXmlValue(body, "displayname");
                        if (name != null) cal.setName(name);
                    }
                    if (hasCalendarColor) {
                        String color = extractXmlValue(body, "calendar-color");
                        if (color != null) cal.setDisplayColor(color);
                    }
                    if (hasCalendarDesc) {
                        String desc = extractXmlValue(body, "calendar-description");
                        if (desc != null) cal.setDescription(desc);
                    }
                    calendarRepository.save(cal);
                }
            } catch (NumberFormatException ignored) {}
        }

        xml.append("<response><href>").append(escapeXml(path)).append("</href>");
        xml.append("<propstat><prop>");
        if (hasDisplayName) xml.append("<displayname/>");
        if (hasCalendarColor) xml.append("<C:calendar-color/>");
        if (hasCalendarDesc) xml.append("<C:calendar-description/>");
        xml.append("</prop><status>HTTP/1.1 200 OK</status></propstat>");
        xml.append("</response></multistatus>");

        response.setContentType("application/xml; charset=utf-8");
        response.setStatus(207);
        response.getWriter().write(xml.toString());
    }

    // ──────────────────────────────────────────────
    // LOCK
    // ──────────────────────────────────────────────

    private void handleLock(HttpServletResponse response, String path) throws IOException {
        log.debug("LOCK {}", path);
        String token = "opaquelocktoken:sf-" + System.currentTimeMillis();
        String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
            + "<D:prop xmlns:D=\"DAV:\"><D:lockdiscovery><D:activelock>"
            + "<D:locktype><D:write/></D:locktype>"
            + "<D:lockscope><D:exclusive/></D:lockscope>"
            + "<D:depth>0</D:depth>"
            + "<D:locktoken><D:href>" + token + "</D:href></D:locktoken>"
            + "</D:activelock></D:lockdiscovery></D:prop>";
        response.setContentType("application/xml; charset=utf-8");
        response.setStatus(200);
        response.setHeader("Lock-Token", token);
        response.getWriter().write(xml);
    }

    // ──────────────────────────────────────────────
    // Helpers
    // ──────────────────────────────────────────────

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

    private String extractPathSegment(String path, String prefix) {
        if (!path.startsWith(prefix)) return null;
        String rest = path.substring(prefix.length());
        int slash = rest.indexOf('/');
        return slash >= 0 ? rest.substring(0, slash) : rest;
    }

    private String extractXmlValue(String xml, String tag) {
        Pattern p = Pattern.compile(
            "<([^:>]*:)?\\Q" + tag + "\\E[^>]*>(.*?)</([^:>]*:)?\\Q" + tag + "\\E>",
            Pattern.DOTALL);
        Matcher m = p.matcher(xml);
        return m.find() ? m.group(2).trim() : null;
    }

    private String extractCalendarName(String body) {
        String name = extractXmlValue(body, "displayname");
        if (name != null && !name.isEmpty()) return name;
        return null;
    }

    private String extractCalendarColor(String body) {
        return extractXmlValue(body, "calendar-color");
    }

    private String extractCalendarDesc(String body) {
        return extractXmlValue(body, "calendar-description");
    }

    private String extractValue(String data, String key) {
        Pattern p = Pattern.compile("(?m)^" + key + "(?:;[^:]*)?:(.+?)(\\r?\\n|$)");
        Matcher m = p.matcher(data);
        return m.find() ? m.group(1).trim() : null;
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
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;")
                .replace(">", "&gt;").replace("\"", "&quot;");
    }

    private String formatHttpDate(LocalDateTime dt) {
        return ZonedDateTime.of(dt, ZoneId.systemDefault())
            .withZoneSameInstant(ZoneId.of("GMT"))
            .format(HTTP_DATE_FMT);
    }

    private boolean isNumeric(String s) {
        if (s == null || s.isEmpty()) return false;
        for (int i = 0; i < s.length(); i++) {
            if (!Character.isDigit(s.charAt(i))) return false;
        }
        return true;
    }

    private CalendarEvent resolveEventFromPath(String path, User user) {
        Matcher m = Pattern.compile("/calendars/(\\d+)/([^/]+)\\.ics").matcher(path);
        if (!m.find()) return null;
        Long calId = Long.parseLong(m.group(1));
        String filename = m.group(2);
        var opt = findEventByFilename(calId, filename);
        if (opt.isPresent() && opt.get().getCalendar().getUser().getId().equals(user.getId())) {
            return opt.get();
        }
        return null;
    }

    private String ctag(User user) {
        var digest = sha256(user.getId() + "-" + System.currentTimeMillis());
        return "\"sf-ctag-" + digest.substring(0, 16) + "\"";
    }

    private String sha256(String input) {
        try {
            var md = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(md.digest(input.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            return input;
        }
    }

    // ──────────────────────────────────────────────
    // iCalendar parsing / rebuilding
    // ──────────────────────────────────────────────

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
        String uid = extractValue(icalData, "UID");

        if (uid != null) event.setUid(uid.replace("@scalefish", ""));
        if (title != null) event.setTitle(title.replace("\\,", ",").replace("\\n", "\n").replace("\\\\", "\\"));
        if (desc != null) event.setDescription(desc.replace("\\n", "\n").replace("\\\\", "\\"));
        if (loc != null) event.setLocation(loc.replace("\\,", ",").replace("\\\\", "\\"));
        if (dtstart != null) {
            try {
                boolean allDay = dtstart.contains("VALUE=DATE") || !dtstart.contains("T");
                event.setAllDay(allDay);
                event.setStartTime(parseIcalDt(dtstart));
            } catch (Exception ignored) {}
        }
        if (dtend != null) {
            try {
                event.setEndTime(parseIcalDt(dtend));
            } catch (Exception ignored) {}
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

    private LocalDateTime parseIcalDt(String value) {
        String v = value.replace("Z", "");
        int tIdx = v.indexOf('T');
        if (tIdx >= 0) {
            String datePart = v.substring(0, tIdx).replaceAll("[^0-9]", "");
            String timePart = v.substring(tIdx + 1).replaceAll("[^0-9]", "");
            if (timePart.length() >= 6) {
                return LocalDateTime.parse(datePart + "T" + timePart.substring(0, 6),
                    DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss"));
            }
            return LocalDateTime.parse(datePart + "T000000",
                DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss"));
        }
        java.time.LocalDate ld = java.time.LocalDate.parse(v.replaceAll("[^0-9]", ""),
            DateTimeFormatter.ofPattern("yyyyMMdd"));
        return ld.atStartOfDay();
    }

    private String rebuildIcal(CalendarEvent event) {
        var sb = new StringBuilder();
        sb.append("BEGIN:VCALENDAR\r\n");
        sb.append("VERSION:2.0\r\n");
        sb.append("PRODID:-//Scalefish//Calendar//EN\r\n");
        sb.append("CALSCALE:GREGORIAN\r\n");
        sb.append("BEGIN:VEVENT\r\n");
        String eventUid = event.getUid() != null ? event.getUid() : String.valueOf(event.getId());
        sb.append("UID:").append(eventUid).append("@scalefish\r\n");
        if (event.isAllDay()) {
            sb.append("DTSTART;VALUE=DATE:").append(event.getStartTime().format(ICAL_DATE_FMT)).append("\r\n");
            sb.append("DTEND;VALUE=DATE:").append(event.getEndTime().format(ICAL_DATE_FMT)).append("\r\n");
        } else {
            sb.append("DTSTART:").append(event.getStartTime().format(ICAL_DT_FMT)).append("\r\n");
            sb.append("DTEND:").append(event.getEndTime().format(ICAL_DT_FMT)).append("\r\n");
        }
        sb.append("SUMMARY:").append(escapeIcalText(event.getTitle())).append("\r\n");
        if (event.getDescription() != null)
            sb.append("DESCRIPTION:").append(escapeIcalText(event.getDescription())).append("\r\n");
        if (event.getLocation() != null)
            sb.append("LOCATION:").append(escapeIcalText(event.getLocation())).append("\r\n");
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
        sb.append("DTSTAMP:").append(LocalDateTime.now().format(ICAL_DT_FMT)).append("\r\n");
        sb.append("END:VEVENT\r\n");
        sb.append("END:VCALENDAR\r\n");
        return sb.toString();
    }

    private String escapeIcalText(String s) {
        return s.replace("\\", "\\\\").replace(";", "\\;").replace(",", "\\,")
                .replace("\r\n", "\\n").replace("\n", "\\n");
    }

    // ──────────────────────────────────────────────
    // vCard parsing / rebuilding
    // ──────────────────────────────────────────────

    private void parseVcardIntoContact(String vcardData, Contact contact) {
        String normalized = vcardData.replace("\r\n", "\n");
        String fn = extractValue(normalized, "FN");
        String email = extractValue(normalized, "EMAIL");
        String tel = extractValue(normalized, "TEL");
        String org = extractValue(normalized, "ORG");
        String note = extractValue(normalized, "NOTE");
        if (fn != null) contact.setName(fn);
        if (email != null) contact.setEmail(email);
        if (tel != null) contact.setPhone(tel);
        if (org != null) contact.setOrganization(org);
        if (note != null) contact.setNotes(note.replace("\\n", "\n"));
    }

    private String rebuildVcard(Contact contact) {
        var sb = new StringBuilder();
        sb.append("BEGIN:VCARD\r\n");
        sb.append("VERSION:3.0\r\n");
        sb.append("FN:").append(contact.getName()).append("\r\n");
        sb.append("N:").append(contact.getName()).append(";;;\r\n");
        if (contact.getEmail() != null)
            sb.append("EMAIL;TYPE=INTERNET:").append(contact.getEmail()).append("\r\n");
        if (contact.getPhone() != null)
            sb.append("TEL:").append(contact.getPhone()).append("\r\n");
        if (contact.getOrganization() != null)
            sb.append("ORG:").append(contact.getOrganization()).append("\r\n");
        if (contact.getNotes() != null)
            sb.append("NOTE:").append(contact.getNotes()).append("\r\n");
        sb.append("END:VCARD\r\n");
        return sb.toString();
    }
}
