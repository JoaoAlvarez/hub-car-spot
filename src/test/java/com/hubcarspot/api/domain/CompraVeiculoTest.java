package com.hubcarspot.api.domain;

import static com.hubcarspot.api.domain.CompraVeiculoTestSamples.*;
import static com.hubcarspot.api.domain.FilialTestSamples.*;
import static com.hubcarspot.api.domain.InstituicaoTestSamples.*;
import static com.hubcarspot.api.domain.VeiculoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.hubcarspot.api.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CompraVeiculoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CompraVeiculo.class);
        CompraVeiculo compraVeiculo1 = getCompraVeiculoSample1();
        CompraVeiculo compraVeiculo2 = new CompraVeiculo();
        assertThat(compraVeiculo1).isNotEqualTo(compraVeiculo2);

        compraVeiculo2.setId(compraVeiculo1.getId());
        assertThat(compraVeiculo1).isEqualTo(compraVeiculo2);

        compraVeiculo2 = getCompraVeiculoSample2();
        assertThat(compraVeiculo1).isNotEqualTo(compraVeiculo2);
    }

    @Test
    void instituicaoTest() {
        CompraVeiculo compraVeiculo = getCompraVeiculoRandomSampleGenerator();
        Instituicao instituicaoBack = getInstituicaoRandomSampleGenerator();

        compraVeiculo.setInstituicao(instituicaoBack);
        assertThat(compraVeiculo.getInstituicao()).isEqualTo(instituicaoBack);

        compraVeiculo.instituicao(null);
        assertThat(compraVeiculo.getInstituicao()).isNull();
    }

    @Test
    void veiculoTest() {
        CompraVeiculo compraVeiculo = getCompraVeiculoRandomSampleGenerator();
        Veiculo veiculoBack = getVeiculoRandomSampleGenerator();

        compraVeiculo.setVeiculo(veiculoBack);
        assertThat(compraVeiculo.getVeiculo()).isEqualTo(veiculoBack);

        compraVeiculo.veiculo(null);
        assertThat(compraVeiculo.getVeiculo()).isNull();
    }

    @Test
    void filialTest() {
        CompraVeiculo compraVeiculo = getCompraVeiculoRandomSampleGenerator();
        Filial filialBack = getFilialRandomSampleGenerator();

        compraVeiculo.setFilial(filialBack);
        assertThat(compraVeiculo.getFilial()).isEqualTo(filialBack);

        compraVeiculo.filial(null);
        assertThat(compraVeiculo.getFilial()).isNull();
    }
}
