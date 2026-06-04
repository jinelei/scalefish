package com.jinelei.scalefish.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "分类简要信息")
public record CategoryBrief(
    @Schema(description = "ID", example = "1") Long id,
    @Schema(description = "分类名称", example = "前端") String name
) {
}
