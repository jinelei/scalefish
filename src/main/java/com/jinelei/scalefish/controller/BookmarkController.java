package com.jinelei.scalefish.controller;

import com.jinelei.scalefish.dto.BookmarkRequest;
import com.jinelei.scalefish.dto.BookmarkResponse;
import com.jinelei.scalefish.dto.GenericResult;
import com.jinelei.scalefish.dto.PageResponse;
import com.jinelei.scalefish.service.BookmarkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "书签管理", description = "书签的增删改查、搜索、置顶、点击计数")
@RestController
@RequestMapping("/api/bookmarks")
public class BookmarkController {

    private final BookmarkService bookmarkService;

    public BookmarkController(BookmarkService bookmarkService) {
        this.bookmarkService = bookmarkService;
    }

    @Operation(summary = "搜索书签", description = "按关键字、分类、标签、置顶状态分页搜索书签")
    @GetMapping
    public GenericResult<PageResponse<BookmarkResponse>> search(
            @Parameter(description = "搜索关键字") @RequestParam(required = false) String keyword,
            @Parameter(description = "分类 ID") @RequestParam(required = false) Long categoryId,
            @Parameter(description = "标签 ID") @RequestParam(required = false) Long tagId,
            @Parameter(description = "是否置顶") @RequestParam(required = false) Boolean pinned,
            @Parameter(description = "页码（从 0 开始）", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页条数", example = "20") @RequestParam(defaultValue = "20") int size) {
        return GenericResult.success(bookmarkService.search(keyword, categoryId, tagId, pinned, page, size));
    }

    @Operation(summary = "获取书签详情")
    @GetMapping("/{id}")
    public GenericResult<BookmarkResponse> getById(@Parameter(description = "书签 ID") @PathVariable Long id) {
        return GenericResult.success(bookmarkService.getById(id));
    }

    @Operation(summary = "创建书签")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GenericResult<BookmarkResponse> create(@Valid @RequestBody BookmarkRequest req) {
        return GenericResult.created(bookmarkService.create(req));
    }

    @Operation(summary = "更新书签")
    @PutMapping("/{id}")
    public GenericResult<BookmarkResponse> update(@Parameter(description = "书签 ID") @PathVariable Long id,
                                                   @Valid @RequestBody BookmarkRequest req) {
        return GenericResult.success(bookmarkService.update(id, req));
    }

    @Operation(summary = "删除书签")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public GenericResult<Void> delete(@Parameter(description = "书签 ID") @PathVariable Long id) {
        bookmarkService.delete(id);
        return GenericResult.noContent();
    }

    @Operation(summary = "切换置顶状态")
    @PatchMapping("/{id}/pin")
    public GenericResult<BookmarkResponse> togglePin(@Parameter(description = "书签 ID") @PathVariable Long id) {
        return GenericResult.success(bookmarkService.togglePin(id));
    }

    @Operation(summary = "记录点击", description = "书签点击次数 +1")
    @PostMapping("/{id}/click")
    public GenericResult<BookmarkResponse> recordClick(@Parameter(description = "书签 ID") @PathVariable Long id) {
        return GenericResult.success(bookmarkService.recordClick(id));
    }
}
