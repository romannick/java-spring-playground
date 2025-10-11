package com.nikolayr.playground.userService.controller;

import com.nikolayr.playground.shared.dto.response.UserDto;
import com.nikolayr.playground.shared.exception.BadRequestException;
import com.nikolayr.playground.shared.exception.EntityNotFoundException;
import com.nikolayr.playground.shared.exception.InvalidBodyAtLeastOneFieldException;
import com.nikolayr.playground.userService.dto.request.CreateUserRequest;
import com.nikolayr.playground.userService.dto.request.UpdateUserRequest;
import com.nikolayr.playground.userService.model.User;
import com.nikolayr.playground.userService.service.UserService;
import com.nikolayr.playground.userService.util.Constants;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public Mono<UserDto> getUser(@PathVariable Long id) {
        return userService.getById(id)
                .switchIfEmpty(Mono.error(new EntityNotFoundException(Constants.UserNotFoundMessage)));
    }

    @PutMapping("/{id}")
    public Mono<UserDto> updateUser(
            @PathVariable Long id,
            @RequestBody UpdateUserRequest request) {
        if (request.getFirstName() == null && request.getLastName() == null) {
            return Mono.error(new InvalidBodyAtLeastOneFieldException(List.of("firstName", "lastName")));
        }

        return userService.update(id, request.getFirstName(), request.getLastName())
                .switchIfEmpty(Mono.error(new EntityNotFoundException(Constants.UserNotFoundMessage)));
    }

    @GetMapping
    public Flux<UserDto> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        if (page < 0 || size <= 0) {
            return Flux.error(new BadRequestException(Constants.InvalidPaginationMessage));
        }

        return userService.getAll(page, size);
    }
}