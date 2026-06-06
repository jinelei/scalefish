package com.jinelei.scalefish.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "修改密码请求")
public record ChangePasswordRequest(
    @NotBlank @Schema(description = "当前密码") String oldPassword,
    @NotBlank @Size(min = 6) @Schema(description = "新密码") String newPassword
) {}
