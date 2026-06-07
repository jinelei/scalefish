--liquibase formatted sql

--changeset jinelei:015-create-calendars-table
CREATE TABLE calendars (
    id BIGINT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    display_color VARCHAR(7),
    user_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);
