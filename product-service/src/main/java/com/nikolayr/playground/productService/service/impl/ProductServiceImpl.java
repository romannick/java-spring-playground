package com.nikolayr.playground.productService.service.impl;

import com.nikolayr.playground.productService.dto.request.CreateProductRequest;
import com.nikolayr.playground.productService.model.Product;
import com.nikolayr.playground.productService.repository.ProductRepository;
import com.nikolayr.playground.productService.service.ProductService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Mono<Product> create(CreateProductRequest request) {
        return productRepository.findByName(request.getName())
                .flatMap(existingProduct -> Mono.<Product>error(new RuntimeException("Product already exists")))
                .switchIfEmpty(productRepository.save(Product.fromCreateProductRequest(request)));
    }

    @Override
    public Mono<Product> getById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public Mono<Product> update(Long id, String name, Double price) {
        return productRepository.findById(id)
                .flatMap(product -> {
                    if (name != null) {
                        product.setName(name);
                    }
                    if (price != null) {
                        product.setPrice(price);
                    }

                    return productRepository.save(product);
                });
    }

    @Override
    public Flux<Product> getAll(int page, int size) {
        return productRepository.findAll()
                .skip((long) page * size)
                .take(size);
    }
}