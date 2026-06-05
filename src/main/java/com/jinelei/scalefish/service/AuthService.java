package com.jinelei.scalefish.service;

import com.jinelei.scalefish.config.AppProperties;
import com.jinelei.scalefish.dto.AuthResponse;
import com.jinelei.scalefish.dto.LoginRequest;
import com.jinelei.scalefish.dto.RefreshRequest;
import com.jinelei.scalefish.dto.RegisterRequest;
import com.jinelei.scalefish.entity.RefreshToken;
import com.jinelei.scalefish.entity.User;
import com.jinelei.scalefish.exception.BusinessException;
import com.jinelei.scalefish.exception.DuplicateResourceException;
import com.jinelei.scalefish.exception.ErrorCode;
import com.jinelei.scalefish.exception.ResourceNotFoundException;
import com.jinelei.scalefish.repository.RefreshTokenRepository;
import com.jinelei.scalefish.repository.UserRepository;
import com.jinelei.scalefish.security.JwtTokenProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final AppProperties appProperties;

    public AuthService(UserRepository userRepository, RefreshTokenRepository refreshTokenRepository,
                       JwtTokenProvider jwtTokenProvider, PasswordEncoder passwordEncoder,
                       AppProperties appProperties) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
        this.appProperties = appProperties;
    }

    public boolean isRegistrationAllowed() {
        return appProperties.isAllowRegistration() || userRepository.count() == 0;
    }

    @Transactional
    public AuthResponse login(LoginRequest req) {
        User user = userRepository.findByUsername(req.username())
            .orElseThrow(() -> new BadCredentialsException("用户名或密码错误"));

        if (!passwordEncoder.matches(req.password(), user.getPassword())) {
            throw new BadCredentialsException("用户名或密码错误");
        }

        return createAuthResponse(user);
    }

    @Transactional
    public AuthResponse register(RegisterRequest req) {
        if (!isRegistrationAllowed()) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "管理员已关闭注册");
        }

        if (userRepository.existsByUsername(req.username())) {
            throw new DuplicateResourceException("User", req.username());
        }

        boolean isFirst = userRepository.count() == 0;

        User user = new User();
        user.setUsername(req.username());
        user.setPassword(passwordEncoder.encode(req.password()));
        user.setRole(isFirst ? "ROLE_ADMIN" : "ROLE_USER");
        user.setName(req.name() != null ? req.name() : req.username());
        user.setEmail(req.email());
        userRepository.save(user);

        return createAuthResponse(user);
    }

    @Transactional
    public AuthResponse refresh(RefreshRequest req) {
        RefreshToken stored = refreshTokenRepository.findByToken(req.refreshToken())
            .orElseThrow(() -> new BadCredentialsException("无效的刷新令牌"));

        if (stored.isExpired()) {
            refreshTokenRepository.delete(stored);
            throw new BadCredentialsException("刷新令牌已过期");
        }

        if (!jwtTokenProvider.validateRefreshToken(req.refreshToken())) {
            refreshTokenRepository.delete(stored);
            throw new BadCredentialsException("无效的刷新令牌");
        }

        Long userId = jwtTokenProvider.getUserIdFromRefreshToken(req.refreshToken());
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User", userId));

        refreshTokenRepository.delete(stored);
        return createAuthResponse(user);
    }

    @Transactional
    public void logout(Long userId) {
        refreshTokenRepository.deleteByUserId(userId);
    }

    public AuthResponse.UserInfo me(User user) {
        return new AuthResponse.UserInfo(user.getId(), user.getUsername(), user.getName(), user.getEmail());
    }

    private AuthResponse createAuthResponse(User user) {
        String accessToken = jwtTokenProvider.generateAccessToken(user.getId(), user.getUsername());
        String refreshTokenStr = jwtTokenProvider.generateRefreshToken(user.getId());

        RefreshToken rt = new RefreshToken();
        rt.setToken(refreshTokenStr);
        rt.setUser(user);
        rt.setExpiresAt(LocalDateTime.now().plusSeconds(jwtTokenProvider.getAccessExpiration() * 30));
        refreshTokenRepository.save(rt);

        var userInfo = new AuthResponse.UserInfo(user.getId(), user.getUsername(), user.getName(), user.getEmail());
        return new AuthResponse(accessToken, refreshTokenStr, jwtTokenProvider.getAccessExpiration() / 1000, userInfo);
    }
}
