package com.jinelei.scalefish.security;

import com.jinelei.scalefish.repository.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class WebDavUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public WebDavUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        return new User(user.getUsername(), user.getPassword(), Collections.emptyList());
    }
}
