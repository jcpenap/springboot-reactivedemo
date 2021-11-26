package com.bahrath.springboot.reactive.vaccine;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class VaccineProviderTest {

    @Autowired
    private VaccineProvider provider;

    @MockBean
    private VaccineService service;

    @Test
    void testVaccinesProvider_Reactive() {
        when(service.getVaccines()).thenReturn(Flux.just(Vaccine.builder().name("Pfizer").build(),
                Vaccine.builder().name("J&J").build(),
                Vaccine.builder().name("Covaxin").build()));
        StepVerifier.create(provider.provideVaccines())
                .expectSubscription()
                .expectNext(Vaccine.builder().name("Pfizer").build())
                .expectNext(Vaccine.builder().name("J&J").build())
                .expectNext(Vaccine.builder().name("Covaxin").build())
                .expectComplete()
                .verify();
    }
}