# Scalefish Server

基于 Spring Boot 4 + Java 21 的个人数据管理后端服务，提供 REST API，专注于书签管理。

## 功能

- **书签管理** — 增删改查、分类、标签、搜索筛选、置顶、点击统计
- **认证** — REST API 使用 JWT，支持 API Token
- **API 文档** — OpenAPI / Swagger UI

## 技术栈

- Spring Boot 4.0.6 + Spring Security
- Spring Data JPA + Liquibase
- MariaDB（生产）/ H2（开发测试）
- Snowflake ID 生成器

## 快速开始

```bash
# 开发环境
./gradlew bootRun

# 构建
./gradlew build
```
