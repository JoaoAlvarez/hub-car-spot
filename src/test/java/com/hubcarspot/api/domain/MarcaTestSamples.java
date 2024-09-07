package com.hubcarspot.api.domain;

import java.util.UUID;

public class MarcaTestSamples {

    public static Marca getMarcaSample1() {
        return new Marca().id("id1").nome("nome1");
    }

    public static Marca getMarcaSample2() {
        return new Marca().id("id2").nome("nome2");
    }

    public static Marca getMarcaRandomSampleGenerator() {
        return new Marca().id(UUID.randomUUID().toString()).nome(UUID.randomUUID().toString());
    }
}
