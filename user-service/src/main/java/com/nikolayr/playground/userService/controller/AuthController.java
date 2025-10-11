package com.nikolayr.playground.userService.controller;

import com.nikolayr.playground.shared.dto.request.AuthRequest;
import com.nikolayr.playground.shared.dto.response.TokenResponse;
import com.nikolayr.playground.userService.dto.request.CreateUserRequest;
import com.nikolayr.playground.userService.dto.request.RefreshTokenRequest;
import com.nikolayr.playground.userService.model.User;
import com.nikolayr.playground.userService.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<User> createUser(@Valid @RequestBody CreateUserRequest user) {
        return authService.signup(user);
    }

    @PostMapping("/login")
    public Mono<TokenResponse> login(@Valid @RequestBody AuthRequest request) {
        return this.authService.login(request.getCredentials());
    }

    @PostMapping("/refresh")
    public Mono<TokenResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        return this.authService.refreshAccessToken(request.getRefreshToken());
    }
}