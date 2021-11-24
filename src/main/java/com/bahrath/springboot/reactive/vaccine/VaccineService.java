package com.bahrath.springboot.reactive.vaccine;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class VaccineService {

    public Flux<Vaccine> getVeccines() {

        return Flux.just(Vaccine.builder().name("Pfizer").build(),
                Vaccine.builder().name("J&J").build(),
                Vaccine.builder().name("Covaxin").build());
    }

}
