package com.jinelei.scalefish.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "分页响应")
public record PageResponse<T>(
    @Schema(description = "数据列表") List<T> content,
    @Schema(description = "总记录数", example = "100") long totalElements,
    @Schema(description = "总页数", example = "5") int totalPages,
    @Schema(description = "当前页码（从 0 开始）", example = "0") int currentPage
) {
}
