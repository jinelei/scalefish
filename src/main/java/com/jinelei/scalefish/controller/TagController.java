package com.jinelei.scalefish.controller;

import com.jinelei.scalefish.dto.GenericResult;
import com.jinelei.scalefish.dto.TagRequest;
import com.jinelei.scalefish.dto.TagResponse;
import com.jinelei.scalefish.dto.TagStatsResponse;
import com.jinelei.scalefish.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@Tag(name = "标签管理", description = "标签的增删查")
@RestController
@RequestMapping("/api/tags")
public class TagController {

    private static final Logger log = LoggerFactory.getLogger(TagController.class);

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @Operation(summary = "获取全部标签")
    @GetMapping
    public GenericResult<List<TagResponse>> getAll() {
        log.debug("GET /api/tags");
        return GenericResult.success(tagService.getAll());
    }

    @Operation(summary = "获取标签统计", description = "按分类/标签筛选统计各标签关联书签数")
    @GetMapping("/stats")
    public GenericResult<List<TagStatsResponse>> getStats(
            @Parameter(description = "分类 ID 列表") @RequestParam(required = false) List<Long> categoryIds,
            @Parameter(description = "标签 ID 列表") @RequestParam(required = false) List<Long> tagIds) {
        log.debug("GET /api/tags/stats - categoryIds={}, tagIds={}", categoryIds, tagIds);
        return GenericResult.success(tagService.getTagStats(categoryIds, tagIds));
    }

    @Operation(summary = "创建标签")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GenericResult<com.jinelei.scalefish.entity.Tag> create(@Valid @RequestBody TagRequest req) {
        log.info("POST /api/tags - name={}", req.name());
        return GenericResult.created(tagService.create(req));
    }

    @Operation(summary = "删除标签")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public GenericResult<Void> delete(@Parameter(description = "标签 ID") @PathVariable Long id) {
        log.info("DELETE /api/tags/{}", id);
        tagService.delete(id);
        return GenericResult.noContent();
    }
}
