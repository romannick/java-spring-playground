package com.nikolayr.playground.userService.service.impl;

import com.nikolayr.playground.shared.dto.response.TokenResponse;
import com.nikolayr.playground.shared.exception.UnauthorizedException;
import com.nikolayr.playground.userService.dto.request.CreateUserRequest;
import com.nikolayr.playground.userService.model.User;
import com.nikolayr.playground.userService.util.auth.JwtUtil;
import com.nikolayr.playground.userService.repository.UserRepository;
import com.nikolayr.playground.userService.service.AuthService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<User> signup(CreateUserRequest request) {
        String[] credentials = User.parseCredentials(request.getCredentials());

        return userRepository.findByEmail(credentials[0])
                .flatMap(existingUser -> Mono.<User>error(new RuntimeException("User already exists")))
                .switchIfEmpty(Mono.defer(() -> {
                    String encodedPassword = passwordEncoder.encode(credentials[1]);
                    User newUser = User.fromCreateUserRequest(request);
                    newUser.setPassword(encodedPassword);

                    return userRepository.save(newUser);
                }));
    }

    public Mono<TokenResponse> login(String credentials) {
        String[] parsedCredentials = User.parseCredentials(credentials);

        return userRepository.findByEmail(parsedCredentials[0])
                .switchIfEmpty(Mono.error(new UnauthorizedException()))
                .flatMap(user -> {
                    if (!passwordEncoder.matches(parsedCredentials[1], user.getPassword())) {
                        return Mono.error(new UnauthorizedException());
                    }

                    String accessToken = jwtUtil.generateAccessToken(user.getEmail());
                    String refreshToken = jwtUtil.generateRefreshToken(user.getEmail());

                    return Mono.just(new TokenResponse(accessToken, refreshToken));
                });
    }

    public Mono<TokenResponse> refreshAccessToken(String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) {
            return Mono.error(new UnauthorizedException());
        }

        return this.jwtUtil.validateToken(refreshToken)
                .then(Mono.defer(() -> {
                    String username = jwtUtil.extractUsername(refreshToken);
                    String newAccessToken = jwtUtil.generateAccessToken(username);
                    String newRefreshToken = jwtUtil.generateRefreshToken(username);

                    return Mono.just(new TokenResponse(newAccessToken, newRefreshToken));
                }));
    }
}