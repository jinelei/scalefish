--liquibase formatted sql

--changeset jinelei:010-import-current-data
--comment: Import data snapshot from 2026-06-05T16:28:09.878763

INSERT INTO categories (id, name, parent_id, sort_order, created_at, updated_at)
VALUES (92074742051872, '工作', NULL, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO categories (id, name, parent_id, sort_order, created_at, updated_at)
VALUES (92074742168608, '我的', NULL, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO categories (id, name, parent_id, sort_order, created_at, updated_at)
VALUES (92074742256672, '个人', NULL, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO tags (id, name, created_at, updated_at)
VALUES (92074737130528, '原型', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO tags (id, name, created_at, updated_at)
VALUES (92074737298464, 'PFMEA', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO tags (id, name, created_at, updated_at)
VALUES (92074737458208, '开发工作', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO tags (id, name, created_at, updated_at)
VALUES (92074737609760, '需求', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO tags (id, name, created_at, updated_at)
VALUES (92074737710112, '产品特性优化0515', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO tags (id, name, created_at, updated_at)
VALUES (92074737849376, 'UI', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO tags (id, name, created_at, updated_at)
VALUES (92074737943584, '市场TOP问题', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO tags (id, name, created_at, updated_at)
VALUES (92074738062368, '市场问题迭代', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO tags (id, name, created_at, updated_at)
VALUES (92074738236448, '研发质量应规避看板', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO tags (id, name, created_at, updated_at)
VALUES (92074738500640, '日常工作', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO tags (id, name, created_at, updated_at)
VALUES (92074738590752, '过程特性', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO tags (id, name, created_at, updated_at)
VALUES (92074738678816, '任务排期', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO tags (id, name, created_at, updated_at)
VALUES (92074738795552, '产品特性', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO tags (id, name, created_at, updated_at)
VALUES (92074738897952, '测试用例', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO tags (id, name, created_at, updated_at)
VALUES (92074739006496, '市场责任异动', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO tags (id, name, created_at, updated_at)
VALUES (92074739137568, '项目应规避', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO tags (id, name, created_at, updated_at)
VALUES (92074739293216, '专项规避', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO tags (id, name, created_at, updated_at)
VALUES (92074739452960, '运维工具', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO tags (id, name, created_at, updated_at)
VALUES (92074739563552, '日志', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO tags (id, name, created_at, updated_at)
VALUES (92074739702816, '市场周报日报', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO tags (id, name, created_at, updated_at)
VALUES (92074739883040, '版本库（产品特性）', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO tags (id, name, created_at, updated_at)
VALUES (92074739987488, '同步BOP/BOM', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO tags (id, name, created_at, updated_at)
VALUES (92074740104224, '市场问题横排', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO tags (id, name, created_at, updated_at)
VALUES (92074740270112, '应规避工作台', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO tags (id, name, created_at, updated_at)
VALUES (92074740438048, '技术方案', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO tags (id, name, created_at, updated_at)
VALUES (92074740597792, '产品特性优化0227', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO tags (id, name, created_at, updated_at)
VALUES (92074740769824, '车型车系', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO tags (id, name, created_at, updated_at)
VALUES (92074740900896, '上线', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO tags (id, name, created_at, updated_at)
VALUES (92074740978720, '零部件试验库', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO tags (id, name, created_at, updated_at)
VALUES (92074741083168, '文档', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO tags (id, name, created_at, updated_at)
VALUES (92074741204000, '市场数据分析', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO tags (id, name, created_at, updated_at)
VALUES (92074741347360, '自建服务', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO tags (id, name, created_at, updated_at)
VALUES (92074741611552, '文档教程', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO tags (id, name, created_at, updated_at)
VALUES (92074741732384, '资源', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO tags (id, name, created_at, updated_at)
VALUES (92074741824544, '游戏', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO tags (id, name, created_at, updated_at)
VALUES (92074741937184, '开源', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074751157280, 'DQS - 版本库（产品特性） - 任务排期', 'https://sai-seres.feishu.cn/wiki/NYZBwoAkciDoibklFGUcnDfCnrw?table=tblBwTqwO3jxYPS0&view=vewvh8sGLt', 'v1.3.0版本（产品特性版本库、基线库）', NULL, 1, 0, 92074742051872, '2026-05-14 06:20:11.24', '2026-06-05 16:26:18.62393');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074750952480, '版本库（产品特性） - 技术方案', 'https://sai-seres.feishu.cn/docx/DAzldsGzqoCFgExmT9ccvffpnZe', '产品特性版本库迭代', NULL, 1, 0, 92074742051872, '2026-05-14 02:47:12.54', '2026-06-05 16:26:18.557052');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074756398112, 'DeepSeek - 探索未至之境', 'https://chat.deepseek.com/a/chat/', NULL, NULL, 1, 0, 92074742256672, '2026-05-14 02:47:12.474', '2026-06-05 16:26:18.491069');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074746989600, 'DQS - 版本库（产品特性） - 原型 20260513', 'https://erdp.seres.cn/objectstor01-prod/product-prototype/prod_env/2026-05-13-884e24980e044f52a29003db4981f4d6/index.html?id=706gm2&p=%E6%96%B0%E5%A2%9E%E7%89%B9%E6%80%A7%E5%88%9B%E5%BB%BA%E7%89%88%E6%9C%AC&g=1', NULL, NULL, 1, 0, 92074742051872, '2026-05-14 06:48:56.3', '2026-06-05 16:26:18.436941');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074753571872, '日历 - CalDavZAP', 'https://webdav.jinelei.com:9443/', NULL, NULL, 0, 0, 92074742168608, '2026-05-22 03:26:30.991', '2026-05-29 02:57:55.792');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074754567200, '网络 - WGDashboard', 'https://wire.jinelei.com:9443/#/', NULL, NULL, 0, 0, 92074742168608, '2026-05-14 02:47:12.634', '2026-05-29 02:57:42.766');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074754456608, '代码 - code-server', 'https://code.jinelei.com:9443/', NULL, NULL, 0, 0, 92074742168608, '2026-05-14 07:35:42.887', '2026-05-29 02:57:15.051');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074754290720, '密码 - Vaultwarden Web', 'https://vault.jinelei.com:9443/#/vault', NULL, NULL, 0, 0, 92074742168608, '2026-05-14 02:47:12.619', '2026-05-29 02:57:04.704');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074754202656, '代理 - mihomo ui', 'https://clash.jinelei.com:9443/ui/#/overview', 'Mihomo服务UI', NULL, 0, 0, 92074742168608, '2026-05-14 02:47:12.604', '2026-05-29 02:56:49.456');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074753838112, '书签 - Linkwarden', 'https://link.jinelei.com:9443/zh/collections/5', NULL, NULL, 0, 0, 92074742168608, '2026-05-14 07:25:19.373', '2026-05-29 02:56:35.296');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074754067488, '音乐 - Navidrome', 'https://music.jinelei.com:9443/', NULL, NULL, 0, 0, 92074742168608, '2026-05-14 07:36:54.603', '2026-05-29 02:56:23.076');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074753711136, '文件 - File Browser', 'https://file.jinelei.com:9443/files/', NULL, NULL, 0, 0, 92074742168608, '2026-05-25 10:03:05.62', '2026-05-29 02:56:00.916');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074753983520, '照片 - Immich', 'https://photo.jinelei.com:9443/photos', NULL, NULL, 0, 0, 92074742168608, '2026-05-14 07:37:10.971', '2026-05-29 02:55:42.26');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074754749472, 'DDNS-GO', 'https://ddns.jinelei.com:9443/', NULL, NULL, 0, 0, 92074742168608, '2026-05-14 07:36:24.212', '2026-05-29 02:55:42.182');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074754962464, '行政区域查询-基础 API 文档-开发指南-Web服务 API | 高德地图API', 'https://lbs.amap.com/api/webservice/guide/api/district', NULL, NULL, 0, 0, 92074742256672, '2026-05-14 02:47:11.838', '2026-05-29 02:55:04.628');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074755046432, '草料二维码', 'https://cli.im/', NULL, NULL, 0, 0, 92074742256672, '2026-05-14 02:47:11.974', '2026-05-29 02:55:04.628');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074755165216, 'Windows 11 LTSC 2024 官方原版ISO下载+激活方法 - 嗨软', 'https://ihacksoft.com/windows-11-ltsc-2024/', NULL, NULL, 0, 0, 92074742256672, '2026-05-14 02:47:11.86', '2026-05-29 02:55:04.628');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074755247136, '手柄测试(Gamepad Tester) - 在线检测手柄', 'https://www.9slab.com/gamepad/home', NULL, NULL, 0, 0, 92074742256672, '2026-05-14 02:47:11.715', '2026-05-29 02:55:04.628');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074755421216, 'Text Compare! - Find differences between two text files', 'https://text-compare.com/', '文本对比', NULL, 0, 0, 92074742256672, '2026-05-14 02:47:12.05', '2026-05-29 02:55:04.628');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074755529760, '皮卡丘的音乐站 - Pikachu Music', 'https://charlespikachu.github.io/musicsquare/', NULL, NULL, 0, 0, 92074742256672, '2026-05-25 09:16:23.253', '2026-05-29 02:55:04.628');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074755662880, 'fontawesome图标在线查询-BeJSON.com', 'https://www.bejson.com/ui/fontawesome/', '图标查询', NULL, 0, 0, 92074742256672, '2026-05-14 02:47:12.027', '2026-05-29 02:55:04.628');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074755826720, 'SWITCH导航小站 |', 'https://www.switch321.com/', NULL, NULL, 0, 0, 92074742256672, '2026-05-14 02:47:11.612', '2026-05-29 02:55:04.628');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074755910688, 'FC 中文游戏ROM全集(964个) - 知乎', 'https://zhuanlan.zhihu.com/p/386497318', NULL, NULL, 0, 0, 92074742256672, '2026-05-14 02:47:11.668', '2026-05-29 02:55:04.628');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074756043808, 'Yuzu Firmware 18.0.0 Download &amp; Installation | Latest Version', 'https://prodkeys.net/yuzu-firmware-1/', NULL, NULL, 0, 0, 92074742256672, '2026-05-14 02:47:11.692', '2026-05-29 02:55:04.628');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074756135968, 'feschber/lan-mouse: mouse &amp; keyboard sharing via LAN', 'https://github.com/feschber/lan-mouse', NULL, NULL, 0, 0, 92074742256672, '2026-05-14 02:47:12.214', '2026-05-29 02:55:04.628');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074756305952, '华为云 - 设备快速接入', 'https://support.huaweicloud.com/qs-iothub/iot_05_00120.html?ticket=ST-1979030-duwhaa5beUEumYMx4AoiGjvw-sso#ZH-CN_TOPIC_0000001085304367__section14253244192717', NULL, NULL, 0, 0, 92074742256672, '2026-05-14 02:47:11.736', '2026-05-29 02:55:04.628');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074756527136, '微信公众平台', 'https://mp.weixin.qq.com/wxopen/home?lang=zh_CN&token=858800857', NULL, NULL, 0, 0, 92074742256672, '2026-05-14 02:47:11.763', '2026-05-29 02:55:04.628');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074756725792, 'padavan', 'https://opt.cn2qq.com/padavan/', '路由器固件下载', NULL, 0, 0, 92074742256672, '2026-05-14 02:47:12.005', '2026-05-29 02:55:04.628');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074756832288, 'p3c/p3c-pmd at master · alibaba/p3c · GitHub', 'https://github.com/alibaba/p3c/tree/master/p3c-pmd', NULL, NULL, 0, 0, 92074742256672, '2026-05-14 02:47:12.24', '2026-05-29 02:55:04.628');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074757073952, 'alisen39/TrWebOCR: 开源易用的中文离线OCR，识别率媲美大厂，并且提供了易用的web页面及web的接口，方便人类日常工作使用或者其他程序来调用~', 'https://github.com/alisen39/TrWebOCR', NULL, NULL, 0, 0, 92074742256672, '2026-05-14 02:47:12.264', '2026-05-29 02:55:04.628');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074757262368, '纸由我 PaperMe - 自定义打印纸生成器', 'https://paperme.toolooz.com/', NULL, NULL, 0, 0, 92074742256672, '2026-05-14 02:47:12.072', '2026-05-29 02:55:04.628');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074757372960, '全部项目 - MAKE 趣无尽', 'https://make.quwj.com/projects?page=2', NULL, NULL, 0, 0, 92074742256672, '2026-05-14 02:47:12.094', '2026-05-29 02:55:04.628');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074757454880, 'Iconfont-阿里巴巴矢量图标库', 'https://www.iconfont.cn/collections/detail?cid=1192', NULL, NULL, 0, 0, 92074742256672, '2026-05-14 02:47:12.117', '2026-05-29 02:55:04.628');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074757604384, '图标 - Vector Icons and Stickers', 'https://www.flaticon.com/', NULL, NULL, 0, 0, 92074742256672, '2026-05-14 02:47:12.144', '2026-05-29 02:55:04.628');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074757862432, 'Maven Central: Publishing', 'https://central.sonatype.com/publishing/deployments', NULL, NULL, 0, 0, 92074742256672, '2026-05-14 02:47:12.171', '2026-05-29 02:55:04.628');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074757983264, 'MacWk - 精品mac软件下载', 'https://macwk.cn/', NULL, NULL, 0, 0, 92074742256672, '2026-05-14 02:47:12.192', '2026-05-29 02:55:04.628');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074758128672, '夸父资源社 | 优质夸克网盘资源分享社区论坛【高质量资源共享】', 'https://www.kfjwzz.com/', NULL, NULL, 0, 0, 92074742256672, '2026-05-14 02:47:12.369', '2026-05-29 02:55:04.628');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074758265888, 'Type Words - 词文记 | 单词跟打 · 文章跟打', 'https://typewords.cc/', NULL, NULL, 0, 0, 92074742256672, '2026-05-14 02:47:12.289', '2026-05-29 02:55:04.628');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074758349856, '和风天气', 'https://id.qweather.com/', NULL, NULL, 0, 0, 92074742256672, '2026-05-14 02:47:11.788', '2026-05-29 02:55:04.628');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074758458400, 'Handbook - Apache ECharts', 'https://echarts.apache.org/handbook/zh/concepts/axis#%E5%88%BB%E5%BA%A6%E6%A0%87%E7%AD%BE', NULL, NULL, 0, 0, 92074742256672, '2026-05-14 02:47:11.813', '2026-05-29 02:55:04.628');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074758538272, 'JSON校验格式化工具', 'http://www.bejson.com/', NULL, NULL, 0, 0, 92074742256672, '2026-05-14 02:47:11.883', '2026-05-29 02:55:04.628');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074758614048, 'UrlEncode编码/UrlDecode解码', 'https://tool.chinaz.com/tools/urlencode.aspx', NULL, NULL, 0, 0, 92074742256672, '2026-05-14 02:47:11.909', '2026-05-29 02:55:04.628');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074758691872, 'BT蚂蚁磁力 | 一个懂你的磁力搜索导航网站', 'https://8.210.218.9/', NULL, NULL, 0, 0, 92074742256672, '2026-05-14 02:47:12.337', '2026-05-29 02:55:04.628');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074758788128, '哔哩哔哩 (゜-゜)つロ 干杯~-bilibili', 'https://www.bilibili.com/?spm_id_from=333.1007.0.0', NULL, NULL, 0, 0, 92074742256672, '2026-05-14 02:47:12.314', '2026-05-29 02:55:04.628');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074758870048, 'Download music, movies, games, software! The Pirate Bay - The galaxy''s most resilient BitTorrent site', 'https://thpibay.site/', NULL, NULL, 0, 0, 92074742256672, '2026-05-14 02:47:12.433', '2026-05-29 02:55:04.628');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074742860832, 'DQS - TOP问题 - UI', 'https://erdp.seres.cn/erdp-file/product-prototype/prod_env/2025-10-24-598cd41aa45b491f9362d34ea2c13856/index.html', '市场TOP问题迭代', NULL, 0, 0, 92074742051872, '2026-05-14 02:47:10.594', '2026-05-28 01:34:55.6');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074742742048, 'DQS - 市场TOP问题 - 原型', 'https://erdp.seres.cn/erdp-file/product-prototype/prod_env/2025-09-18-73dbd556cbd04432bf6f97e22a1c272c/index.html?id=l8ahl6&p=top%E9%97%AE%E9%A2%98', '市场TOP问题迭代', NULL, 0, 0, 92074742051872, '2026-05-14 02:47:10.636', '2026-05-28 01:34:48.866');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074742957088, 'DQS - 市场问题迭代 - 需求', 'https://alidocs.dingtalk.com/document/preview?dentryKey=bL1EkVMkTNzxXblz&docKey=YvenvoPo24wVqoyZ&dontjump=true&type=d', '市场问题迭代', NULL, 0, 0, 92074742051872, '2026-05-14 02:47:10.675', '2026-05-28 01:34:44.071');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074750010400, 'DQS - 版本库（产品特性） - 需求', 'https://sai-seres.feishu.cn/docx/Nrd9dRupCoHkdYxp5Hnc4rySnvc', '产品特性版本库迭代', NULL, 0, 0, 92074742051872, '2026-05-14 02:47:10.906', '2026-05-28 01:34:36.47');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074751296544, 'DQS - 零部件试验库 -  原型', 'https://559nlx.axshare.com/?id=exmnjq&p=%E9%9B%B6%E9%83%A8%E4%BB%B6%E8%AF%95%E9%AA%8C%E5%BA%93%E5%8E%9F%E5%9E%8B%E5%9B%BE&g=1&sc=3', '零部件试验库迭代', NULL, 0, 0, 92074742051872, '2026-05-14 02:47:10.756', '2026-05-28 01:34:31.423');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074742621216, 'DQS - PFMEA一期 - 原型', 'https://f93nib.axshare.com/?id=blkfwt&p=%E5%B7%A5%E4%BD%9C%E5%8F%B0-%E7%95%8C%E9%9D%A2%E5%9B%BE%EF%BC%88%E4%BF%A1%E6%81%AF%E6%9E%B6%E6%9E%84%EF%BC%89&g=1', 'PFMEA一期迭代', NULL, 0, 0, 92074742051872, '2026-05-14 02:47:10.797', '2026-05-28 01:34:26.363');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074742379552, 'DQS - PFMEA一期 - UI', 'https://erdp.seres.cn/erdp-file/product-prototype/prod_env/2025-02-17-8c128d4f1b2d40d1a942b67797db7b97/index.html#p0', 'PFMEA一期迭代', NULL, 0, 0, 92074742051872, '2026-05-14 02:47:10.837', '2026-05-28 01:34:20.75');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074752341024, 'DQS - 市场数据分析统计 - 原型', 'https://y3x2r0.axshare.com/?id=xuoy9u&p=%E5%93%8D%E5%BA%94_%E6%94%B9%E8%BF%9B%E9%97%AE%E9%A2%98%E5%8E%9F%E5%9E%8B%E5%9B%BE&g=1', '市场数据分析迭代', NULL, 0, 0, 92074742051872, '2026-05-14 02:47:10.878', '2026-05-28 01:34:14.442');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074752234528, 'DQS - 问题横排 - 技术方案', 'https://sai-seres.feishu.cn/docx/VLsgdDQRbo19dbxkG3CcgUX9ntc', '问题横排迭代', NULL, 0, 0, 92074742051872, '2026-05-14 02:47:12.573', '2026-05-28 01:34:01.079');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074748191776, 'DQS - 问题横排 - 原型', 'https://erdp.seres.cn/objectstor01-prod/product-prototype/prod_env/2026-04-30-1aef79a6289d466b810fb77130aaa94b/prod_env/2026-04-29-d05fd57b946545689c93d850fce60262/prod_env/2026-04-29-7235c584875747aa852cb280944c87ec/prod_env/2026-04-29-3ea96785a2b84c7a87e9feae0e52dc60/prod_env/2026-04-24-2d4e28a016034ac080754d4544736650/index.html?id=xrntm2&p=%E9%97%AE%E9%A2%98%E6%A8%AA%E6%8E%92%EF%BC%88%E9%87%8D%E5%A4%8D%E9%97%AE%E9%A2%98%E9%A9%B3%E5%9B%9E%EF%BC%89&g=1&hi=1&sc=2', NULL, NULL, 0, 0, 92074742051872, '2026-05-14 02:47:12.649', '2026-05-28 01:33:56.409');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074747989024, 'DQS - 问题横排 - 需求', 'https://sai-seres.feishu.cn/wiki/HT1WwJhGNirKWokG4uzcBAKMnvh', NULL, NULL, 0, 0, 92074742051872, '2026-05-14 02:47:12.665', '2026-05-28 01:33:48.339');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074747472928, 'DQS - 同步BOP/BOM - 需求 字段对照表', 'https://sai-seres.feishu.cn/sheets/CvEXsoxT7hPck7tsIVKcILKmnsg', NULL, NULL, 0, 0, 92074742051872, '2026-05-14 02:47:12.682', '2026-05-28 01:33:43.642');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074746483744, 'DQS - 产品特性优化0515 - 任务排期', 'https://sai-seres.feishu.cn/wiki/IXELwB0geiwWjZkE5EFcjGKLnDh?table=tblwRJcIiqBVux9M&view=vewfOimpkN', NULL, NULL, 0, 0, 92074742051872, '2026-05-18 08:58:16.241', '2026-05-28 01:33:32.915');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074742514720, 'DQS - 产品特性优化0515 - 需求', 'https://sai-seres.feishu.cn/docx/DheYdCgo8oVigXxsnhxcQew9nUf', NULL, NULL, 0, 0, 92074742051872, '2026-05-18 08:32:21.013', '2026-05-28 01:33:23.072');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074743079968, 'DQS - 经验化看板 - 原型', 'https://erdp.seres.cn/objectstor01-prod/product-prototype/prod_env/2026-04-23-8e7fce631e6e4f7c997999813f7373c0/prod_env/2026-04-10-feba581c749441eaaa6d3e857cb4cca8/index.html?id=vb5g2c&p=%E7%BB%8F%E9%AA%8C%E5%8C%96%E7%9C%8B%E6%9D%BF&sc=1&hi=1', NULL, NULL, 0, 0, 92074742051872, '2026-05-28 01:29:30.965', '2026-05-28 01:33:15.451');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074752107552, 'DQS - BOP/BOM同步 - 测试用例', 'https://sai-seres.feishu.cn/file/VxiPbFCSQoVDn5xQFWhcE3IbnOb', '同步BOP/BOM迭代', NULL, 0, 0, 92074742051872, '2026-05-14 02:47:12.557', '2026-05-28 01:33:01.167');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074751988768, 'XxlJob - UAT', 'https://xxl-job-admin-uat.erdp-sd.seres.cn/xxl-job-admin/', NULL, NULL, 0, 0, 92074742051872, '2026-05-14 02:47:12.52', '2026-05-27 09:14:56.77');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074751872032, 'SigNoz | Home', 'https://signoz.erdp-sd.seres.cn/home', '查看应用日志', NULL, 0, 0, 92074742051872, '2026-05-14 02:47:11.37', '2026-05-27 09:14:56.729');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074751786016, '天行平台 - 本地', 'http://10.36.57.167:5173/fas/home', NULL, NULL, 0, 0, 92074742051872, '2026-05-14 02:47:11.349', '2026-05-27 09:14:56.685');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074751659040, '天行平台 - 测试', 'https://erdpqa.seres.cn/fas/home', NULL, NULL, 0, 0, 92074742051872, '2026-05-14 02:47:11.327', '2026-05-27 09:14:56.642');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074751063072, '天行平台 - UAT', 'https://erdpuat.seres.cn/fas/home', NULL, NULL, 0, 0, 92074742051872, '2026-05-14 02:47:11.306', '2026-05-27 09:14:56.599');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074753434656, '天行平台 - 正式', 'https://erdp.seres.cn/fas/home', NULL, NULL, 0, 0, 92074742051872, '2026-05-14 02:47:11.284', '2026-05-27 09:14:56.557');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074753336352, 'Rancher', 'https://rancher.erdp-sd.seres.cn/dashboard/home', '集团水土集群', NULL, 0, 0, 92074742051872, '2026-05-14 02:47:11.261', '2026-05-27 09:14:56.514');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074753213472, '账号查询', 'https://erdpqa.seres.cn/chandao-api/index.html', NULL, NULL, 0, 0, 92074742051872, '2026-05-14 02:47:11.239', '2026-05-27 09:14:56.472');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074753084448, 'DbGate', 'https://dbm.erdp-sd.seres.cn/', NULL, NULL, 0, 0, 92074742051872, '2026-05-14 02:47:11.218', '2026-05-27 09:14:56.427');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074749809696, 'GitLab', 'https://git.seres.cn/erdp/business/dqs/erdp-fas', '集团GitLab', NULL, 0, 0, 92074742051872, '2026-05-14 02:47:11.197', '2026-05-27 09:14:56.385');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074748843040, '禅道', 'https://ztpms.sokon.com:1443/execution-bug-3689.html', NULL, NULL, 0, 0, 92074742051872, '2026-05-14 02:47:11.176', '2026-05-27 09:14:56.341');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074748695584, '天行问数 - UAT', 'https://erdpuat.seres.cn/erdp-sqlbot/#/login', NULL, NULL, 0, 0, 92074742051872, '2026-05-14 02:47:11.153', '2026-05-27 09:14:56.297');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074748525600, '天行问数 - 生产', 'https://erdp.seres.cn/erdp-sqlbot/#/ds/index', NULL, NULL, 0, 0, 92074742051872, '2026-05-14 02:47:11.132', '2026-05-27 09:14:56.255');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074748410912, 'Sonar Qube', 'https://erdp.seres.cn/erdp-sonar/project/issues?resolved=false&types=BUG&inNewCodePeriod=true&id=erdp-fas', NULL, NULL, 0, 0, 92074742051872, '2026-05-14 02:47:11.11', '2026-05-27 09:14:56.213');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074745543712, 'Grafana DQS', 'https://grafana.erdp-sd.seres.cn/d/ce0j33mqspiwwa/index-erdp1?orgId=1&from=now-6h&to=now&timezone=browser', '查看应用日志', NULL, 0, 0, 92074742051872, '2026-05-14 02:47:11.037', '2026-05-27 09:14:56.171');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074745689120, 'Nexus', 'https://repo.seres.cn/nexus/#browse/browse', '集团Nexus', NULL, 0, 0, 92074742051872, '2026-05-14 02:47:11.012', '2026-05-27 09:14:56.13');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074745926688, 'SeresCloud', 'https://cloud.seres.cn/#/home', '集团流水线', NULL, 0, 0, 92074742051872, '2026-05-14 02:47:10.991', '2026-05-27 09:14:56.09');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074745426976, 'Nacos - QA', 'https://nacos-test.erdp-sd.seres.cn/nacos/', NULL, NULL, 0, 0, 92074742051872, '2026-05-14 02:47:10.971', '2026-05-27 09:14:56.049');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074745216032, 'Nacos - UAT', 'https://nacos-uat.erdp-sd.seres.cn/nacos/', NULL, NULL, 0, 0, 92074742051872, '2026-05-14 02:47:10.954', '2026-05-27 09:14:56.005');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074746358816, 'XxlJob - QA', 'https://xxl-job-admin-test.erdp-sd.seres.cn/xxl-job-admin/', NULL, NULL, 0, 0, 92074742051872, '2026-05-14 02:47:10.938', '2026-05-27 09:14:55.96');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074746260512, '运维门户', 'https://ad-erdp.seres.cn/ops/', NULL, NULL, 0, 0, 92074742051872, '2026-05-14 02:47:10.923', '2026-05-27 09:14:55.915');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074746741792, '‬⁢⁣​‍‌⁡​‍‬⁤‬‍‬​⁤⁡⁣⁣‌​‍​‍​‌⁢⁣⁢‬‬‍⁡﻿‬​‬⁢​⁣⁣﻿⁣⁣⁡⁤‌​​operation-log-starter - SAI 2.0云文档', 'https://sai-seres.feishu.cn/wiki/Rqcqwz1phif7qAkLwhCcwDbVnvc', NULL, NULL, 0, 0, 92074742051872, '2026-05-26 05:54:58.417', '2026-05-27 09:14:40.94');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074748320800, 'DQS', 'https://app.apifox.com/project/5449658', NULL, NULL, 0, 0, 92074742051872, '2026-05-15 06:15:13.004', '2026-05-27 09:14:40.811');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074751556640, 'DQS Api文档', 'http://localhost:8080/erdp-fas/doc.html#/home', 'DQS Api文档', NULL, 0, 0, 92074742051872, '2026-05-14 02:47:12.493', '2026-05-27 09:14:40.417');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074752928800, '北森API文档', 'https://assessdata.italent.cn/interfacedoc/%E5%AE%A2%E6%88%B7%E5%AF%B9%E6%8E%A5%E5%8F%8A%E5%B8%B8%E8%A7%81%E9%97%AE%E9%A2%98.html', '北森API接入文档', NULL, 0, 0, 92074742051872, '2026-05-14 02:47:11.436', '2026-05-27 09:14:40.385');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074751431712, '灵犀助手 - 文档', 'https://erdpqa.seres.cn/erdp-amap/doc.html#/default/dialogue-controller/getMessageByIdUsingGET', '灵犀助手API接入文档', NULL, 0, 0, 92074742051872, '2026-05-14 02:47:11.396', '2026-05-27 09:14:40.342');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074746100768, 'DQS - 研发质量部工作看板 - 原型', 'https://l1jz63.axshare.com/?id=08693p&p=%E7%A0%94%E5%8F%91%E8%B4%A8%E9%87%8F%E9%83%A8%E5%B7%A5%E4%BD%9C%E7%9C%8B%E6%9D%BF', '市场问题迭代', NULL, 0, 0, 92074742051872, '2026-05-14 02:47:10.717', '2026-05-27 09:14:40.084');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074745058336, 'DQS - 专项规避 - 流程图', 'https://www.processon.com/v/68ca106415c2b22f75f7dc29', '专项规避迭代', NULL, 0, 0, 92074742051872, '2026-05-14 02:47:10.417', '2026-05-27 09:14:39.91');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074744706080, 'DQS - 项目应规避 - 原型', 'https://erdp.seres.cn/erdp-file/product-prototype/prod_env/2025-08-19-f5650fb519f2488f8be2f3ab9b378050/index.html?id=n3ihla&p=pe%E8%B4%A3%E4%BB%BB%E4%BA%BA%E7%BC%96%E5%88%B6%E6%8E%AA%E6%96%BD%E8%AE%A1%E5%88%92&g=1', '项目应规避迭代', NULL, 0, 0, 92074742051872, '2026-05-14 02:47:10.37', '2026-05-27 09:14:39.867');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074744527904, 'DQS - 项目应规避 - 流程图', 'https://www.processon.com/v/687f3aed33494a11aa563b69', '项目应规避迭代', NULL, 0, 0, 92074742051872, '2026-05-14 02:47:10.337', '2026-05-27 09:14:39.824');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074744376352, 'DQS - 责任异动 - 原型', 'https://erdp.seres.cn/erdp-file/product-prototype/prod_env/2025-09-18-73dbd556cbd04432bf6f97e22a1c272c/index.html?id=4fezre&p=%E5%8E%9F%E5%9E%8B%E8%BF%AD%E4%BB%A3%E6%B8%85%E5%8D%95', '责任异动迭代', NULL, 0, 0, 92074742051872, '2026-05-14 02:47:10.295', '2026-05-27 09:14:39.784');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074749690912, 'DQS - 车系车型配置 - 原型', 'https://erdp.seres.cn/erdp-file/product-prototype/prod_env/2025-09-18-73dbd556cbd04432bf6f97e22a1c272c/start.html?p=%E8%BD%A6%E7%B3%BB%E8%BD%A6%E5%9E%8B%E9%85%8D%E7%BD%AE&id=umcq76&g=1', '车系车型配置迭代', NULL, 0, 0, 92074742051872, '2026-05-14 02:47:10.256', '2026-05-27 09:14:39.744');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074744249376, 'DQS - 产品特性 - 测试用例', 'https://alidocs.dingtalk.com/i/nodes/20eMKjyp81RNo5r3T7bwwBqyWxAZB1Gv?utm_scene=team_space', '产品特性迭代', NULL, 0, 0, 92074742051872, '2026-05-14 02:47:10.216', '2026-05-27 09:14:39.702');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074744069152, 'DQS - 产品特性 - 原型 2025-10-23', 'https://erdp.seres.cn/erdp-file/product-prototype/prod_env/2025-10-23-0ad8da3f116847478b08b4669c82b7bf/index.html?id=qt4vc6&p=%E6%B5%81%E7%A8%8B%E5%9B%BE&g=1', '产品特性迭代', NULL, 0, 0, 92074742051872, '2026-05-14 02:47:10.188', '2026-05-27 09:14:39.659');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074749430816, 'DQS - 产品特性 - 原型 2025-10-11', 'https://erdp.seres.cn/erdp-file/product-prototype/prod_env/2025-10-11-1de49ea6288044fb928c050931f01442/index.html?id=9yzcec&p=%E9%9B%B6%E9%83%A8%E4%BB%B6%E7%89%B9%E6%80%A7%E5%88%86%E6%9E%90%E6%B5%81%E7%A8%8B%E5%9B%BE&g=1', '产品特性迭代', NULL, 0, 0, 92074742051872, '2026-05-14 02:47:10.162', '2026-05-27 09:14:39.618');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074749322272, 'DQS - 产品特性 -流程图', 'https://www.processon.com/v/68e9d26f75ec317b63426252', '产品特性迭代', NULL, 0, 0, 92074742051872, '2026-05-14 02:47:10.142', '2026-05-27 09:14:39.576');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074752830496, 'DQS - 过程特性 - 测试用例', 'https://sai-seres.feishu.cn/sheets/MQ9EsqQ2ZhijAltxHHUcCpKZn1c', '过程特性迭代', NULL, 0, 0, 92074742051872, '2026-05-14 02:47:10.119', '2026-05-27 09:14:39.535');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074743878688, 'DQS - 过程特性 - 研发计划', 'https://sai-seres.feishu.cn/wiki/Rj9MwGpYKixUqrk8aoLcWToVnag?table=tblP2krgsb5AKPwY&view=vewYeCq9XS', '过程特性迭代', NULL, 0, 0, 92074742051872, '2026-05-14 02:47:10.075', '2026-05-27 09:14:39.494');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074752715808, 'DQS - 过程特性 - 原型 2026-02-28', 'https://erdp.seres.cn/erdp-file/product-prototype/prod_env/2026-02-28-41ceec51b82a4ea490e163650a60ffce/index.html?id=509sin&p=%E5%B7%A5%E8%89%BA%E7%BC%96%E5%88%B6%E9%A1%B5%E9%9D%A2&g=1', '过程特性迭代', NULL, 0, 0, 92074742051872, '2026-05-14 02:47:10.034', '2026-05-27 09:14:39.462');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074743684128, 'DQS - 过程特性 - 原型2026-03-02', 'https://erdp.seres.cn/erdp-file/product-prototype/prod_env/2026-03-02-b627300fa8224a7f93fc5443f63dc784/index.html?id=x4sdc4&p=%E5%8E%86%E5%8F%B2%E7%89%88%E6%9C%AC%E8%AE%B0%E5%BD%95', '过程特性迭代', NULL, 0, 0, 92074742051872, '2026-05-14 02:47:09.999', '2026-05-27 09:14:39.42');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074749019168, 'DQS - 过程特性 - 原型 2026-02-10', 'https://erdp.seres.cn/erdp-file/product-prototype/prod_env/2026-02-10-95620d9de307484dac57af1d45f6a8eb/index.html?id=ryeumz&p=%E5%AF%B9%E6%8E%A5bop%E6%95%B0%E6%8D%AE', '过程特性迭代', NULL, 0, 0, 92074742051872, '2026-05-14 02:47:09.959', '2026-05-27 09:14:39.378');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074749557792, 'DQS - 产品特性迭代 - 技术方案', 'https://sai-seres.feishu.cn/docx/Q2EfdTFUIopID8xyNHsctuCtn8b', '产品特性升级迭代', NULL, 0, 0, 92074742051872, '2026-05-14 02:47:09.918', '2026-05-27 09:14:39.336');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074746864672, 'DQS - 过程特性 - 原型 2026-01-06', 'https://erdp.seres.cn/erdp-file/product-prototype/prod_env/2026-01-06-ca412fa1fac644f2af35841eef269cb1/index.html?id=21x4mv&p=%E6%B5%81%E7%A8%8B%E5%9B%BE', '过程特性迭代', NULL, 0, 0, 92074742051872, '2026-05-14 02:47:09.592', '2026-05-27 09:14:39.294');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074746588192, 'DQS - 周报日报 - 原型', 'https://erdp.seres.cn/erdp-file/product-prototype/prod_env/2025-12-24-26d97c3e0dcd444cba6bf56ecd58ce91/index.html?id=xr2zrg&p=%E6%97%A5%E6%8A%A5%E8%AF%A6%E6%83%85%EF%BC%88pc%E7%AB%AF%EF%BC%89%E6%9F%A5%E7%9C%8B&g=1', '周报日报迭代', NULL, 0, 0, 92074742051872, '2026-05-14 02:47:09.545', '2026-05-27 09:14:39.252');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074749209632, 'DQS - 应规避工作台 - 原型', 'https://erdp.seres.cn/erdp-file/product-prototype/prod_env/2026-02-03-74b67784786c4bb4a21c2ab3e443f810/index.html?id=xrgvj9&p=%E5%BA%94%E8%A7%84%E9%81%BF%E5%B7%A5%E4%BD%9C%E5%8F%B0&g=1', '应规避工作台迭代', NULL, 0, 0, 92074742051872, '2026-05-14 02:47:09.505', '2026-05-27 09:14:39.21');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074750714912, '‌⁢‌‬‍​⁢⁤⁡‌​​​‍⁢⁤​​​‌⁤⁡⁢‌​​‬⁣⁢⁡﻿⁡‬‍‌​﻿﻿⁢⁣⁤⁣⁣⁣⁣​​⁣⁣‌DQS - 产品特性优化 - 上线', 'https://sai-seres.feishu.cn/docx/WnuYdfIJRo8vf0xewSiciA3Knhg', '产品特性迭代', NULL, 0, 0, 92074742051872, '2026-05-14 02:47:09.464', '2026-05-27 09:14:39.177');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074750612512, '⁣⁣⁣‬⁤‍‬‌‍⁤⁢‍⁤⁣​﻿​⁡⁣⁡﻿⁢﻿⁢⁡⁢⁤‬⁤⁢​⁢​​‌​‍⁢‬‬​​⁤⁢﻿​﻿⁣‌DQS - 产品特性升级 - 需求', 'https://sai-seres.feishu.cn/docx/VWVGd4PsBoCtOAxPrjcc3NWYnEf', '产品特性升级迭代', NULL, 0, 0, 92074742051872, '2026-05-14 02:47:09.438', '2026-05-27 09:14:39.137');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074750516256, 'DQS - 同步BOP/BOM - 原型', 'https://erdp.seres.cn/erdp-file/product-prototype/prod_env/2026-04-07-be6ff5fa11494e40b0a52fd98bfec4eb/index.html?id=0ruzlk&p=%E5%8E%9F%E5%9E%8B%E5%9B%BE%E8%BF%AD%E4%BB%A3%E5%8E%86%E5%8F%B2%E8%AE%B0%E5%BD%95', '同步BOP/BOM迭代', NULL, 0, 0, 92074742051872, '2026-05-14 02:47:09.416', '2026-05-27 09:14:39.093');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074743548960, 'SAI 2.0云文档', 'https://sai-seres.feishu.cn/wiki/WXGnwizjaiWr8Hkbelcc7OYqnic', NULL, NULL, 0, 0, 92074742051872, '2026-05-26 02:33:22.473', '2026-05-27 09:14:22.777');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074743204896, 'K89 Beta日常专项测试 5.18-5.30 - SAI 2.0云文档', 'https://sai-seres.feishu.cn/wiki/SX3sw6jtFinm1UkME8hc8cTCn6g', NULL, NULL, 0, 0, 92074742051872, '2026-05-25 02:46:21.236', '2026-05-27 09:14:22.754');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074743438368, '‍‬‌⁣⁢​‌⁤​‍‌⁢​​‍⁣⁣⁣⁢⁤﻿​⁢⁣‬﻿‌‌​​﻿⁣⁢​​⁢⁡‬⁢​⁢⁤‌​​‍⁤​﻿‍各单位数字化+AI工作人员统计（工程数字) - SAI 2.0云文档', 'https://sai-seres.feishu.cn/sheets/IJ30s9ffwhS4tMtVx0hcate1nfb', NULL, NULL, 0, 0, 92074742051872, '2026-05-15 02:51:10.248', '2026-05-27 09:14:22.735');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074752570400, '【机密】专利排查工作案件清单 - 数字应用 - SAI 2.0云文档', 'https://sai-seres.feishu.cn/sheets/GNBFsyZqdh9YsFtw5hGcRTX1nBe', NULL, NULL, 0, 0, 92074742051872, '2026-05-15 02:38:06.811', '2026-05-27 09:14:22.719');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074752459808, '‌⁣﻿﻿​​​⁣‬‌‍‬⁢⁣⁤﻿⁣​‌⁤﻿​﻿﻿​​⁢⁤​﻿​⁢⁢‬‬​‌⁢⁤﻿​​‍⁤‍⁤⁤⁤‌‍请2.5日11：00前完成整车技术平台2026年春节期间出勤计划统计表 - SAI 2.0云文档', 'https://sai-seres.feishu.cn/wiki/UVp7wdDkPiq1SWkHbKOc9rs2nbd?sheet=iT4So2', NULL, NULL, 0, 0, 92074742051872, '2026-05-14 02:47:11.574', '2026-05-27 09:14:22.699');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074750129184, 'SAI 2.0', 'https://sai2.seres.cn/dispatch/', NULL, NULL, 0, 0, 92074742051872, '2026-05-14 02:47:11.548', '2026-05-27 09:14:22.679');
INSERT INTO bookmarks (id, title, url, description, favicon_url, is_pinned, click_count, category_id, created_at, updated_at)
VALUES (92074749105184, 'SAI', 'https://download.dingtalk.com/guest/welcome?uuid=06a01b1e-1f9c-4253-a0dd-c04f52f2d00a450', NULL, NULL, 0, 0, 92074742051872, '2026-05-14 02:47:11.488', '2026-05-27 09:14:22.651');

INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074751157280, 92074737458208);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074751157280, 92074739883040);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074751157280, 92074738678816);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074750952480, 92074740438048);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074750952480, 92074737458208);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074750952480, 92074739883040);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074746989600, 92074737458208);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074746989600, 92074739883040);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074746989600, 92074737609760);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074753571872, 92074741347360);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074754567200, 92074741347360);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074754456608, 92074741347360);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074754290720, 92074741347360);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074754202656, 92074741347360);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074753838112, 92074741347360);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074754067488, 92074741347360);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074753711136, 92074741347360);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074753983520, 92074741347360);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074754749472, 92074741347360);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074754962464, 92074741611552);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074754962464, 92074741083168);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074755046432, 92074741732384);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074755165216, 92074741611552);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074755247136, 92074741732384);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074755421216, 92074741732384);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074755662880, 92074741732384);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074755826720, 92074741824544);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074755910688, 92074741824544);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074756043808, 92074741824544);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074756135968, 92074741937184);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074756305952, 92074741611552);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074756305952, 92074741083168);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074756527136, 92074741611552);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074756527136, 92074741083168);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074756725792, 92074741732384);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074756832288, 92074741937184);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074757073952, 92074741937184);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074757262368, 92074741732384);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074757372960, 92074741732384);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074757454880, 92074741732384);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074757604384, 92074741732384);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074757862432, 92074741732384);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074757983264, 92074741732384);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074758128672, 92074741732384);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074758349856, 92074741611552);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074758349856, 92074741083168);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074758458400, 92074741611552);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074758458400, 92074741083168);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074758538272, 92074741732384);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074758614048, 92074741732384);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074758691872, 92074741732384);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074758870048, 92074741732384);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074742860832, 92074737849376);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074742860832, 92074737943584);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074742860832, 92074737458208);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074742742048, 92074737130528);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074742742048, 92074737943584);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074742742048, 92074737458208);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074742957088, 92074737458208);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074742957088, 92074738062368);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074742957088, 92074737609760);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074750010400, 92074737458208);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074750010400, 92074739883040);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074750010400, 92074737609760);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074751296544, 92074737458208);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074751296544, 92074740978720);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074751296544, 92074737609760);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074742621216, 92074737849376);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074742621216, 92074737298464);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074742621216, 92074737458208);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074742379552, 92074737130528);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074742379552, 92074737298464);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074742379552, 92074737458208);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074752341024, 92074737458208);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074752341024, 92074741204000);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074752341024, 92074737609760);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074752234528, 92074740438048);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074752234528, 92074740104224);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074752234528, 92074737458208);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074748191776, 92074737130528);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074748191776, 92074740104224);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074748191776, 92074737458208);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074747989024, 92074740104224);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074747989024, 92074737458208);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074747989024, 92074737609760);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074747472928, 92074739987488);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074747472928, 92074737458208);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074747472928, 92074737609760);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074746483744, 92074737710112);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074746483744, 92074737458208);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074746483744, 92074738678816);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074742514720, 92074737710112);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074742514720, 92074737458208);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074742514720, 92074737609760);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074743079968, 92074737130528);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074743079968, 92074738236448);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074752107552, 92074738897952);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074752107552, 92074739987488);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074752107552, 92074737458208);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074751988768, 92074739452960);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074751872032, 92074739452960);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074751872032, 92074739563552);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074751786016, 92074739452960);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074751659040, 92074739452960);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074751063072, 92074739452960);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074753434656, 92074739452960);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074753336352, 92074739452960);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074753336352, 92074739563552);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074753213472, 92074739452960);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074753084448, 92074739452960);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074749809696, 92074739452960);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074748843040, 92074739452960);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074748695584, 92074739452960);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074748525600, 92074739452960);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074748410912, 92074739452960);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074745543712, 92074739452960);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074745543712, 92074739563552);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074745689120, 92074739452960);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074745926688, 92074739452960);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074745426976, 92074739452960);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074745216032, 92074739452960);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074746358816, 92074739452960);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074746260512, 92074739452960);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074746741792, 92074737458208);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074748320800, 92074737458208);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074751556640, 92074737458208);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074751556640, 92074741083168);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074752928800, 92074737458208);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074752928800, 92074741083168);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074751431712, 92074737458208);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074751431712, 92074741083168);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074746100768, 92074737130528);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074746100768, 92074737458208);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074746100768, 92074738062368);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074745058336, 92074737130528);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074745058336, 92074739293216);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074745058336, 92074737458208);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074744706080, 92074737130528);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074744706080, 92074739137568);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074744706080, 92074737458208);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074744527904, 92074737130528);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074744527904, 92074739137568);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074744527904, 92074737458208);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074744376352, 92074739006496);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074744376352, 92074737130528);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074744376352, 92074737458208);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074749690912, 92074737130528);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074749690912, 92074740769824);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074749690912, 92074737458208);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074744249376, 92074738897952);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074744249376, 92074737458208);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074744249376, 92074738795552);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074744069152, 92074737130528);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074744069152, 92074737458208);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074744069152, 92074738795552);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074749430816, 92074737130528);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074749430816, 92074737458208);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074749430816, 92074738795552);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074749322272, 92074737130528);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074749322272, 92074737458208);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074749322272, 92074738795552);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074752830496, 92074738897952);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074752830496, 92074738590752);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074752830496, 92074737458208);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074743878688, 92074738590752);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074743878688, 92074737458208);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074743878688, 92074738678816);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074752715808, 92074737130528);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074752715808, 92074738590752);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074752715808, 92074737458208);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074743684128, 92074737130528);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074743684128, 92074738590752);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074743684128, 92074737458208);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074749019168, 92074737130528);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074749019168, 92074738590752);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074749019168, 92074737458208);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074749557792, 92074740597792);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074749557792, 92074740438048);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074749557792, 92074737458208);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074746864672, 92074737130528);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074746864672, 92074738590752);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074746864672, 92074737458208);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074746588192, 92074737130528);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074746588192, 92074737458208);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074746588192, 92074739702816);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074749209632, 92074737130528);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074749209632, 92074737458208);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074749209632, 92074740270112);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074750714912, 92074737458208);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074750714912, 92074738795552);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074750714912, 92074740900896);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074750612512, 92074740597792);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074750612512, 92074737458208);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074750612512, 92074737609760);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074750516256, 92074737130528);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074750516256, 92074739987488);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074750516256, 92074737458208);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074743548960, 92074738500640);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074743204896, 92074738500640);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074743438368, 92074738500640);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074752570400, 92074738500640);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074752459808, 92074738500640);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074750129184, 92074738500640);
INSERT INTO bookmark_tags (bookmark_id, tag_id) VALUES (92074749105184, 92074738500640);
