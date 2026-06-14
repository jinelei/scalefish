package com.jinelei.scalefish.service;

import com.jinelei.scalefish.dto.ApiTokenRequest;
import com.jinelei.scalefish.dto.ApiTokenResponse;
import com.jinelei.scalefish.entity.ApiToken;
import com.jinelei.scalefish.exception.BusinessException;
import com.jinelei.scalefish.exception.ErrorCode;
import com.jinelei.scalefish.exception.ResourceNotFoundException;
import com.jinelei.scalefish.repository.ApiTokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HexFormat;
import java.util.List;

@Service
public class ApiTokenService {

    private static final Logger log = LoggerFactory.getLogger(ApiTokenService.class);

    private static final String TOKEN_PREFIX = "sf_";
    private static final int TOKEN_BYTES = 32;
    private static final SecureRandom RANDOM = new SecureRandom();

    private final ApiTokenRepository apiTokenRepository;

    public ApiTokenService(ApiTokenRepository apiTokenRepository) {
        this.apiTokenRepository = apiTokenRepository;
    }

    public ApiTokenResponse create(Long userId, ApiTokenRequest request) {
        log.info("Create API token: name={}, userId={}", request.name(), userId);
        byte[] raw = new byte[TOKEN_BYTES];
        RANDOM.nextBytes(raw);
        String rawToken = TOKEN_PREFIX + Base64.getUrlEncoder().withoutPadding().encodeToString(raw);
        String hash = sha256(rawToken);
        String prefix = rawToken.substring(0, 12);

        LocalDateTime expiresAt = switch (request.expiresIn() != null ? request.expiresIn() : "never") {
            case "7d" -> LocalDateTime.now().plusDays(7);
            case "30d" -> LocalDateTime.now().plusDays(30);
            case "1y" -> LocalDateTime.now().plusYears(1);
            default -> null;
        };

        var token = new ApiToken();
        token.setUserId(userId);
        token.setName(request.name());
        token.setTokenHash(hash);
        token.setTokenPrefix(prefix);
        token.setExpiresAt(expiresAt);

        apiTokenRepository.save(token);
        log.info("API token created: id={}, name={}, prefix={}", token.getId(), token.getName(), token.getTokenPrefix());

        return new ApiTokenResponse(
            token.getId(), token.getName(), rawToken,
            token.getTokenPrefix(), token.getExpiresAt(),
            token.getLastUsedAt(), token.getCreatedAt()
        );
    }

    public List<ApiTokenResponse> list(Long userId) {
        log.debug("List API tokens for userId={}", userId);
        return apiTokenRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
            .map(t -> new ApiTokenResponse(
                t.getId(), t.getName(), null,
                t.getTokenPrefix(), t.getExpiresAt(),
                t.getLastUsedAt(), t.getCreatedAt()
            ))
            .toList();
    }

    public void revoke(Long userId, Long tokenId) {
        log.info("Revoke API token: id={}, userId={}", tokenId, userId);
        var token = apiTokenRepository.findById(tokenId)
            .orElseThrow(() -> new ResourceNotFoundException("Token", tokenId));
        if (!token.getUserId().equals(userId)) {
            log.warn("API token revoke forbidden: id={}, userId={}", tokenId, userId);
            throw new BusinessException(ErrorCode.FORBIDDEN, "Token does not belong to the current user");
        }
        apiTokenRepository.delete(token);
        log.info("API token revoked: id={}", tokenId);
    }

    public java.util.Optional<ApiToken> authenticate(String rawToken) {
        if (rawToken == null || !rawToken.startsWith(TOKEN_PREFIX)) {
            log.debug("API token auth failed: invalid prefix");
            return java.util.Optional.empty();
        }
        String hash = sha256(rawToken);
        var opt = apiTokenRepository.findByTokenHash(hash);
        if (opt.isEmpty()) {
            log.debug("API token auth failed: token not found");
            return java.util.Optional.empty();
        }

        var token = opt.get();
        if (token.getExpiresAt() != null && token.getExpiresAt().isBefore(LocalDateTime.now())) {
            log.debug("API token auth failed: expired token id={}", token.getId());
            apiTokenRepository.delete(token);
            return java.util.Optional.empty();
        }

        log.debug("API token auth success: id={}, name={}", token.getId(), token.getName());
        return java.util.Optional.of(token);
    }

    private static String sha256(String input) {
        try {
            var digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes());
            return HexFormat.of().formatHex(hash);
        } catch (Exception e) {
            throw new RuntimeException("SHA-256 not available", e);
        }
    }
}
