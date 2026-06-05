--liquibase formatted sql

--changeset jinelei:012-add-role-column
ALTER TABLE users ADD COLUMN role VARCHAR(50);
