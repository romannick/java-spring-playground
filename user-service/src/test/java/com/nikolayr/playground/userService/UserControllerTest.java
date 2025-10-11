package com.nikolayr.playground.userService;

import com.nikolayr.playground.shared.dto.response.GenericErrorResponse;
import com.nikolayr.playground.userService.dto.request.UpdateUserRequest;
import com.nikolayr.playground.userService.model.User;
import com.nikolayr.playground.userService.service.UserService;
import com.nikolayr.playground.userService.util.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureWebTestClient
class UserControllerTest {
    @Autowired
    private WebTestClient webClient;

    @MockBean
    private UserService userService;

    private User sampleUser;

    @BeforeEach
    void setUp() {
        sampleUser = new User(1L, "John", "test", "Doe", "john.doe@example.com");
    }

//    @Test
//    void createUser_success() {
//        CreateUserRequest request = new CreateUserRequest("John", "Doe", "john.doe@example.com");
//
//        Mockito.when(userService.create(request)).thenReturn(Mono.just(sampleUser));
//
//        webClient.post()
//                .uri("/users")
//                .contentType(MediaType.APPLICATION_JSON)
//                .bodyValue(request)
//                .exchange()
//                .expectStatus().isCreated()
//                .expectBody(User.class)
//                .isEqualTo(sampleUser);
//    }

    @Test
    void getUser_success() {
        Mockito.when(userService.getById(1L)).thenReturn(Mono.just(sampleUser));

        webClient.get()
                .uri("/users/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(User.class)
                .isEqualTo(sampleUser);
    }

    @Test
    void getUser_notFound() {
        Mockito.when(userService.getById(2L)).thenReturn(Mono.empty());

        webClient.get()
                .uri("/users/2")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void updateUser_success() {
        UpdateUserRequest request = new UpdateUserRequest("Jane", null);

        User updatedUser = new User(1L, "Jane", "Doe", "john.doe@example.com");
        Mockito.when(userService.update(1L, "Jane", null)).thenReturn(Mono.just(updatedUser));

        webClient.put()
                .uri("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(User.class)
                .isEqualTo(updatedUser);
    }

    @Test
    void updateUser_invalidBody() {
        UpdateUserRequest request = new UpdateUserRequest(null, null);

        webClient.put()
                .uri("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void getAllUsers_success() {
        Mockito.when(userService.getAll(0, 10)).thenReturn(Flux.just(sampleUser));

        webClient.get()
                .uri("/users?page=0&size=10")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(User.class)
                .hasSize(1)
                .contains(sampleUser);
    }

    @Test
    void getAllUsers_invalidPaginationPageParam() {
        webClient.get()
                .uri("/users?page=-1&size=10")
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(GenericErrorResponse.class)
                .consumeWith(response -> {
                    GenericErrorResponse body = response.getResponseBody();

                    assertNotNull(body);
                    assertEquals(Constants.InvalidPaginationMessage, body.getError());
                });
    }

    @Test
    void getAllUsers_invalidPaginationSizeParam() {
        webClient.get()
                .uri("/users?page=10&size=-1")
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(GenericErrorResponse.class)
                .consumeWith(response -> {
                    GenericErrorResponse body = response.getResponseBody();

                    assertNotNull(body);
                    assertEquals(Constants.InvalidPaginationMessage, body.getError());
                });
    }
}
