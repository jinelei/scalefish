--liquibase formatted sql

--changeset jinelei:005-create-bookmarks-table
CREATE TABLE bookmarks (
    id BIGINT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    url VARCHAR(2048) NOT NULL,
    description TEXT,
    favicon_url VARCHAR(2048),
    is_pinned BOOLEAN DEFAULT FALSE,
    click_count INT DEFAULT 0,
    category_id BIGINT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE bookmarks ADD CONSTRAINT fk_bookmarks_category FOREIGN KEY (category_id) REFERENCES categories(id);
