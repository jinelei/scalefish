package com.jinelei.scalefish.dto;

import com.jinelei.scalefish.entity.CalendarEvent;

import java.time.LocalDateTime;

public record CalendarEventResponse(
    Long id,
    Long calendarId,
    String title,
    String description,
    String location,
    LocalDateTime startTime,
    LocalDateTime endTime,
    boolean allDay,
    String rrule,
    String status,
    int priority,
    String categories,
    String url,
    int sequence,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    public static CalendarEventResponse from(CalendarEvent e) {
        return new CalendarEventResponse(e.getId(), e.getCalendar().getId(),
            e.getTitle(), e.getDescription(), e.getLocation(),
            e.getStartTime(), e.getEndTime(), e.isAllDay(),
            e.getRrule(), e.getStatus(), e.getPriority(),
            e.getCategories(), e.getUrl(), e.getSequence(),
            e.getCreatedAt(), e.getUpdatedAt());
    }
}
