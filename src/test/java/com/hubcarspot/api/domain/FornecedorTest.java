package com.hubcarspot.api.domain;

import static com.hubcarspot.api.domain.FornecedorTestSamples.*;
import static com.hubcarspot.api.domain.InstituicaoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.hubcarspot.api.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FornecedorTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Fornecedor.class);
        Fornecedor fornecedor1 = getFornecedorSample1();
        Fornecedor fornecedor2 = new Fornecedor();
        assertThat(fornecedor1).isNotEqualTo(fornecedor2);

        fornecedor2.setId(fornecedor1.getId());
        assertThat(fornecedor1).isEqualTo(fornecedor2);

        fornecedor2 = getFornecedorSample2();
        assertThat(fornecedor1).isNotEqualTo(fornecedor2);
    }

    @Test
    void instituicaoTest() {
        Fornecedor fornecedor = getFornecedorRandomSampleGenerator();
        Instituicao instituicaoBack = getInstituicaoRandomSampleGenerator();

        fornecedor.setInstituicao(instituicaoBack);
        assertThat(fornecedor.getInstituicao()).isEqualTo(instituicaoBack);

        fornecedor.instituicao(null);
        assertThat(fornecedor.getInstituicao()).isNull();
    }
}
