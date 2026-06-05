package com.jinelei.scalefish.config;

import com.jinelei.scalefish.entity.User;
import com.jinelei.scalefish.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AppProperties appProperties;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder,
                           AppProperties appProperties) {
        this.userRepository = userRepository;
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
}
