package com.hubcarspot.api.domain;

import java.util.UUID;

public class StatusDocumentoTestSamples {

    public static StatusDocumento getStatusDocumentoSample1() {
        return new StatusDocumento().id("id1").instituicaoId("instituicaoId1").nome("nome1");
    }

    public static StatusDocumento getStatusDocumentoSample2() {
        return new StatusDocumento().id("id2").instituicaoId("instituicaoId2").nome("nome2");
    }

    public static StatusDocumento getStatusDocumentoRandomSampleGenerator() {
        return new StatusDocumento()
            .id(UUID.randomUUID().toString())
            .instituicaoId(UUID.randomUUID().toString())
            .nome(UUID.randomUUID().toString());
    }
}
