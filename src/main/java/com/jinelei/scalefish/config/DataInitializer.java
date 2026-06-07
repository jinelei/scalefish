package com.jinelei.scalefish.config;

import com.jinelei.scalefish.entity.ApiToken;
import com.jinelei.scalefish.entity.User;
import com.jinelei.scalefish.repository.ApiTokenRepository;
import com.jinelei.scalefish.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.HexFormat;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final UserRepository userRepository;
    private final ApiTokenRepository apiTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final AppProperties appProperties;

    public DataInitializer(UserRepository userRepository,
                           ApiTokenRepository apiTokenRepository,
                           PasswordEncoder passwordEncoder,
                           AppProperties appProperties) {
        this.userRepository = userRepository;
        this.apiTokenRepository = apiTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.appProperties = appProperties;
    }

    @Override
    public void run(String... args) {
        if (appProperties.isAllowRegistration()) {
            return;
        }

        if (userRepository.count() > 0) {
            return;
        }

        AppProperties.Admin adminConfig = appProperties.getAdmin();
        String username = adminConfig.getUsername() != null ? adminConfig.getUsername() : "admin";
        String password = adminConfig.getPassword();
        if (password == null || password.isBlank()) {
            password = generateRandomPassword();
        }

        User admin = new User();
        admin.setUsername(username);
        admin.setPassword(passwordEncoder.encode(password));
        admin.setRole("ROLE_ADMIN");
        admin.setName(adminConfig.getName() != null ? adminConfig.getName() : username);
        admin.setEmail(adminConfig.getEmail());
        userRepository.save(admin);

        log.info("Initial admin account created: username={}, password={}", username, password);

        // Create default API token from config
        String rawToken = adminConfig.getToken();
        if (rawToken != null && !rawToken.isBlank()) {
            String hash = sha256(rawToken);
            String prefix = rawToken.substring(0, Math.min(12, rawToken.length()));

            var entity = new ApiToken();
            entity.setUserId(admin.getId());
            entity.setName("Default Admin Token");
            entity.setTokenHash(hash);
            entity.setTokenPrefix(prefix);
            apiTokenRepository.save(entity);

            log.info("Default API token created: prefix={}", prefix);
        }
    }

    private String generateRandomPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(16);
        for (int i = 0; i < 16; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
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
