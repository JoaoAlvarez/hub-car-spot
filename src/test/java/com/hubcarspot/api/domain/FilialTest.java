package com.hubcarspot.api.domain;

import static com.hubcarspot.api.domain.FilialTestSamples.*;
import static com.hubcarspot.api.domain.InstituicaoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.hubcarspot.api.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FilialTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Filial.class);
        Filial filial1 = getFilialSample1();
        Filial filial2 = new Filial();
        assertThat(filial1).isNotEqualTo(filial2);

        filial2.setId(filial1.getId());
        assertThat(filial1).isEqualTo(filial2);

        filial2 = getFilialSample2();
        assertThat(filial1).isNotEqualTo(filial2);
    }

    @Test
    void instituicaoTest() {
        Filial filial = getFilialRandomSampleGenerator();
        Instituicao instituicaoBack = getInstituicaoRandomSampleGenerator();

        filial.setInstituicao(instituicaoBack);
        assertThat(filial.getInstituicao()).isEqualTo(instituicaoBack);

        filial.instituicao(null);
        assertThat(filial.getInstituicao()).isNull();
    }
}
