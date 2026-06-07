--liquibase formatted sql

--changeset jinelei:014-init-default-token
--comment: Superceded by DataInitializer (config-based init from app.admin.*)
--validCheckSum: any
SELECT 1 WHERE 1 = 1;
