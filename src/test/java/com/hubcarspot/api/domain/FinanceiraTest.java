package com.hubcarspot.api.domain;

import static com.hubcarspot.api.domain.FinanceiraTestSamples.*;
import static com.hubcarspot.api.domain.InstituicaoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.hubcarspot.api.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FinanceiraTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Financeira.class);
        Financeira financeira1 = getFinanceiraSample1();
        Financeira financeira2 = new Financeira();
        assertThat(financeira1).isNotEqualTo(financeira2);

        financeira2.setId(financeira1.getId());
        assertThat(financeira1).isEqualTo(financeira2);

        financeira2 = getFinanceiraSample2();
        assertThat(financeira1).isNotEqualTo(financeira2);
    }

    @Test
    void instituicaoTest() {
        Financeira financeira = getFinanceiraRandomSampleGenerator();
        Instituicao instituicaoBack = getInstituicaoRandomSampleGenerator();

        financeira.setInstituicao(instituicaoBack);
        assertThat(financeira.getInstituicao()).isEqualTo(instituicaoBack);

        financeira.instituicao(null);
        assertThat(financeira.getInstituicao()).isNull();
    }
}
