package com.jinelei.scalefish.service;

import com.jinelei.scalefish.dto.CalendarEventRequest;
import com.jinelei.scalefish.dto.CalendarEventResponse;
import com.jinelei.scalefish.entity.CalendarEvent;
import com.jinelei.scalefish.entity.User;
import com.jinelei.scalefish.exception.BusinessException;
import com.jinelei.scalefish.exception.ErrorCode;
import com.jinelei.scalefish.exception.ResourceNotFoundException;
import com.jinelei.scalefish.repository.CalendarEventRepository;
import com.jinelei.scalefish.repository.CalendarRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CalendarEventService {

    private static final Logger log = LoggerFactory.getLogger(CalendarEventService.class);

    private final CalendarEventRepository eventRepository;
    private final CalendarRepository calendarRepository;
    private final CalendarService calendarService;

    public CalendarEventService(CalendarEventRepository eventRepository,
                                 CalendarRepository calendarRepository,
                                 CalendarService calendarService) {
        this.eventRepository = eventRepository;
        this.calendarRepository = calendarRepository;
        this.calendarService = calendarService;
    }

    public List<CalendarEventResponse> listByCalendar(User user, Long calendarId) {
        log.debug("List events for calendar: {}, userId={}", calendarId, user.getId());
        calendarService.getEntity(user, calendarId);
        return eventRepository.findByCalendarIdOrderByStartTimeAsc(calendarId).stream()
            .map(CalendarEventResponse::from)
            .toList();
    }

    public List<CalendarEventResponse> listByRange(User user, LocalDateTime start, LocalDateTime end) {
        log.debug("List events by range: userId={}, start={}, end={}", user.getId(), start, end);
        return eventRepository.findByCalendarUserIdAndStartTimeBetweenOrderByStartTimeAsc(user.getId(), start, end)
            .stream()
            .map(CalendarEventResponse::from)
            .toList();
    }

    public CalendarEventResponse create(User user, Long calendarId, CalendarEventRequest request) {
        log.info("Create event: title={}, calendarId={}, userId={}", request.title(), calendarId, user.getId());
        var cal = calendarService.getEntity(user, calendarId);
        var event = new CalendarEvent();
        event.setTitle(request.title());
        event.setDescription(request.description());
        event.setLocation(request.location());
        event.setStartTime(request.startTime());
        event.setEndTime(request.endTime());
        event.setAllDay(request.allDay());
        event.setRrule(request.rrule());
        event.setStatus(request.status() != null ? request.status() : "CONFIRMED");
        event.setPriority(request.priority() != null ? request.priority() : 0);
        event.setCategories(request.categories());
        event.setUrl(request.url());
        event.setSequence(0);
        event.setCalendar(cal);
        event.setIcalData(buildIcal(event));
        eventRepository.save(event);
        log.info("Event created: id={}, title={}", event.getId(), event.getTitle());
        return CalendarEventResponse.from(event);
    }

    public CalendarEventResponse update(User user, Long eventId, CalendarEventRequest request) {
        log.info("Update event: id={}, userId={}", eventId, user.getId());
        var event = eventRepository.findById(eventId)
            .orElseThrow(() -> new ResourceNotFoundException("CalendarEvent", eventId));
        if (!event.getCalendar().getUser().getId().equals(user.getId())) {
            log.warn("Event update forbidden: id={}, userId={}", eventId, user.getId());
            throw new BusinessException(ErrorCode.FORBIDDEN, "Event does not belong to the current user");
        }
        event.setTitle(request.title());
        event.setDescription(request.description());
        event.setLocation(request.location());
        event.setStartTime(request.startTime());
        event.setEndTime(request.endTime());
        event.setAllDay(request.allDay());
        event.setRrule(request.rrule());
        event.setStatus(request.status() != null ? request.status() : "CONFIRMED");
        event.setPriority(request.priority() != null ? request.priority() : 0);
        event.setCategories(request.categories());
        event.setUrl(request.url());
        event.setSequence(event.getSequence() + 1);
        event.setIcalData(buildIcal(event));
        eventRepository.save(event);
        log.info("Event updated: id={}", event.getId());
        return CalendarEventResponse.from(event);
    }

    public void delete(User user, Long eventId) {
        log.info("Delete event: id={}, userId={}", eventId, user.getId());
        var event = eventRepository.findById(eventId)
            .orElseThrow(() -> new ResourceNotFoundException("CalendarEvent", eventId));
        if (!event.getCalendar().getUser().getId().equals(user.getId())) {
            log.warn("Event delete forbidden: id={}, userId={}", eventId, user.getId());
            throw new BusinessException(ErrorCode.FORBIDDEN, "Event does not belong to the current user");
        }
        eventRepository.delete(event);
        log.info("Event deleted: id={}", eventId);
    }

    private String buildIcal(CalendarEvent event) {
        var sb = new StringBuilder();
        sb.append("BEGIN:VCALENDAR\r\n");
        sb.append("VERSION:2.0\r\n");
        sb.append("PRODID:-//Scalefish//Calendar//EN\r\n");
        sb.append("CALSCALE:GREGORIAN\r\n");
        sb.append("BEGIN:VEVENT\r\n");
        sb.append("UID:").append(event.getId()).append("@scalefish\r\n");
        sb.append("DTSTART:").append(formatIcalDate(event.getStartTime(), event.isAllDay())).append("\r\n");
        sb.append("DTEND:").append(formatIcalDate(event.getEndTime(), event.isAllDay())).append("\r\n");
        sb.append("SUMMARY:").append(escapeIcal(event.getTitle())).append("\r\n");
        if (event.getDescription() != null) {
            sb.append("DESCRIPTION:").append(escapeIcal(event.getDescription())).append("\r\n");
        }
        if (event.getLocation() != null) {
            sb.append("LOCATION:").append(escapeIcal(event.getLocation())).append("\r\n");
        }
        if (event.getRrule() != null) {
            sb.append("RRULE:").append(event.getRrule()).append("\r\n");
        }
        if (event.getStatus() != null) {
            sb.append("STATUS:").append(event.getStatus()).append("\r\n");
        }
        if (event.getPriority() > 0) {
            sb.append("PRIORITY:").append(event.getPriority()).append("\r\n");
        }
        if (event.getCategories() != null) {
            sb.append("CATEGORIES:").append(event.getCategories()).append("\r\n");
        }
        if (event.getUrl() != null) {
            sb.append("URL:").append(event.getUrl()).append("\r\n");
        }
        sb.append("SEQUENCE:").append(event.getSequence()).append("\r\n");
        sb.append("DTSTAMP:").append(formatIcalDate(LocalDateTime.now(), false)).append("\r\n");
        sb.append("END:VEVENT\r\n");
        sb.append("END:VCALENDAR\r\n");
        return sb.toString();
    }

    private String formatIcalDate(LocalDateTime dt, boolean allDay) {
        if (allDay) {
            return dt.toLocalDate().toString().replace("-", "") + "T" + dt.toLocalTime().toString().replace(":", "");
        }
        return dt.toString().replace("-", "").replace(":", "").replace("T", "T");
    }

    private String escapeIcal(String s) {
        return s.replace("\\", "\\\\").replace(";", "\\;").replace(",", "\\,")
                .replace("\r\n", "\\n").replace("\n", "\\n");
    }

    public CalendarEvent getEntity(User user, Long eventId) {
        log.debug("Get event entity: id={}, userId={}", eventId, user.getId());
        var event = eventRepository.findById(eventId)
            .orElseThrow(() -> new ResourceNotFoundException("CalendarEvent", eventId));
        if (!event.getCalendar().getUser().getId().equals(user.getId())) {
            log.warn("Event getEntity forbidden: id={}, userId={}", eventId, user.getId());
            throw new BusinessException(ErrorCode.FORBIDDEN, "Event does not belong to the current user");
        }
        return event;
    }
}
