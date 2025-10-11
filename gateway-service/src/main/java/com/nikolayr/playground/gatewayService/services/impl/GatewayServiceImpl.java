package com.nikolayr.playground.gatewayService.services.impl;

import com.nikolayr.playground.gatewayService.services.GatewayService;
import com.nikolayr.playground.shared.logging.TraceIdLoggingFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Service
public class GatewayServiceImpl implements GatewayService {
    private static final Logger log = LoggerFactory.getLogger(GatewayServiceImpl.class);
    
    private final WebClient webClient;
    private final String serviceUrlUser;
    private final String serviceUrlProduct;

    public GatewayServiceImpl(WebClient.Builder webClientBuilder,
                              @Value("${service.url.user}") String serviceUrlUser,
                              @Value("${service.url.product}") String serviceUrlProduct) {
        this.serviceUrlUser = serviceUrlUser;
        this.serviceUrlProduct = serviceUrlProduct;
        this.webClient = webClientBuilder.build();
    }

    public Mono<ResponseEntity<String>> proxyToUser(ServerWebExchange exchange, Mono<String> body) {
        return this.proxyRequest(exchange,body,this.serviceUrlUser);
    }

    public Mono<ResponseEntity<String>> proxyAuthToUser(ServerWebExchange exchange, Mono<String> body) {
        return this.proxyRequest(exchange,body,this.serviceUrlUser);
    }

    public Mono<ResponseEntity<String>> proxyToProduct(ServerWebExchange exchange, Mono<String> body) {
        return this.proxyRequest(exchange,body,this.serviceUrlProduct);
    }

    private Mono<ResponseEntity<String>> proxyRequest(ServerWebExchange exchange, Mono<String> body, String target) {
        return Mono.deferContextual(ctx -> {
            log.info("test");
            String traceId = ctx.getOrDefault(TraceIdLoggingFilter.TRACE_ID_HEADER, "unknown-trace");

            String originalPath = exchange.getRequest().getURI().getRawPath();
            String path = originalPath.replaceFirst("/api", target);
            String query = exchange.getRequest().getURI().getRawQuery();
            String targetUri = path + (query != null ? "?" + query : "");

            HttpMethod method = exchange.getRequest().getMethod();

            return webClient
                    .method(method)
                    .uri(targetUri)
                    .headers(headers -> {
                        headers.addAll(exchange.getRequest().getHeaders());
                        headers.add(TraceIdLoggingFilter.TRACE_ID_HEADER, traceId);
                    })
                    .body(body != null ? body : Mono.empty(), String.class)
                    .exchangeToMono(response -> response.toEntity(String.class));
        });
    }
}
