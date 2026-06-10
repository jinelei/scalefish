package com.jinelei.scalefish.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private static final Logger log = LoggerFactory.getLogger(JwtTokenProvider.class);

    private final SecretKey accessKey;
    private final SecretKey refreshKey;
    private final long accessExpiration;
    private final long refreshExpiration;

    public JwtTokenProvider(
            @Value("${jwt.access-secret}") String accessSecret,
            @Value("${jwt.refresh-secret}") String refreshSecret,
            @Value("${jwt.access-expiration}") long accessExpiration,
            @Value("${jwt.refresh-expiration}") long refreshExpiration) {
        this.accessKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessSecret));
        this.refreshKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(refreshSecret));
        this.accessExpiration = accessExpiration;
        this.refreshExpiration = refreshExpiration;
        log.info("JwtTokenProvider initialized: accessExpiration={}ms, refreshExpiration={}ms", accessExpiration, refreshExpiration);
    }

    public String generateAccessToken(Long userId, String username) {
        Date now = new Date();
        String token = Jwts.builder()
                .subject(userId.toString())
                .claim("username", username)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + accessExpiration))
                .signWith(accessKey)
                .compact();
        log.debug("Access token generated for userId={}", userId);
        return token;
    }

    public String generateRefreshToken(Long userId) {
        Date now = new Date();
        String token = Jwts.builder()
                .subject(userId.toString())
                .issuedAt(now)
                .expiration(new Date(now.getTime() + refreshExpiration))
                .signWith(refreshKey)
                .compact();
        log.debug("Refresh token generated for userId={}", userId);
        return token;
    }

    public Long getUserIdFromAccessToken(String token) {
        return Long.parseLong(parseAccessClaims(token).getSubject());
    }

    public Long getUserIdFromRefreshToken(String token) {
        return Long.parseLong(parseRefreshClaims(token).getSubject());
    }

    public boolean validateAccessToken(String token) {
        try {
            parseAccessClaims(token);
            return true;
        } catch (Exception e) {
            log.debug("Access token validation failed: {}", e.getMessage());
            return false;
        }
    }

    public boolean validateRefreshToken(String token) {
        try {
            parseRefreshClaims(token);
            return true;
        } catch (Exception e) {
            log.debug("Refresh token validation failed: {}", e.getMessage());
            return false;
        }
    }

    public long getAccessExpiration() {
        return accessExpiration;
    }

    public long getRefreshExpiration() {
        return refreshExpiration;
    }

    private Claims parseAccessClaims(String token) {
        return Jwts.parser().verifyWith(accessKey).build().parseSignedClaims(token).getPayload();
    }

    private Claims parseRefreshClaims(String token) {
        return Jwts.parser().verifyWith(refreshKey).build().parseSignedClaims(token).getPayload();
    }
}
