--liquibase formatted sql

--changeset jinelei:021-init-default-address-book
--comment: Initialize a default address book named "私人通讯录" for all users
INSERT INTO address_books (id, name, description, user_id, created_at, updated_at)
SELECT
    u.id + 100000000000000,
    '私人通讯录',
    '',
    u.id,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
FROM users u
WHERE NOT EXISTS (
    SELECT 1 FROM address_books ab WHERE ab.user_id = u.id
);
