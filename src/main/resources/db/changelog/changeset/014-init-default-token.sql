--liquibase formatted sql

--changeset jinelei:014-init-default-token
--comment: Initialize default admin user and API token

INSERT INTO users (id, username, password, name, email, role, created_at, updated_at)
VALUES (1, 'jinelei', '$2b$12$6qI5Pt3Apfg1RJ9mbGpDzu/JgnCw9GvEUhObfGwFXlc197MtS0Tbq', 'Administrator', 'jinelei@163.com', 'ROLE_ADMIN', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO api_tokens (id, user_id, name, token_hash, token_prefix, expires_at, created_at, updated_at)
VALUES (2, 1, 'Default Admin Token', 'f1ec5f6a86feadc7cd4eba26e8ff28babdda69ad98a55eef8ccb085fd1af19bc', 'sf_MP0RCstGE', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
