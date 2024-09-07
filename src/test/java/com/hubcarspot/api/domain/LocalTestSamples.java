package com.hubcarspot.api.domain;

import java.util.UUID;

public class LocalTestSamples {

    public static Local getLocalSample1() {
        return new Local().id("id1").nome("nome1");
    }

    public static Local getLocalSample2() {
        return new Local().id("id2").nome("nome2");
    }

    public static Local getLocalRandomSampleGenerator() {
        return new Local().id(UUID.randomUUID().toString()).nome(UUID.randomUUID().toString());
    }
}
