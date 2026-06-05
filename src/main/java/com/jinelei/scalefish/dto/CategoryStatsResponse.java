package com.jinelei.scalefish.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "分类统计响应（含子分类）")
public record CategoryStatsResponse(
    @Schema(description = "分类 ID") Long id,
    @Schema(description = "分类名称") String name,
    @Schema(description = "关联书签数量（含子分类）") int count
) {}
