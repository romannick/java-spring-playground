package com.nikolayr.playground.productService.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateProductRequest {
    @NotBlank
    private final String name;

    @NotBlank
    private final double price;
}