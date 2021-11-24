package com.bahrath.springboot.reactive.vaccine;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Vaccine {
    private String name;
    private boolean delivered;

}
