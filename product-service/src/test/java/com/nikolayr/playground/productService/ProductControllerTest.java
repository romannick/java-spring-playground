package com.nikolayr.playground.productService;

import com.nikolayr.playground.productService.dto.request.CreateProductRequest;
import com.nikolayr.playground.productService.dto.request.UpdateProductRequest;
import com.nikolayr.playground.productService.model.Product;
import com.nikolayr.playground.productService.service.ProductService;
import com.nikolayr.playground.shared.dto.response.GenericErrorResponse;
import com.nikolayr.playground.productService.util.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureWebTestClient
class ProductControllerTest {
    @Autowired
    private WebTestClient webClient;

    @MockBean
    private ProductService productService;

    private Product sampleProduct;

    @BeforeEach
    void setUp() {
        sampleProduct = new Product(1L, "Fresh Pasta", 1.99);
    }

    @Test
    void createProduct_success() {
        CreateProductRequest request = new CreateProductRequest("Fresh Pasta", 1.99);

        Mockito.when(productService.create(request)).thenReturn(Mono.just(sampleProduct));

        webClient.post()
                .uri("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Product.class)
                .isEqualTo(sampleProduct);
    }

    @Test
    void getProduct_success() {
        Mockito.when(productService.getById(1L)).thenReturn(Mono.just(sampleProduct));

        webClient.get()
                .uri("/products/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Product.class)
                .isEqualTo(sampleProduct);
    }

    @Test
    void getProduct_notFound() {
        Mockito.when(productService.getById(2L)).thenReturn(Mono.empty());

        webClient.get()
                .uri("/products/2")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void updateProduct_success() {
        UpdateProductRequest request = new UpdateProductRequest("Old Pasta", null);

        Product updatedProduct = new Product(1L, "Old Pasta", 1.99);
        Mockito.when(productService.update(1L, "Old Pasta", null)).thenReturn(Mono.just(updatedProduct));

        webClient.put()
                .uri("/products/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Product.class)
                .isEqualTo(updatedProduct);
    }

    @Test
    void updateProduct_invalidBody() {
        UpdateProductRequest request = new UpdateProductRequest(null, null);

        webClient.put()
                .uri("/products/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void getAllProducts_success() {
        Mockito.when(productService.getAll(0, 10)).thenReturn(Flux.just(sampleProduct));

        webClient.get()
                .uri("/products?page=0&size=10")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Product.class)
                .hasSize(1)
                .contains(sampleProduct);
    }

    @Test
    void getAllProducts_invalidPaginationPageParam() {
        webClient.get()
                .uri("/products?page=-1&size=10")
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(GenericErrorResponse.class)
                .consumeWith(response -> {
                    GenericErrorResponse body = response.getResponseBody();

                    assertNotNull(body);
                    assertEquals(Constants.InvalidPaginationMessage, body.getError());
                });
    }

    @Test
    void getAllProducts_invalidPaginationSizeParam() {
        webClient.get()
                .uri("/products?page=10&size=-1")
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(GenericErrorResponse.class)
                .consumeWith(response -> {
                    GenericErrorResponse body = response.getResponseBody();

                    assertNotNull(body);
                    assertEquals(Constants.InvalidPaginationMessage, body.getError());
                });
    }
}
