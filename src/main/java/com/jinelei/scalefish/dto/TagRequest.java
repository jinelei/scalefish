package com.jinelei.scalefish.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "创建标签请求")
public record TagRequest(
    @NotBlank
    @Schema(description = "标签名称", example = "java")
    String name
) {
}
