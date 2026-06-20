package com.jinelei.scalefish.controller;

import com.jinelei.scalefish.dto.GenericResult;
import com.jinelei.scalefish.service.AppConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/app-config")
public class AppConfigController {

    private static final Logger log = LoggerFactory.getLogger(AppConfigController.class);

    private final AppConfigService appConfigService;

    public AppConfigController(AppConfigService appConfigService) {
        this.appConfigService = appConfigService;
    }

    @GetMapping
    public GenericResult<Map<String, String>> getAll() {
        log.debug("GET /api/app-config");
        return GenericResult.success(appConfigService.getAll());
    }

    @PutMapping
    public GenericResult<Void> update(@RequestBody Map<String, String> config) {
        log.info("PUT /api/app-config - config={}", config);
        appConfigService.update(config);
        return GenericResult.success(null);
    }
}
