package com.hubcarspot.api.domain;

import static com.hubcarspot.api.domain.InstituicaoTestSamples.*;
import static com.hubcarspot.api.domain.TaxasTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.hubcarspot.api.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TaxasTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Taxas.class);
        Taxas taxas1 = getTaxasSample1();
        Taxas taxas2 = new Taxas();
        assertThat(taxas1).isNotEqualTo(taxas2);

        taxas2.setId(taxas1.getId());
        assertThat(taxas1).isEqualTo(taxas2);

        taxas2 = getTaxasSample2();
        assertThat(taxas1).isNotEqualTo(taxas2);
    }

    @Test
    void instituicaoTest() {
        Taxas taxas = getTaxasRandomSampleGenerator();
        Instituicao instituicaoBack = getInstituicaoRandomSampleGenerator();

        taxas.setInstituicao(instituicaoBack);
        assertThat(taxas.getInstituicao()).isEqualTo(instituicaoBack);

        taxas.instituicao(null);
        assertThat(taxas.getInstituicao()).isNull();
    }
}
