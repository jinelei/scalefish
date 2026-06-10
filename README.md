# Scalefish Server

基于 Spring Boot 4 + Java 21 的个人数据管理后端服务，支持 REST API 和 WebDAV（CalDAV / CardDAV）协议，可作为轻量级 Nextcloud 替代方案。

## 功能

- **书签管理** — 增删改查、分类、标签、搜索筛选、置顶、点击统计
- **日历 (CalDAV)** — 管理日历和事件（iCal/ICS），支持重复规则、全天事件，兼容 Thunderbird / macOS 日历 / iOS
- **联系人 (CardDAV)** — 管理通讯录和联系人（vCard/VCF），兼容 Thunderbird / macOS 通讯录 / iOS
- **认证** — REST API 使用 JWT，WebDAV 使用 HTTP Basic Auth，支持 API Token
- **自动发现** — `/.well-known` 端点，方便客户端配置
- **API 文档** — OpenAPI / Swagger UI

## 技术栈

- Spring Boot 4.0.6 + Spring Security
- Spring Data JPA + Liquibase
- MariaDB（生产）/ H2（开发测试）
- iCal4j、ez-vcard
- Snowflake ID 生成器

## 快速开始

```bash
# 开发环境
./gradlew bootRun

# 构建
./gradlew build
```
