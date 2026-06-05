package com.jinelei.scalefish.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "认证响应")
public record AuthResponse(
    @Schema(description = "访问令牌") String accessToken,
    @Schema(description = "刷新令牌") String refreshToken,
    @Schema(description = "过期秒数") long expiresIn,
    @Schema(description = "用户信息") UserInfo user
) {
    public record UserInfo(Long id, String username, String name, String email) {}
}
