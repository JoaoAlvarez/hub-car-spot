package com.hubcarspot.api.domain;

import java.util.UUID;

public class TrocaVeiculoTestSamples {

    public static TrocaVeiculo getTrocaVeiculoSample1() {
        return new TrocaVeiculo()
            .id("id1")
            .carroEntradaId("carroEntradaId1")
            .carroSaidaId("carroSaidaId1")
            .condicaoPagamento("condicaoPagamento1");
    }

    public static TrocaVeiculo getTrocaVeiculoSample2() {
        return new TrocaVeiculo()
            .id("id2")
            .carroEntradaId("carroEntradaId2")
            .carroSaidaId("carroSaidaId2")
            .condicaoPagamento("condicaoPagamento2");
    }

    public static TrocaVeiculo getTrocaVeiculoRandomSampleGenerator() {
        return new TrocaVeiculo()
            .id(UUID.randomUUID().toString())
            .carroEntradaId(UUID.randomUUID().toString())
            .carroSaidaId(UUID.randomUUID().toString())
            .condicaoPagamento(UUID.randomUUID().toString());
    }
}
