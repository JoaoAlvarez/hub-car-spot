package com.hubcarspot.api.domain;

import java.util.UUID;

public class TaxasTestSamples {

    public static Taxas getTaxasSample1() {
        return new Taxas().id("id1").nome("nome1");
    }

    public static Taxas getTaxasSample2() {
        return new Taxas().id("id2").nome("nome2");
    }

    public static Taxas getTaxasRandomSampleGenerator() {
        return new Taxas().id(UUID.randomUUID().toString()).nome(UUID.randomUUID().toString());
    }
}
