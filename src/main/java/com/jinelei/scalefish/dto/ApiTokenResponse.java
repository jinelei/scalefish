package com.jinelei.scalefish.dto;

import java.time.LocalDateTime;

public record ApiTokenResponse(
    Long id,
    String name,
    String token,
    String tokenPrefix,
    LocalDateTime expiresAt,
    LocalDateTime lastUsedAt,
    LocalDateTime createdAt
) {}
