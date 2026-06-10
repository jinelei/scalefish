package com.jinelei.scalefish.controller;

import com.jinelei.scalefish.dto.ApiTokenRequest;
import com.jinelei.scalefish.dto.ApiTokenResponse;
import com.jinelei.scalefish.dto.GenericResult;
import com.jinelei.scalefish.entity.User;
import com.jinelei.scalefish.service.ApiTokenService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tokens")
public class ApiTokenController {

    private static final Logger log = LoggerFactory.getLogger(ApiTokenController.class);

    private final ApiTokenService apiTokenService;

    public ApiTokenController(ApiTokenService apiTokenService) {
        this.apiTokenService = apiTokenService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GenericResult<ApiTokenResponse> create(@AuthenticationPrincipal User user,
                                                   @Valid @RequestBody ApiTokenRequest request) {
        log.info("POST /api/tokens - name={}, userId={}", request.name(), user.getId());
        var data = apiTokenService.create(user.getId(), request);
        return GenericResult.success(data);
    }

    @GetMapping
    public GenericResult<List<ApiTokenResponse>> list(@AuthenticationPrincipal User user) {
        log.debug("GET /api/tokens - userId={}", user.getId());
        var data = apiTokenService.list(user.getId());
        return GenericResult.success(data);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void revoke(@AuthenticationPrincipal User user, @PathVariable Long id) {
        log.info("DELETE /api/tokens/{} - userId={}", id, user.getId());
        apiTokenService.revoke(user.getId(), id);
    }
}
