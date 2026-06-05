package com.jinelei.scalefish.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "通用响应结果")
public record GenericResult<T>(
    @Schema(description = "状态码", example = "200") int code,
    @Schema(description = "提示信息", example = "success") String message,
    @Schema(description = "数据") T data
) {

    public static <T> GenericResult<T> success(T data) {
        return new GenericResult<>(200, "success", data);
    }

    public static <T> GenericResult<T> created(T data) {
        return new GenericResult<>(201, "created", data);
    }

    public static <T> GenericResult<T> noContent() {
        return new GenericResult<>(204, "no content", null);
    }

    public static <T> GenericResult<T> error(int code, String message) {
        return new GenericResult<>(code, message, null);
    }
}
