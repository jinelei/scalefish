--liquibase formatted sql

--changeset jinelei:004-create-tags-table
CREATE TABLE tags (
    id BIGINT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);
