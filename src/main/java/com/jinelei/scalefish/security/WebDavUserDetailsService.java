package com.jinelei.scalefish.security;

import com.jinelei.scalefish.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class WebDavUserDetailsService implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(WebDavUserDetailsService.class);

    private final UserRepository userRepository;

    public WebDavUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Loading WebDAV user: {}", username);
        var user = userRepository.findByUsername(username)
            .orElseThrow(() -> {
                log.warn("WebDAV authentication failed: user '{}' not found", username);
                return new UsernameNotFoundException("User not found: " + username);
            });
        log.debug("WebDAV user '{}' loaded successfully", username);
        return new User(user.getUsername(), user.getPassword(), Collections.emptyList());
    }
}
