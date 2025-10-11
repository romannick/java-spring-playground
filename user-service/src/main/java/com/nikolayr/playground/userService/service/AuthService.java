package com.nikolayr.playground.userService.service;

import com.nikolayr.playground.shared.dto.response.TokenResponse;
import com.nikolayr.playground.userService.dto.request.CreateUserRequest;
import com.nikolayr.playground.userService.model.User;
import reactor.core.publisher.Mono;

public interface AuthService {
    Mono<User> signup(CreateUserRequest request);

    Mono<TokenResponse> login(String credentials);

    Mono<TokenResponse> refreshAccessToken(String refreshToken);
}