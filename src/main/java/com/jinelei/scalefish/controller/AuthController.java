package com.jinelei.scalefish.controller;

import com.jinelei.scalefish.dto.AuthResponse;
import com.jinelei.scalefish.dto.GenericResult;
import com.jinelei.scalefish.dto.LoginRequest;
import com.jinelei.scalefish.dto.RefreshRequest;
import com.jinelei.scalefish.dto.RegisterRequest;
import com.jinelei.scalefish.entity.User;
import com.jinelei.scalefish.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Tag(name = "认证管理")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "登录")
    @PostMapping("/login")
    public GenericResult<AuthResponse> login(@Valid @RequestBody LoginRequest req) {
        return GenericResult.success(authService.login(req));
    }

    @Operation(summary = "注册", description = "第一个注册用户自动成为管理员")
    @PostMapping("/register")
    public GenericResult<AuthResponse> register(@Valid @RequestBody RegisterRequest req) {
        return GenericResult.success(authService.register(req));
    }

    @Operation(summary = "刷新令牌")
    @PostMapping("/refresh")
    public GenericResult<AuthResponse> refresh(@Valid @RequestBody RefreshRequest req) {
        return GenericResult.success(authService.refresh(req));
    }

    @Operation(summary = "退出登录")
    @PostMapping("/logout")
    public GenericResult<Void> logout(@AuthenticationPrincipal User user) {
        if (user != null) authService.logout(user.getId());
        return GenericResult.success(null);
    }

    @Operation(summary = "查询注册状态")
    @GetMapping("/registration-status")
    public GenericResult<Map<String, Object>> registrationStatus() {
        return GenericResult.success(Map.of("allowRegistration", authService.isRegistrationAllowed()));
    }

    @Operation(summary = "获取当前用户信息")
    @GetMapping("/me")
    public GenericResult<AuthResponse.UserInfo> me(@AuthenticationPrincipal User user) {
        return GenericResult.success(authService.me(user));
    }
}
