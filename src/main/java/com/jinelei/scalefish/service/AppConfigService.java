package com.jinelei.scalefish.service;

import com.jinelei.scalefish.entity.AppConfig;
import com.jinelei.scalefish.repository.AppConfigRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
public class AppConfigService {

    private static final Logger log = LoggerFactory.getLogger(AppConfigService.class);

    private final AppConfigRepository appConfigRepository;

    public AppConfigService(AppConfigRepository appConfigRepository) {
        this.appConfigRepository = appConfigRepository;
    }

    @Transactional(readOnly = true)
    public Map<String, String> getAll() {
        Map<String, String> map = new HashMap<>();
        for (AppConfig c : appConfigRepository.findAll()) {
            map.put(c.getKey(), c.getValue());
        }
        return map;
    }

    @Transactional(readOnly = true)
    public String get(String key) {
        return appConfigRepository.findById(key)
                .map(AppConfig::getValue)
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public String getDisplayName() {
        String name = get("display_name");
        return name != null ? name : "scalefish";
    }

    @Transactional
    public void update(Map<String, String> config) {
        log.info("Update app config: {}", config);
        for (Map.Entry<String, String> entry : config.entrySet()) {
            AppConfig cfg = appConfigRepository.findById(entry.getKey())
                    .orElse(new AppConfig(entry.getKey(), entry.getValue()));
            cfg.setValue(entry.getValue());
            appConfigRepository.save(cfg);
        }
    }
}
