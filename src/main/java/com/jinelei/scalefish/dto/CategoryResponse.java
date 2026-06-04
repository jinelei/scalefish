package com.jinelei.scalefish.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "分类树响应")
public record CategoryResponse(
    @Schema(description = "ID", example = "1") Long id,
    @Schema(description = "分类名称", example = "技术") String name,
    @Schema(description = "排序号", example = "1") Integer sortOrder,
    @Schema(description = "子分类列表") List<CategoryResponse> children
) {
}
