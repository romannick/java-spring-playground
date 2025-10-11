package com.nikolayr.playground.userService.service.impl;

import com.nikolayr.playground.userService.repository.UserRepository;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CustomReactiveUserDetailsService implements ReactiveUserDetailsService {
    private final UserRepository userRepository;

    public CustomReactiveUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return userRepository.findByEmail(username)
                .map(u -> User
                        .withUsername(u.getEmail())
                        .password(u.getPassword())
                        .authorities("USER")
                        .build()
                );
    }
}