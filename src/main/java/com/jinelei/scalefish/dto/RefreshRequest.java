package com.jinelei.scalefish.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "刷新令牌请求")
public record RefreshRequest(
    @NotBlank @Schema(description = "刷新令牌") String refreshToken
) {}
