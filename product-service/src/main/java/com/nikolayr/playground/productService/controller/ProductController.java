package com.nikolayr.playground.productService.controller;

import com.nikolayr.playground.productService.dto.request.CreateProductRequest;
import com.nikolayr.playground.productService.dto.request.UpdateProductRequest;
import com.nikolayr.playground.productService.model.Product;
import com.nikolayr.playground.productService.service.ProductService;
import com.nikolayr.playground.shared.exception.BadRequestException;
import com.nikolayr.playground.shared.exception.EntityNotFoundException;
import com.nikolayr.playground.shared.exception.InvalidBodyAtLeastOneFieldException;
import com.nikolayr.playground.productService.util.Constants;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Product> createProduct(@Valid @RequestBody CreateProductRequest request) {
        return productService.create(request);
    }

    @GetMapping("/{id}")
    public Mono<Product> getProduct(@PathVariable Long id) {
        return productService.getById(id)
                .switchIfEmpty(Mono.error(new EntityNotFoundException(Constants.ProductNotFoundMessage)));
    }

    @PutMapping("/{id}")
    public Mono<Product> updateProduct(
            @PathVariable Long id,
            @RequestBody UpdateProductRequest request) {
        if (request.getName() == null && request.getPrice() == null) {
            return Mono.error(new InvalidBodyAtLeastOneFieldException(List.of("name", "price")));
        }

        return productService.update(id, request.getName(), request.getPrice())
                .switchIfEmpty(Mono.error(new EntityNotFoundException(Constants.ProductNotFoundMessage)));
    }

    @GetMapping
    public Flux<Product> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        if (page < 0 || size <= 0) {
            return Flux.error(new BadRequestException(Constants.InvalidPaginationMessage));
        }

        return productService.getAll(page, size);
    }
}