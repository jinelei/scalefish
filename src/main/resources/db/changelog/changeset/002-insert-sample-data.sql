--liquibase formatted sql

--changeset jinelei:002-insert-sample-data
INSERT INTO users (name, email) VALUES ('Alice', 'alice@example.com');
INSERT INTO users (name, email) VALUES ('Bob', 'bob@example.com');
