package com.nikolayr.playground.shared.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    public Long id;
    public String email;
    public String firstName;
    public String lastName;
}
