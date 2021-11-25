package com.bahrath.springboot.reactive.controller;

import com.bahrath.springboot.reactive.entities.Product;
import com.bahrath.springboot.reactive.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductRepository repository;

    @PostMapping
    public Mono<Product> addProduct(@RequestBody Product product) {
        return repository.save(product);
    }

    @GetMapping
    public Flux<Product> getProducts() {
        return repository.findAll();
    }
}
