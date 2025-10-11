package com.nikolayr.playground.productService.service;

import com.nikolayr.playground.productService.dto.request.CreateProductRequest;
import com.nikolayr.playground.productService.model.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductService {
    Mono<Product> create(CreateProductRequest request);

    Mono<Product> getById(Long id);

    Mono<Product> update(Long id, String name, Double price);

    Flux<Product> getAll(int page, int size);
}
