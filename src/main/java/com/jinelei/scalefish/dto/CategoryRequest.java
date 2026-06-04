package com.jinelei.scalefish.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "创建/更新分类请求")
public record CategoryRequest(
    @NotBlank
    @Schema(description = "分类名称", example = "前端")
    String name,

    @Schema(description = "父分类 ID", example = "1")
    Long parentId,

    @Schema(description = "排序号", example = "1")
    Integer sortOrder
) {
}
