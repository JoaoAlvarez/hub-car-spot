package com.hubcarspot.api.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class CompraVeiculoTestSamples {

    private static final Random random = new Random();
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static CompraVeiculo getCompraVeiculoSample1() {
        return new CompraVeiculo()
            .id("id1")
            .kmEntrada(1)
            .enderecoCrlv("enderecoCrlv1")
            .cidadeCrlv("cidadeCrlv1")
            .ufCrlv("ufCrlv1")
            .cpfCrlv("cpfCrlv1")
            .condicaoPagamento("condicaoPagamento1");
    }

    public static CompraVeiculo getCompraVeiculoSample2() {
        return new CompraVeiculo()
            .id("id2")
            .kmEntrada(2)
            .enderecoCrlv("enderecoCrlv2")
            .cidadeCrlv("cidadeCrlv2")
            .ufCrlv("ufCrlv2")
            .cpfCrlv("cpfCrlv2")
            .condicaoPagamento("condicaoPagamento2");
    }

    public static CompraVeiculo getCompraVeiculoRandomSampleGenerator() {
        return new CompraVeiculo()
            .id(UUID.randomUUID().toString())
            .kmEntrada(intCount.incrementAndGet())
            .enderecoCrlv(UUID.randomUUID().toString())
            .cidadeCrlv(UUID.randomUUID().toString())
            .ufCrlv(UUID.randomUUID().toString())
            .cpfCrlv(UUID.randomUUID().toString())
            .condicaoPagamento(UUID.randomUUID().toString());
    }
}
