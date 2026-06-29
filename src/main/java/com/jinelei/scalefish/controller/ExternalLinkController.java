package com.jinelei.scalefish.controller;

import com.jinelei.scalefish.dto.ExternalLinkRequest;
import com.jinelei.scalefish.dto.ExternalLinkResponse;
import com.jinelei.scalefish.dto.GenericResult;
import com.jinelei.scalefish.service.ExternalLinkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "外部链接管理", description = "外部超链接的增删改查")
@RestController
@RequestMapping("/api/external-links")
public class ExternalLinkController {

    private static final Logger log = LoggerFactory.getLogger(ExternalLinkController.class);

    private final ExternalLinkService externalLinkService;

    public ExternalLinkController(ExternalLinkService externalLinkService) {
        this.externalLinkService = externalLinkService;
    }

    @Operation(summary = "获取全部外部链接")
    @GetMapping
    public GenericResult<List<ExternalLinkResponse>> getAll() {
        log.debug("GET /api/external-links");
        return GenericResult.success(externalLinkService.getAll());
    }

    @Operation(summary = "创建外部链接")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GenericResult<ExternalLinkResponse> create(@Valid @RequestBody ExternalLinkRequest req) {
        log.info("POST /api/external-links - name={}, url={}", req.name(), req.url());
        return GenericResult.created(externalLinkService.create(req));
    }

    @Operation(summary = "更新外部链接")
    @PutMapping("/{id}")
    public GenericResult<ExternalLinkResponse> update(
            @Parameter(description = "外部链接 ID") @PathVariable Long id,
            @Valid @RequestBody ExternalLinkRequest req) {
        log.info("PUT /api/external-links/{} - name={}", id, req.name());
        return GenericResult.success(externalLinkService.update(id, req));
    }

    @Operation(summary = "删除外部链接")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public GenericResult<Void> delete(@Parameter(description = "外部链接 ID") @PathVariable Long id) {
        log.info("DELETE /api/external-links/{}", id);
        externalLinkService.delete(id);
        return GenericResult.noContent();
    }
}
