package com.jinelei.scalefish.repository;

import com.jinelei.scalefish.entity.ApiToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ApiTokenRepository extends JpaRepository<ApiToken, Long> {

    List<ApiToken> findByUserIdOrderByCreatedAtDesc(Long userId);

    Optional<ApiToken> findByTokenHash(String tokenHash);

    void deleteByUserId(Long userId);

    @Modifying
    @Query("update ApiToken t set t.lastUsedAt = :now, t.updatedAt = :now where t.id = :id")
    int touchLastUsed(@Param("id") Long id, @Param("now") LocalDateTime now);
}
