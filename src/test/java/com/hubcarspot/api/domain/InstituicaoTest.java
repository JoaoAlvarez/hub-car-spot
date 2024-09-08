package com.hubcarspot.api.domain;

import static com.hubcarspot.api.domain.InstituicaoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.hubcarspot.api.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InstituicaoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Instituicao.class);
        Instituicao instituicao1 = getInstituicaoSample1();
        Instituicao instituicao2 = new Instituicao();
        assertThat(instituicao1).isNotEqualTo(instituicao2);

        instituicao2.setId(instituicao1.getId());
        assertThat(instituicao1).isEqualTo(instituicao2);

        instituicao2 = getInstituicaoSample2();
        assertThat(instituicao1).isNotEqualTo(instituicao2);
    }
}
