package com.hubcarspot.api.domain;

import static com.hubcarspot.api.domain.InstituicaoTestSamples.*;
import static com.hubcarspot.api.domain.LocalTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.hubcarspot.api.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LocalTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Local.class);
        Local local1 = getLocalSample1();
        Local local2 = new Local();
        assertThat(local1).isNotEqualTo(local2);

        local2.setId(local1.getId());
        assertThat(local1).isEqualTo(local2);

        local2 = getLocalSample2();
        assertThat(local1).isNotEqualTo(local2);
    }

    @Test
    void instituicaoTest() {
        Local local = getLocalRandomSampleGenerator();
        Instituicao instituicaoBack = getInstituicaoRandomSampleGenerator();

        local.setInstituicao(instituicaoBack);
        assertThat(local.getInstituicao()).isEqualTo(instituicaoBack);

        local.instituicao(null);
        assertThat(local.getInstituicao()).isNull();
    }
}
