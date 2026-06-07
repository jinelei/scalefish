--liquibase formatted sql

--changeset jinelei:016-create-calendar-events-table
CREATE TABLE calendar_events (
    id BIGINT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    event_location VARCHAR(255),
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    is_all_day BOOLEAN DEFAULT FALSE,
    rrule TEXT,
    ical_data TEXT,
    calendar_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (calendar_id) REFERENCES calendars(id) ON DELETE CASCADE
);

CREATE INDEX idx_calendar_events_calendar ON calendar_events(calendar_id);
CREATE INDEX idx_calendar_events_start ON calendar_events(start_time);
