package com.jinelei.scalefish.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "calendar_events")
@Getter
@Setter
@NoArgsConstructor
public class CalendarEvent extends BaseEntity {

    @Column(nullable = false, length = 200)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "event_location", length = 255)
    private String location;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Column(name = "is_all_day")
    private boolean allDay;

    @Column(columnDefinition = "TEXT")
    private String rrule;

    @Column(name = "event_status", length = 20, nullable = false)
    private String status = "CONFIRMED";

    @Column(name = "event_priority", nullable = false)
    private int priority;

    @Column(columnDefinition = "TEXT")
    private String categories;

    @Column(name = "event_url", length = 500)
    private String url;

    @Column(name = "event_sequence", nullable = false)
    private int sequence;

    @Column(length = 255)
    private String uid;

    @Column(name = "ical_data", columnDefinition = "TEXT")
    private String icalData;

    @ManyToOne
    @JoinColumn(name = "calendar_id", nullable = false)
    private CalendarEntity calendar;
}
