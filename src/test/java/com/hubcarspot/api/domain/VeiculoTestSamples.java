package com.hubcarspot.api.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class VeiculoTestSamples {

    private static final Random random = new Random();
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Veiculo getVeiculoSample1() {
        return new Veiculo()
            .id("id1")
            .placa("placa1")
            .marca("marca1")
            .modelo("modelo1")
            .anoFabricacao(1)
            .anoModelo(1)
            .cor("cor1")
            .combustivel("combustivel1")
            .cambio("cambio1")
            .chassi("chassi1")
            .renavam("renavam1")
            .numeroMotor("numeroMotor1")
            .numeroCambio("numeroCambio1")
            .quilometraegem(1)
            .kmSaida(1)
            .cavalos("cavalos1")
            .motorizacao("motorizacao1")
            .adicional("adicional1")
            .descritivoCurtoAcessorios("descritivoCurtoAcessorios1");
    }

    public static Veiculo getVeiculoSample2() {
        return new Veiculo()
            .id("id2")
            .placa("placa2")
            .marca("marca2")
            .modelo("modelo2")
            .anoFabricacao(2)
            .anoModelo(2)
            .cor("cor2")
            .combustivel("combustivel2")
            .cambio("cambio2")
            .chassi("chassi2")
            .renavam("renavam2")
            .numeroMotor("numeroMotor2")
            .numeroCambio("numeroCambio2")
            .quilometraegem(2)
            .kmSaida(2)
            .cavalos("cavalos2")
            .motorizacao("motorizacao2")
            .adicional("adicional2")
            .descritivoCurtoAcessorios("descritivoCurtoAcessorios2");
    }

    public static Veiculo getVeiculoRandomSampleGenerator() {
        return new Veiculo()
            .id(UUID.randomUUID().toString())
            .placa(UUID.randomUUID().toString())
            .marca(UUID.randomUUID().toString())
            .modelo(UUID.randomUUID().toString())
            .anoFabricacao(intCount.incrementAndGet())
            .anoModelo(intCount.incrementAndGet())
            .cor(UUID.randomUUID().toString())
            .combustivel(UUID.randomUUID().toString())
            .cambio(UUID.randomUUID().toString())
            .chassi(UUID.randomUUID().toString())
            .renavam(UUID.randomUUID().toString())
            .numeroMotor(UUID.randomUUID().toString())
            .numeroCambio(UUID.randomUUID().toString())
            .quilometraegem(intCount.incrementAndGet())
            .kmSaida(intCount.incrementAndGet())
            .cavalos(UUID.randomUUID().toString())
            .motorizacao(UUID.randomUUID().toString())
            .adicional(UUID.randomUUID().toString())
            .descritivoCurtoAcessorios(UUID.randomUUID().toString());
    }
}
