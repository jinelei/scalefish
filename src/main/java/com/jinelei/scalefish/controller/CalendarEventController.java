package com.jinelei.scalefish.controller;

import com.jinelei.scalefish.dto.CalendarEventRequest;
import com.jinelei.scalefish.dto.CalendarEventResponse;
import com.jinelei.scalefish.dto.GenericResult;
import com.jinelei.scalefish.entity.User;
import com.jinelei.scalefish.service.CalendarEventService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@RestController
@RequestMapping("/api/events")
public class CalendarEventController {

    private final CalendarEventService eventService;

    public CalendarEventController(CalendarEventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public GenericResult<List<CalendarEventResponse>> list(
            @AuthenticationPrincipal User user,
            @RequestParam(required = false) Long calendarId,
            @RequestParam(required = false) String start,
            @RequestParam(required = false) String end) {
        if (calendarId != null) {
            return GenericResult.success(eventService.listByCalendar(user, calendarId));
        }
        if (start != null && end != null) {
            LocalDateTime startDt = parseIso(start);
            LocalDateTime endDt = parseIso(end);
            return GenericResult.success(eventService.listByRange(user, startDt, endDt));
        }
        return GenericResult.success(List.of());
    }

    private LocalDateTime parseIso(String value) {
        if (value == null) return null;
        try {
            return LocalDateTime.parse(value);
        } catch (Exception e) {
            try {
                Instant instant = Instant.parse(value);
                return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
            } catch (Exception e2) {
                throw new RuntimeException("Cannot parse date: " + value, e2);
            }
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GenericResult<CalendarEventResponse> create(@AuthenticationPrincipal User user,
                                                        @RequestParam Long calendarId,
                                                        @Valid @RequestBody CalendarEventRequest request) {
        return GenericResult.success(eventService.create(user, calendarId, request));
    }

    @PutMapping("/{id}")
    public GenericResult<CalendarEventResponse> update(@AuthenticationPrincipal User user,
                                                        @PathVariable Long id,
                                                        @Valid @RequestBody CalendarEventRequest request) {
        return GenericResult.success(eventService.update(user, id, request));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal User user, @PathVariable Long id) {
        eventService.delete(user, id);
    }
}
