package com.nikolayr.playground.productService.model;

import com.nikolayr.playground.productService.dto.request.CreateProductRequest;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("Products")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Product {
    @Id
    private Long id;
    private String name;
    private double price;

    public static Product fromCreateProductRequest(CreateProductRequest request) {
        Product product = new Product();

        product.setName(request.getName());
        product.setPrice(request.getPrice());

        return product;
    }
}