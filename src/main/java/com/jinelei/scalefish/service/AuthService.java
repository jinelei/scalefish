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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

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
        boolean allowed = appProperties.isAllowRegistration();
        log.debug("Registration allowed: {}", allowed);
        return allowed;
    }

    @Transactional
    public AuthResponse login(LoginRequest req) {
        log.info("Login attempt: username={}", req.username());
        User user = userRepository.findByUsername(req.username())
            .orElseThrow(() -> {
                log.warn("Login failed: user not found: {}", req.username());
                return new BadCredentialsException("用户名或密码错误");
            });

        if (!passwordEncoder.matches(req.password(), user.getPassword())) {
            log.warn("Login failed: wrong password for user: {}", req.username());
            throw new BadCredentialsException("用户名或密码错误");
        }

        log.info("Login success: userId={}, username={}", user.getId(), user.getUsername());
        return createAuthResponse(user);
    }

    @Transactional
    public AuthResponse register(RegisterRequest req) {
        log.info("Register attempt: username={}", req.username());
        if (!isRegistrationAllowed()) {
            log.warn("Registration blocked: registration disabled");
            throw new BusinessException(ErrorCode.FORBIDDEN, "管理员已关闭注册");
        }

        if (userRepository.existsByUsername(req.username())) {
            log.warn("Registration failed: duplicate username: {}", req.username());
            throw new DuplicateResourceException("User", req.username());
        }

        boolean isFirst = userRepository.count() == 0;
        log.debug("First user: {}", isFirst);

        User user = new User();
        user.setUsername(req.username());
        user.setPassword(passwordEncoder.encode(req.password()));
        user.setRole(isFirst ? "ROLE_ADMIN" : "ROLE_USER");
        user.setName(req.name() != null ? req.name() : req.username());
        user.setEmail(req.email());
        userRepository.save(user);

        log.info("Register success: userId={}, username={}, role={}", user.getId(), user.getUsername(), user.getRole());
        return createAuthResponse(user);
    }

    @Transactional
    public AuthResponse refresh(RefreshRequest req) {
        log.debug("Token refresh attempt");
        RefreshToken stored = refreshTokenRepository.findByToken(req.refreshToken())
            .orElseThrow(() -> {
                log.warn("Token refresh failed: token not found");
                return new BadCredentialsException("无效的刷新令牌");
            });

        if (stored.isExpired()) {
            log.warn("Token refresh failed: token expired for user: {}", stored.getUser().getId());
            refreshTokenRepository.delete(stored);
            throw new BadCredentialsException("刷新令牌已过期");
        }

        if (!jwtTokenProvider.validateRefreshToken(req.refreshToken())) {
            log.warn("Token refresh failed: invalid token for user: {}", stored.getUser().getId());
            refreshTokenRepository.delete(stored);
            throw new BadCredentialsException("无效的刷新令牌");
        }

        Long userId = jwtTokenProvider.getUserIdFromRefreshToken(req.refreshToken());
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User", userId));

        refreshTokenRepository.delete(stored);
        log.info("Token refresh success: userId={}", userId);
        return createAuthResponse(user);
    }

    @Transactional
    public void logout(Long userId) {
        log.info("User logout: userId={}", userId);
        refreshTokenRepository.deleteByUserId(userId);
    }

    public AuthResponse.UserInfo me(User user) {
        log.debug("Get user info: userId={}", user.getId());
        return new AuthResponse.UserInfo(user.getId(), user.getUsername(), user.getName(), user.getEmail());
    }

    @Transactional
    public AuthResponse.UserInfo updateProfile(Long userId, String name, String email) {
        log.info("Update profile: userId={}", userId);
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User", userId));
        if (name != null) user.setName(name);
        if (email != null) user.setEmail(email);
        userRepository.save(user);
        log.info("Profile updated: userId={}, name={}, email={}", userId, user.getName(), user.getEmail());
        return new AuthResponse.UserInfo(user.getId(), user.getUsername(), user.getName(), user.getEmail());
    }

    @Transactional
    public void changePassword(Long userId, String oldPassword, String newPassword) {
        log.info("Change password: userId={}", userId);
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User", userId));

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            log.warn("Change password failed: wrong old password for userId={}", userId);
            throw new BadCredentialsException("当前密码错误");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        refreshTokenRepository.deleteByUserId(userId);
        log.info("Password changed for userId={}", userId);
    }

    private AuthResponse createAuthResponse(User user) {
        String accessToken = jwtTokenProvider.generateAccessToken(user.getId(), user.getUsername());
        String refreshTokenStr = jwtTokenProvider.generateRefreshToken(user.getId());

        RefreshToken rt = new RefreshToken();
        rt.setToken(refreshTokenStr);
        rt.setUser(user);
        rt.setExpiresAt(LocalDateTime.now().plusSeconds(jwtTokenProvider.getRefreshExpiration() / 1000));
        refreshTokenRepository.save(rt);

        var userInfo = new AuthResponse.UserInfo(user.getId(), user.getUsername(), user.getName(), user.getEmail());
        return new AuthResponse(accessToken, refreshTokenStr, jwtTokenProvider.getAccessExpiration() / 1000, userInfo);
    }
}
