package com.jinelei.scalefish.repository;

import com.jinelei.scalefish.entity.AppConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppConfigRepository extends JpaRepository<AppConfig, String> {
    Optional<AppConfig> findByKey(String key);
}
