package com.nikolayr.playground.userService.service;

import com.nikolayr.playground.shared.dto.response.UserDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<UserDto> getById(Long id);

    Mono<UserDto> update(Long id, String firstName, String lastName);

    Flux<UserDto> getAll(int page, int size);
}
