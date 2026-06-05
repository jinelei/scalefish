package com.jinelei.scalefish.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "注册请求")
public record RegisterRequest(
    @NotBlank @Size(min = 2, max = 100) @Schema(description = "用户名") String username,
    @NotBlank @Size(min = 6) @Schema(description = "密码") String password,
    @Schema(description = "显示名称") String name,
    @Schema(description = "邮箱") String email
) {}
