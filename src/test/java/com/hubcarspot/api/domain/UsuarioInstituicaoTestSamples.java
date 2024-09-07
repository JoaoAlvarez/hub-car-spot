package com.hubcarspot.api.domain;

import java.util.UUID;

public class UsuarioInstituicaoTestSamples {

    public static UsuarioInstituicao getUsuarioInstituicaoSample1() {
        return new UsuarioInstituicao().id("id1").identificador("identificador1").role("role1");
    }

    public static UsuarioInstituicao getUsuarioInstituicaoSample2() {
        return new UsuarioInstituicao().id("id2").identificador("identificador2").role("role2");
    }

    public static UsuarioInstituicao getUsuarioInstituicaoRandomSampleGenerator() {
        return new UsuarioInstituicao()
            .id(UUID.randomUUID().toString())
            .identificador(UUID.randomUUID().toString())
            .role(UUID.randomUUID().toString());
    }
}
