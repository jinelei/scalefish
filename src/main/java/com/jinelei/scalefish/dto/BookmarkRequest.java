package com.jinelei.scalefish.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import java.util.Set;

@Schema(description = "创建/更新书签请求")
public record BookmarkRequest(
    @NotBlank
    @Schema(description = "标题", example = "Spring Boot 官方文档")
    String title,

    @NotBlank
    @Schema(description = "URL", example = "https://spring.io/projects/spring-boot")
    String url,

    @Schema(description = "备注描述", example = "Spring Boot 官方参考文档")
    String description,

    @Schema(description = "分类 ID", example = "1")
    Long categoryId,

    @Schema(description = "标签 ID 集合", example = "[1, 2]")
    Set<Long> tagIds
) {
}
