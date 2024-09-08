package com.hubcarspot.api.domain;

import java.util.UUID;

public class CombustivelTestSamples {

    public static Combustivel getCombustivelSample1() {
        return new Combustivel().id("id1").nome("nome1");
    }

    public static Combustivel getCombustivelSample2() {
        return new Combustivel().id("id2").nome("nome2");
    }

    public static Combustivel getCombustivelRandomSampleGenerator() {
        return new Combustivel().id(UUID.randomUUID().toString()).nome(UUID.randomUUID().toString());
    }
}
