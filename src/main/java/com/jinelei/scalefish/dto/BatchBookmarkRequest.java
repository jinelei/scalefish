package com.jinelei.scalefish.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Set;

@Schema(description = "批量更新书签请求")
public record BatchBookmarkRequest(
    @Schema(description = "书签 ID 列表")
    Set<Long> ids,

    @Schema(description = "分类 ID（传 null 表示不修改）")
    Long categoryId,

    @Schema(description = "要追加的标签 ID 集合")
    Set<Long> addTagIds,

    @Schema(description = "要删除的标签 ID 集合")
    Set<Long> removeTagIds
) {
}
