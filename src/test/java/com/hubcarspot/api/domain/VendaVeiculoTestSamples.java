package com.hubcarspot.api.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class VendaVeiculoTestSamples {

    private static final Random random = new Random();
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static VendaVeiculo getVendaVeiculoSample1() {
        return new VendaVeiculo().id("id1").kmSaida(1).condicaoRecebimento("condicaoRecebimento1");
    }

    public static VendaVeiculo getVendaVeiculoSample2() {
        return new VendaVeiculo().id("id2").kmSaida(2).condicaoRecebimento("condicaoRecebimento2");
    }

    public static VendaVeiculo getVendaVeiculoRandomSampleGenerator() {
        return new VendaVeiculo()
            .id(UUID.randomUUID().toString())
            .kmSaida(intCount.incrementAndGet())
            .condicaoRecebimento(UUID.randomUUID().toString());
    }
}
