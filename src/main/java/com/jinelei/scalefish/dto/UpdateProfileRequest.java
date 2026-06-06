package com.jinelei.scalefish.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "更新个人信息请求")
public record UpdateProfileRequest(
    @Schema(description = "显示名称") String name,
    @Schema(description = "邮箱") String email
) {}
