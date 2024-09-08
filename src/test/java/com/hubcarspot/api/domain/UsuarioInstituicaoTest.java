package com.hubcarspot.api.domain;

import static com.hubcarspot.api.domain.InstituicaoTestSamples.*;
import static com.hubcarspot.api.domain.UsuarioInstituicaoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.hubcarspot.api.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UsuarioInstituicaoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UsuarioInstituicao.class);
        UsuarioInstituicao usuarioInstituicao1 = getUsuarioInstituicaoSample1();
        UsuarioInstituicao usuarioInstituicao2 = new UsuarioInstituicao();
        assertThat(usuarioInstituicao1).isNotEqualTo(usuarioInstituicao2);

        usuarioInstituicao2.setId(usuarioInstituicao1.getId());
        assertThat(usuarioInstituicao1).isEqualTo(usuarioInstituicao2);

        usuarioInstituicao2 = getUsuarioInstituicaoSample2();
        assertThat(usuarioInstituicao1).isNotEqualTo(usuarioInstituicao2);
    }

    @Test
    void instituicaoTest() {
        UsuarioInstituicao usuarioInstituicao = getUsuarioInstituicaoRandomSampleGenerator();
        Instituicao instituicaoBack = getInstituicaoRandomSampleGenerator();

        usuarioInstituicao.setInstituicao(instituicaoBack);
        assertThat(usuarioInstituicao.getInstituicao()).isEqualTo(instituicaoBack);

        usuarioInstituicao.instituicao(null);
        assertThat(usuarioInstituicao.getInstituicao()).isNull();
    }
}
