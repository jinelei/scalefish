package com.jinelei.scalefish.dto;

import com.jinelei.scalefish.entity.CalendarEntity;

import java.time.LocalDateTime;

public record CalendarResponse(
    Long id,
    String name,
    String description,
    String displayColor,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    public static CalendarResponse from(CalendarEntity c) {
        return new CalendarResponse(c.getId(), c.getName(), c.getDescription(),
            c.getDisplayColor(), c.getCreatedAt(), c.getUpdatedAt());
    }
}
