package com.nikolayr.playground.gatewayService.controller;

import com.nikolayr.playground.gatewayService.services.GatewayService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
public class GatewayController {

    private final GatewayService gatewayService;

    public GatewayController(GatewayService gatewayService) {
        this.gatewayService = gatewayService;
    }

    @RequestMapping("/users/**")
    public Mono<ResponseEntity<String>> proxyToUser(ServerWebExchange exchange,
                                                   @RequestBody(required = false) Mono<String> body) {
        return gatewayService.proxyToUser(exchange, body);
    }

    @RequestMapping("/auth/**")
    public Mono<ResponseEntity<String>> proxyAuthToUser(ServerWebExchange exchange,
                                                    @RequestBody(required = false) Mono<String> body) {
        return gatewayService.proxyAuthToUser(exchange, body);
    }

    @RequestMapping("/products/**")
    public Mono<ResponseEntity<String>> proxyToProduct(ServerWebExchange exchange,
                                                    @RequestBody(required = false) Mono<String> body) {
        return gatewayService.proxyToProduct(exchange, body);
    }
}
