package com.hubcarspot.api.domain;

import static com.hubcarspot.api.domain.InstituicaoTestSamples.*;
import static com.hubcarspot.api.domain.StatusDocumentoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.hubcarspot.api.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StatusDocumentoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StatusDocumento.class);
        StatusDocumento statusDocumento1 = getStatusDocumentoSample1();
        StatusDocumento statusDocumento2 = new StatusDocumento();
        assertThat(statusDocumento1).isNotEqualTo(statusDocumento2);

        statusDocumento2.setId(statusDocumento1.getId());
        assertThat(statusDocumento1).isEqualTo(statusDocumento2);

        statusDocumento2 = getStatusDocumentoSample2();
        assertThat(statusDocumento1).isNotEqualTo(statusDocumento2);
    }

    @Test
    void instituicaoTest() {
        StatusDocumento statusDocumento = getStatusDocumentoRandomSampleGenerator();
        Instituicao instituicaoBack = getInstituicaoRandomSampleGenerator();

        statusDocumento.setInstituicao(instituicaoBack);
        assertThat(statusDocumento.getInstituicao()).isEqualTo(instituicaoBack);

        statusDocumento.instituicao(null);
        assertThat(statusDocumento.getInstituicao()).isNull();
    }
}
