--liquibase formatted sql

--changeset jinelei:008-insert-sample-tags
INSERT INTO tags (name, created_at) VALUES ('java', NOW());
INSERT INTO tags (name, created_at) VALUES ('spring', NOW());
INSERT INTO tags (name, created_at) VALUES ('javascript', NOW());
INSERT INTO tags (name, created_at) VALUES ('database', NOW());
INSERT INTO tags (name, created_at) VALUES ('devops', NOW());
