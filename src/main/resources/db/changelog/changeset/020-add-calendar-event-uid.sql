--liquibase formatted sql

--changeset jinelei:020-add-calendar-event-uid
ALTER TABLE calendar_events ADD COLUMN uid VARCHAR(255);
CREATE INDEX idx_calendar_events_uid ON calendar_events(uid);
