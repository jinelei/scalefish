--liquibase formatted sql

--changeset jinelei:007-insert-sample-categories
INSERT INTO categories (name, parent_id, sort_order, created_at) VALUES ('技术', NULL, 1, NOW());
INSERT INTO categories (name, parent_id, sort_order, created_at) VALUES ('前端', 1, 1, NOW());
INSERT INTO categories (name, parent_id, sort_order, created_at) VALUES ('后端', 1, 2, NOW());
INSERT INTO categories (name, parent_id, sort_order, created_at) VALUES ('工具', NULL, 2, NOW());
INSERT INTO categories (name, parent_id, sort_order, created_at) VALUES ('阅读', NULL, 3, NOW());
