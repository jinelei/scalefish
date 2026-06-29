package com.jinelei.scalefish.dto;

import com.jinelei.scalefish.entity.ExternalLink;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "外部链接响应")
public record ExternalLinkResponse(
    @Schema(description = "ID", example = "1") Long id,
    @Schema(description = "链接名称", example = "音乐") String name,
    @Schema(description = "链接 URL", example = "https://music.example.com") String url,
    @Schema(description = "图标名称", example = "FiMusic") String icon,
    @Schema(description = "排序序号", example = "1") Integer sortOrder
) {
    public static ExternalLinkResponse from(ExternalLink link) {
        return new ExternalLinkResponse(link.getId(), link.getName(), link.getUrl(), link.getIcon(), link.getSortOrder());
    }
}
