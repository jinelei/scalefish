--liquibase formatted sql

--changeset jinelei:009-insert-sample-bookmarks
INSERT INTO bookmarks (title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES ('Spring Boot 官方文档', 'https://spring.io/projects/spring-boot', 'Spring Boot 官方参考文档', 'https://spring.io/favicon.ico', TRUE, 10, 3, NOW(), NOW());

INSERT INTO bookmarks (title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES ('Spring 官方指南', 'https://spring.io/guides', 'Spring 系列教程', 'https://spring.io/favicon.ico', FALSE, 5, 3, NOW(), NOW());

INSERT INTO bookmarks (title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES ('MDN Web 文档', 'https://developer.mozilla.org/zh-CN/', 'HTML、CSS、JavaScript 权威参考', 'https://developer.mozilla.org/favicon.ico', FALSE, 8, 2, NOW(), NOW());

INSERT INTO bookmarks (title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES ('React 官方文档', 'https://react.dev/', 'React 前端框架官方文档', 'https://react.dev/favicon.ico', TRUE, 7, 2, NOW(), NOW());

INSERT INTO bookmarks (title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES ('GitHub', 'https://github.com', '代码托管与协作平台', 'https://github.com/favicon.ico', TRUE, 20, 4, NOW(), NOW());

INSERT INTO bookmarks (title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES ('Stack Overflow', 'https://stackoverflow.com', '开发者问答社区', 'https://stackoverflow.com/favicon.ico', FALSE, 15, 4, NOW(), NOW());

INSERT INTO bookmarks (title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES ('阮一峰的网络日志', 'https://www.ruanyifeng.com/blog/', '科技、互联网、编程相关博客', NULL, FALSE, 12, 5, NOW(), NOW());

INSERT INTO bookmarks (title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES ('阿里巴巴 Java 开发手册', 'https://github.com/alibaba/p3c', '阿里巴巴 Java 编码规范', 'https://github.com/favicon.ico', FALSE, 3, 3, NOW(), NOW());

INSERT INTO bookmarks (title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES ('Vue.js 官方文档', 'https://vuejs.org/', 'Vue.js 渐进式 JavaScript 框架', 'https://vuejs.org/favicon.ico', FALSE, 6, 2, NOW(), NOW());

INSERT INTO bookmarks (title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES ('Docker 官方文档', 'https://docs.docker.com/', 'Docker 容器化技术文档', 'https://docs.docker.com/favicon.ico', FALSE, 4, 4, NOW(), NOW());

-- bookmark_tags associations
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES ((SELECT id FROM bookmarks WHERE title = 'Spring Boot 官方文档'), (SELECT id FROM tags WHERE name = 'java'));
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES ((SELECT id FROM bookmarks WHERE title = 'Spring Boot 官方文档'), (SELECT id FROM tags WHERE name = 'spring'));
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES ((SELECT id FROM bookmarks WHERE title = 'Spring Boot 官方文档'), (SELECT id FROM tags WHERE name = 'database'));
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES ((SELECT id FROM bookmarks WHERE title = 'Spring 官方指南'), (SELECT id FROM tags WHERE name = 'spring'));
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES ((SELECT id FROM bookmarks WHERE title = 'Spring 官方指南'), (SELECT id FROM tags WHERE name = 'java'));
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES ((SELECT id FROM bookmarks WHERE title = 'MDN Web 文档'), (SELECT id FROM tags WHERE name = 'javascript'));
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES ((SELECT id FROM bookmarks WHERE title = 'React 官方文档'), (SELECT id FROM tags WHERE name = 'javascript'));
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES ((SELECT id FROM bookmarks WHERE title = 'GitHub'), (SELECT id FROM tags WHERE name = 'devops'));
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES ((SELECT id FROM bookmarks WHERE title = 'Stack Overflow'), (SELECT id FROM tags WHERE name = 'java'));
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES ((SELECT id FROM bookmarks WHERE title = 'Stack Overflow'), (SELECT id FROM tags WHERE name = 'javascript'));
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES ((SELECT id FROM bookmarks WHERE title = 'Stack Overflow'), (SELECT id FROM tags WHERE name = 'devops'));
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES ((SELECT id FROM bookmarks WHERE title = '阿里巴巴 Java 开发手册'), (SELECT id FROM tags WHERE name = 'java'));
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES ((SELECT id FROM bookmarks WHERE title = 'Vue.js 官方文档'), (SELECT id FROM tags WHERE name = 'javascript'));
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES ((SELECT id FROM bookmarks WHERE title = 'Docker 官方文档'), (SELECT id FROM tags WHERE name = 'devops'));
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES ((SELECT id FROM bookmarks WHERE title = 'Docker 官方文档'), (SELECT id FROM tags WHERE name = 'database'));
