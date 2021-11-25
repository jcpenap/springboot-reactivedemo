package com.bahrath.springboot.reactive.repository;

import com.bahrath.springboot.reactive.entities.Product;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ProductRepository extends ReactiveMongoRepository<Product, Long> {
}
