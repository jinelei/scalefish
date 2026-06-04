--liquibase formatted sql

--changeset jinelei:006-create-bookmark-tags-table
CREATE TABLE bookmark_tags (
    bookmark_id BIGINT NOT NULL,
    tag_id BIGINT NOT NULL,
    PRIMARY KEY (bookmark_id, tag_id)
);

ALTER TABLE bookmark_tags ADD CONSTRAINT fk_bt_bookmark FOREIGN KEY (bookmark_id) REFERENCES bookmarks(id);
ALTER TABLE bookmark_tags ADD CONSTRAINT fk_bt_tag FOREIGN KEY (tag_id) REFERENCES tags(id);
