package com.nikolayr.playground.userService.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserRequest {
    @NotBlank
    private String credentials;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;
}