package com.nikolayr.playground.userService.model;

import com.nikolayr.playground.shared.utils.StringUtils;
import com.nikolayr.playground.userService.dto.request.CreateUserRequest;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("Users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {
    @Id
    private Long id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;

    public static User fromCreateUserRequest(CreateUserRequest request) {
        User user = new User();
        String[] credentials = User.parseCredentials(request.getCredentials());

        user.setEmail(credentials[0]);
        user.setPassword(credentials[1]);
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());

        return user;
    }

    public static String[] parseCredentials(String credentials) {
        String CREDENTIALS_SEPARATOR = ":";

        String decodedCredentials = StringUtils.decodeBase64(credentials);
        String[] splitCredentials = decodedCredentials.split(CREDENTIALS_SEPARATOR);

        String email = splitCredentials[0];
        String password = splitCredentials[1];

        return new String[]{email, password};
    }
}