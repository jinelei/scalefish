package com.jinelei.scalefish.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "标签统计响应")
public record TagStatsResponse(
    @Schema(description = "标签 ID") Long id,
    @Schema(description = "标签名称") String name,
    @Schema(description = "关联书签数量") int count
) {}
