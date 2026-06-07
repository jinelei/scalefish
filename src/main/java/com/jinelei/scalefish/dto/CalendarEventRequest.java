package com.jinelei.scalefish.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CalendarEventRequest(
    @NotBlank String title,
    String description,
    String location,
    @NotNull LocalDateTime startTime,
    @NotNull LocalDateTime endTime,
    boolean allDay,
    String rrule,
    String status,
    Integer priority,
    String categories,
    String url
) {}
