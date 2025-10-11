package com.nikolayr.playground.shared.config;

import io.opentracing.Tracer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JaegerConfig {
    private final String jaegerHttpSenderUrl;
    private final String springApplicationName;

    public JaegerConfig(
            @Value("${spring.jaeger.http-sender.url}") String jaegerHttpSenderUrl,
            @Value("${spring.application.name}") String springApplicationName
    ) {
        this.jaegerHttpSenderUrl = jaegerHttpSenderUrl;
        this.springApplicationName = springApplicationName;
    }

    @Bean
    public Tracer jaegerTracer() {
        return new io.jaegertracing.Configuration(this.springApplicationName)
                .withSampler(
                        new io.jaegertracing.Configuration.SamplerConfiguration()
                                .withType("const")
                                .withParam(1))
                .withReporter(
                        new io.jaegertracing.Configuration.ReporterConfiguration()
                                .withLogSpans(true)
                                .withSender(
                                        new io.jaegertracing.Configuration.SenderConfiguration()
                                                .withEndpoint(this.jaegerHttpSenderUrl)))
                .getTracer();
    }
}
