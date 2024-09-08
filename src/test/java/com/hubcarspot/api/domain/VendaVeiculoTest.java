package com.hubcarspot.api.domain;

import static com.hubcarspot.api.domain.FilialTestSamples.*;
import static com.hubcarspot.api.domain.FinanceiraTestSamples.*;
import static com.hubcarspot.api.domain.InstituicaoTestSamples.*;
import static com.hubcarspot.api.domain.VeiculoTestSamples.*;
import static com.hubcarspot.api.domain.VendaVeiculoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.hubcarspot.api.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VendaVeiculoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VendaVeiculo.class);
        VendaVeiculo vendaVeiculo1 = getVendaVeiculoSample1();
        VendaVeiculo vendaVeiculo2 = new VendaVeiculo();
        assertThat(vendaVeiculo1).isNotEqualTo(vendaVeiculo2);

        vendaVeiculo2.setId(vendaVeiculo1.getId());
        assertThat(vendaVeiculo1).isEqualTo(vendaVeiculo2);

        vendaVeiculo2 = getVendaVeiculoSample2();
        assertThat(vendaVeiculo1).isNotEqualTo(vendaVeiculo2);
    }

    @Test
    void veiculoTest() {
        VendaVeiculo vendaVeiculo = getVendaVeiculoRandomSampleGenerator();
        Veiculo veiculoBack = getVeiculoRandomSampleGenerator();

        vendaVeiculo.setVeiculo(veiculoBack);
        assertThat(vendaVeiculo.getVeiculo()).isEqualTo(veiculoBack);

        vendaVeiculo.veiculo(null);
        assertThat(vendaVeiculo.getVeiculo()).isNull();
    }

    @Test
    void instituicaoTest() {
        VendaVeiculo vendaVeiculo = getVendaVeiculoRandomSampleGenerator();
        Instituicao instituicaoBack = getInstituicaoRandomSampleGenerator();

        vendaVeiculo.setInstituicao(instituicaoBack);
        assertThat(vendaVeiculo.getInstituicao()).isEqualTo(instituicaoBack);

        vendaVeiculo.instituicao(null);
        assertThat(vendaVeiculo.getInstituicao()).isNull();
    }

    @Test
    void filialTest() {
        VendaVeiculo vendaVeiculo = getVendaVeiculoRandomSampleGenerator();
        Filial filialBack = getFilialRandomSampleGenerator();

        vendaVeiculo.setFilial(filialBack);
        assertThat(vendaVeiculo.getFilial()).isEqualTo(filialBack);

        vendaVeiculo.filial(null);
        assertThat(vendaVeiculo.getFilial()).isNull();
    }

    @Test
    void financeiraTest() {
        VendaVeiculo vendaVeiculo = getVendaVeiculoRandomSampleGenerator();
        Financeira financeiraBack = getFinanceiraRandomSampleGenerator();

        vendaVeiculo.setFinanceira(financeiraBack);
        assertThat(vendaVeiculo.getFinanceira()).isEqualTo(financeiraBack);

        vendaVeiculo.financeira(null);
        assertThat(vendaVeiculo.getFinanceira()).isNull();
    }
}
