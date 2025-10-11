package com.nikolayr.playground.userService.controller;

import com.nikolayr.playground.shared.dto.response.MessageResponse;
import io.r2dbc.spi.Connection;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/health")
public class HealthController {
    private final ConnectionFactory connectionFactory;

    public HealthController(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @GetMapping
    public Mono<ResponseEntity<MessageResponse>> healthCheck() {
        return Mono.usingWhen(
                connectionFactory.create(),
                conn -> Mono.from(
                        Flux.from(conn.createStatement("SELECT 1").execute())
                                .flatMap(result -> Flux.from(result.map((row, meta) -> row.get(0))))
                                .then()
                ).then(Mono.just(ResponseEntity.ok(new MessageResponse("UP")))),
                Connection::close
        ).onErrorResume(ex -> Mono.just(
                ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                        .body(new MessageResponse("DOWN: " + ex.getMessage()))
        ));
    }
}