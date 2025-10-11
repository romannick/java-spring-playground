package com.nikolayr.playground.productService.repository;

import com.nikolayr.playground.productService.model.Product;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface ProductRepository extends ReactiveCrudRepository<Product, Long> {
    Mono<Product> findByName(String name);
}