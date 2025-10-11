package com.nikolayr.playground.shared.logging;

import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
@Order(-1)
public class TraceIdLoggingFilter implements WebFilter {
    public static final String TRACE_ID_HEADER = "X-Trace-Id";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String traceId = exchange.getRequest().getHeaders().getFirst(TRACE_ID_HEADER);

        if (traceId == null || traceId.isEmpty()) {
            traceId = UUID.randomUUID().toString();
        }

        exchange.getResponse().getHeaders().add(TRACE_ID_HEADER, traceId);

        String method = exchange.getRequest().getMethod() != null
                ? exchange.getRequest().getMethod().name()
                : "UNKNOWN";
        String path = exchange.getRequest().getPath().value();
        String query = exchange.getRequest().getQueryParams().toString();

        System.out.println("Incoming request: [" + traceId + "] " + method + " " + path + " " + query);

        String finalTraceId = traceId;
        return chain.filter(exchange)
                .doOnEach(signal -> MDC.put("traceId", finalTraceId))
                .doFinally(signalType -> {
                    int status = exchange.getResponse().getStatusCode() != null
                            ? exchange.getResponse().getStatusCode().value()
                            : 200;
                    System.out.println("Completed request: [" + finalTraceId + "] " + method + " " + path + " Status: " + status);

                    MDC.remove("traceId");
                })
                .contextWrite(ctx -> ctx.put(TRACE_ID_HEADER, finalTraceId));

    }
}