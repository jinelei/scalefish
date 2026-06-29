--liquibase formatted sql

--changeset jinelei:016-create-external-links-table
CREATE TABLE external_links (
    id BIGINT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    url VARCHAR(2048) NOT NULL,
    icon VARCHAR(50),
    sort_order INT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO external_links (id, name, url, icon, sort_order, created_at, updated_at)
VALUES (92074800000001, '音乐', 'https://music.jinelei.com:9443/', 'FiMusic', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO external_links (id, name, url, icon, sort_order, created_at, updated_at)
VALUES (92074800000002, '照片', 'https://photo.jinelei.com:9443/photos', 'FiCamera', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO external_links (id, name, url, icon, sort_order, created_at, updated_at)
VALUES (92074800000003, '日历', 'https://calendar.jinelei.com:9443/', 'FiCalendar', 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO external_links (id, name, url, icon, sort_order, created_at, updated_at)
VALUES (92074800000004, '文件', 'https://file.jinelei.com:9443/files/', 'FiFolder', 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO external_links (id, name, url, icon, sort_order, created_at, updated_at)
VALUES (92074800000005, '代理', 'https://clash.jinelei.com:9443/ui/#/overview', 'FiShield', 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO external_links (id, name, url, icon, sort_order, created_at, updated_at)
VALUES (92074800000006, 'VPN', 'https://wire.jinelei.com:9443/#/', 'FiLock', 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO external_links (id, name, url, icon, sort_order, created_at, updated_at)
VALUES (92074800000007, 'DDNS', 'https://ddns.jinelei.com:9443/', 'FiGlobe', 7, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO external_links (id, name, url, icon, sort_order, created_at, updated_at)
VALUES (92074800000008, '代码', 'https://code.jinelei.com:9443/', 'FiCode', 8, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
