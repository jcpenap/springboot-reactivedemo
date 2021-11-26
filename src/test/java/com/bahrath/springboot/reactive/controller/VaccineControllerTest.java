package com.bahrath.springboot.reactive.controller;

import com.bahrath.springboot.reactive.vaccine.Vaccine;
import com.bahrath.springboot.reactive.vaccine.VaccineService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class VaccineControllerTest {

    @Autowired
    private VaccineController controller;

    @MockBean
    private VaccineService service;

    @Test
    void getVaccines() {
        when(service.getVaccines()).thenReturn(Flux.just(Vaccine.builder().name("Pfizer").build(),
                Vaccine.builder().name("J&J").build(),
                Vaccine.builder().name("Covaxin").build()));

        StepVerifier.create(controller.getVaccines())
                .expectNextCount(3)
                .expectComplete()
                .verify();

        verify(service).getVaccines();
    }
}