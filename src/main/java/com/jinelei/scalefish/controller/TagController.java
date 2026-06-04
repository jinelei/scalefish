package com.jinelei.scalefish.controller;

import com.jinelei.scalefish.dto.GenericResult;
import com.jinelei.scalefish.dto.TagRequest;
import com.jinelei.scalefish.dto.TagResponse;
import com.jinelei.scalefish.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@Tag(name = "标签管理", description = "标签的增删查")
@RestController
@RequestMapping("/api/tags")
public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @Operation(summary = "获取全部标签")
    @GetMapping
    public GenericResult<List<TagResponse>> getAll() {
        return GenericResult.success(tagService.getAll());
    }

    @Operation(summary = "创建标签")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GenericResult<com.jinelei.scalefish.entity.Tag> create(@Valid @RequestBody TagRequest req) {
        return GenericResult.created(tagService.create(req));
    }

    @Operation(summary = "删除标签")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public GenericResult<Void> delete(@Parameter(description = "标签 ID") @PathVariable Long id) {
        tagService.delete(id);
        return GenericResult.noContent();
    }
}
