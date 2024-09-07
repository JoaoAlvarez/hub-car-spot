package com.hubcarspot.api.domain;

import static com.hubcarspot.api.domain.FilialTestSamples.*;
import static com.hubcarspot.api.domain.InstituicaoTestSamples.*;
import static com.hubcarspot.api.domain.TrocaVeiculoTestSamples.*;
import static com.hubcarspot.api.domain.VeiculoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.hubcarspot.api.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TrocaVeiculoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TrocaVeiculo.class);
        TrocaVeiculo trocaVeiculo1 = getTrocaVeiculoSample1();
        TrocaVeiculo trocaVeiculo2 = new TrocaVeiculo();
        assertThat(trocaVeiculo1).isNotEqualTo(trocaVeiculo2);

        trocaVeiculo2.setId(trocaVeiculo1.getId());
        assertThat(trocaVeiculo1).isEqualTo(trocaVeiculo2);

        trocaVeiculo2 = getTrocaVeiculoSample2();
        assertThat(trocaVeiculo1).isNotEqualTo(trocaVeiculo2);
    }

    @Test
    void veiculoEntradaTest() {
        TrocaVeiculo trocaVeiculo = getTrocaVeiculoRandomSampleGenerator();
        Veiculo veiculoBack = getVeiculoRandomSampleGenerator();

        trocaVeiculo.setVeiculoEntrada(veiculoBack);
        assertThat(trocaVeiculo.getVeiculoEntrada()).isEqualTo(veiculoBack);

        trocaVeiculo.veiculoEntrada(null);
        assertThat(trocaVeiculo.getVeiculoEntrada()).isNull();
    }

    @Test
    void veiculoSaidaTest() {
        TrocaVeiculo trocaVeiculo = getTrocaVeiculoRandomSampleGenerator();
        Veiculo veiculoBack = getVeiculoRandomSampleGenerator();

        trocaVeiculo.setVeiculoSaida(veiculoBack);
        assertThat(trocaVeiculo.getVeiculoSaida()).isEqualTo(veiculoBack);

        trocaVeiculo.veiculoSaida(null);
        assertThat(trocaVeiculo.getVeiculoSaida()).isNull();
    }

    @Test
    void filialTest() {
        TrocaVeiculo trocaVeiculo = getTrocaVeiculoRandomSampleGenerator();
        Filial filialBack = getFilialRandomSampleGenerator();

        trocaVeiculo.setFilial(filialBack);
        assertThat(trocaVeiculo.getFilial()).isEqualTo(filialBack);

        trocaVeiculo.filial(null);
        assertThat(trocaVeiculo.getFilial()).isNull();
    }

    @Test
    void instituicaoTest() {
        TrocaVeiculo trocaVeiculo = getTrocaVeiculoRandomSampleGenerator();
        Instituicao instituicaoBack = getInstituicaoRandomSampleGenerator();

        trocaVeiculo.setInstituicao(instituicaoBack);
        assertThat(trocaVeiculo.getInstituicao()).isEqualTo(instituicaoBack);

        trocaVeiculo.instituicao(null);
        assertThat(trocaVeiculo.getInstituicao()).isNull();
    }
}
