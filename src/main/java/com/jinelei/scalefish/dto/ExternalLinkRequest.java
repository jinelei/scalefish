package com.jinelei.scalefish.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "创建/更新外部链接请求")
public record ExternalLinkRequest(
    @NotBlank
    @Schema(description = "链接名称", example = "音乐")
    String name,

    @NotBlank
    @Schema(description = "链接 URL", example = "https://music.example.com")
    String url,

    @Schema(description = "图标名称（可选）", example = "FiMusic")
    String icon,

    @Schema(description = "排序序号", example = "1")
    Integer sortOrder
) {}
