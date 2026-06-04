package com.jinelei.scalefish.dto;

import com.jinelei.scalefish.entity.Bookmark;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Schema(description = "书签响应")
public record BookmarkResponse(
    @Schema(description = "ID", example = "1") Long id,
    @Schema(description = "标题", example = "Spring Boot 官方文档") String title,
    @Schema(description = "URL", example = "https://spring.io/projects/spring-boot") String url,
    @Schema(description = "备注描述") String description,
    @Schema(description = "图标 URL") String faviconUrl,
    @Schema(description = "是否置顶", example = "false") boolean pinned,
    @Schema(description = "点击次数", example = "10") int clickCount,
    @Schema(description = "所属分类") CategoryBrief category,
    @Schema(description = "标签列表") Set<TagResponse> tags,
    @Schema(description = "创建时间") LocalDateTime createdAt,
    @Schema(description = "更新时间") LocalDateTime updatedAt
) {
    public static BookmarkResponse from(Bookmark b) {
        CategoryBrief cat = b.getCategory() == null
            ? null
            : new CategoryBrief(b.getCategory().getId(), b.getCategory().getName());
        return new BookmarkResponse(
            b.getId(), b.getTitle(), b.getUrl(), b.getDescription(),
            b.getFaviconUrl(), b.isPinned(), b.getClickCount(),
            cat,
            b.getTags().stream().map(TagResponse::from).collect(Collectors.toSet()),
            b.getCreatedAt(), b.getUpdatedAt()
        );
    }
}
