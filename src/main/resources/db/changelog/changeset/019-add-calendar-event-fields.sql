--liquibase formatted sql

--changeset jinelei:019-add-calendar-event-fields
ALTER TABLE calendar_events ADD COLUMN event_status VARCHAR(20) NOT NULL DEFAULT 'CONFIRMED';
ALTER TABLE calendar_events ADD COLUMN event_priority INT NOT NULL DEFAULT 0;
ALTER TABLE calendar_events ADD COLUMN categories TEXT;
ALTER TABLE calendar_events ADD COLUMN event_url VARCHAR(500);
ALTER TABLE calendar_events ADD COLUMN event_sequence INT NOT NULL DEFAULT 0;
