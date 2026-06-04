package com.jinelei.scalefish.dto;

import com.jinelei.scalefish.entity.Tag;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "标签响应")
public record TagResponse(
    @Schema(description = "ID", example = "1") Long id,
    @Schema(description = "标签名称", example = "spring") String name
) {
    public static TagResponse from(Tag t) {
        return new TagResponse(t.getId(), t.getName());
    }
}
