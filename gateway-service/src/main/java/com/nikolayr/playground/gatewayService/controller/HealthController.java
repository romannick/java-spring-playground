package com.nikolayr.playground.gatewayService.controller;

import com.nikolayr.playground.shared.dto.response.MessageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/health")
public class HealthController {
    @GetMapping
    public Mono<ResponseEntity<MessageResponse>> healthCheck() {
        return Mono.just(ResponseEntity.ok(new MessageResponse("UP")));
    }
}