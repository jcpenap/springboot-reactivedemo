package com.bahrath.springboot.reactive.controller;

import com.bahrath.springboot.reactive.entities.Product;
import com.bahrath.springboot.reactive.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class ProductControllerTest {

    @Autowired
    private ProductController controller;

    @MockBean
    private ProductRepository repository;

    @Test
    void addProduct() {

        Product product = new Product(null, "Iphone", "It's owesome", 3000d);
        Product productSaved = new Product("12311231", "Iphone", "It's owesome", 3000d);
        when(repository.save(product)).thenReturn(Mono.just(productSaved));

        StepVerifier.create(controller.addProduct(product))
                .assertNext(p -> {
                    assertNotNull(p);
                    assertNotNull(p.getId());
                    assertEquals("12311231", p.getId());;
                })
                .expectComplete()
                .verify();
        verify(repository).save(product);
    }

    @Test
    void getProducts() {
        when(repository.findAll())
                .thenReturn(Flux.just(new Product("12311231", "Iphone", "It's owesome", 3000d),
                new Product("12311232", "Iphone", "It's owesome", 3000d),
                new Product("12311233", "Iphone", "It's owesome", 3000d)));

        StepVerifier.create(controller.getProducts())
                .expectNextCount(3)
                .expectComplete()
                .verify();

        verify(repository).findAll();
    }
}