package com.bahrath.springboot.reactive.vaccine;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@Builder
public class Vaccine {
    private String name;
    private boolean delivered;

    @Override
    public boolean equals(Object o) {
        Vaccine vaccine = null;
        if (o instanceof Vaccine) {
            vaccine = (Vaccine) o;
        }
        return this.name.equals(vaccine.name);
    }
}
