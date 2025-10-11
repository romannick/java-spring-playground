package com.nikolayr.playground.gatewayService.services;

import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public interface GatewayService {
    Mono<ResponseEntity<String>> proxyToUser(ServerWebExchange exchange, Mono<String> body);

    Mono<ResponseEntity<String>> proxyAuthToUser(ServerWebExchange exchange, Mono<String> body);

    Mono<ResponseEntity<String>> proxyToProduct(ServerWebExchange exchange, Mono<String> body);
}
