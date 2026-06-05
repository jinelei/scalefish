package com.jinelei.scalefish.security;

import com.jinelei.scalefish.entity.User;
import com.jinelei.scalefish.repository.UserRepository;
import com.jinelei.scalefish.service.ApiTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final ApiTokenService apiTokenService;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider,
                                    UserRepository userRepository,
                                    ApiTokenService apiTokenService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
        this.apiTokenService = apiTokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header != null && header.startsWith(BEARER_PREFIX)) {
            String token = header.substring(BEARER_PREFIX.length());

            User user = tryJwt(token);
            if (user == null) {
                user = tryApiToken(token);
            }

            if (user != null) {
                var auth = new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
        chain.doFilter(request, response);
    }

    private User tryJwt(String token) {
        if (jwtTokenProvider.validateAccessToken(token)) {
            Long userId = jwtTokenProvider.getUserIdFromAccessToken(token);
            return userRepository.findById(userId).orElse(null);
        }
        return null;
    }

    private User tryApiToken(String token) {
        return apiTokenService.authenticate(token)
            .flatMap(t -> userRepository.findById(t.getUserId()))
            .orElse(null);
    }
}
