package com.nikolayr.playground.productService.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateProductRequest {
    private final String name;
    private final Double price;
}