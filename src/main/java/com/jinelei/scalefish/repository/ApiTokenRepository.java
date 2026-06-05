package com.jinelei.scalefish.repository;

import com.jinelei.scalefish.entity.ApiToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ApiTokenRepository extends JpaRepository<ApiToken, Long> {

    List<ApiToken> findByUserIdOrderByCreatedAtDesc(Long userId);

    Optional<ApiToken> findByTokenHash(String tokenHash);

    void deleteByUserId(Long userId);
}
