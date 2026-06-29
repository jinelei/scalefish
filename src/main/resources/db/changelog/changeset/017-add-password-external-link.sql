--liquibase formatted sql

--changeset jinelei:017-add-password-external-link
INSERT INTO external_links (id, name, url, icon, sort_order, created_at, updated_at)
VALUES (92074800000009, '密码', 'https://vault.jinelei.com:9443/', 'FiLock', 9, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
