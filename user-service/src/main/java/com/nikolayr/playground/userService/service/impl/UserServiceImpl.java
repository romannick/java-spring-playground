package com.nikolayr.playground.userService.service.impl;

import com.nikolayr.playground.shared.dto.response.UserDto;
import com.nikolayr.playground.userService.model.User;
import com.nikolayr.playground.userService.repository.UserRepository;
import com.nikolayr.playground.userService.service.UserService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Mono<UserDto> getById(Long id) {
        return userRepository
                .findById(id)
                .map(this::toDto);
    }

    @Override
    public Mono<UserDto> update(Long id, String firstName, String lastName) {
        return userRepository.findById(id)
                .flatMap(user -> {
                    if (firstName != null) {
                        user.setFirstName(firstName);
                    }
                    if (lastName != null) {
                        user.setLastName(lastName);
                    }

                    return userRepository.save(user);
                })
                .map(this::toDto);
    }

    @Override
    public Flux<UserDto> getAll(int page, int size) {
        return userRepository.findAll()
                .skip((long) page * size)
                .take(size)
                .map(this::toDto);
    }

    private UserDto toDto(User user) {
        return new UserDto(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName()
        );
    }
}