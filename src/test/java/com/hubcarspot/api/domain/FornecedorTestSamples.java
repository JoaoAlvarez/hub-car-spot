package com.hubcarspot.api.domain;

import java.util.UUID;

public class FornecedorTestSamples {

    public static Fornecedor getFornecedorSample1() {
        return new Fornecedor()
            .id("id1")
            .nome("nome1")
            .cnpj("cnpj1")
            .telefone("telefone1")
            .cep("cep1")
            .endereco("endereco1")
            .bairro("bairro1")
            .cidade("cidade1")
            .numero("numero1")
            .uf("uf1");
    }

    public static Fornecedor getFornecedorSample2() {
        return new Fornecedor()
            .id("id2")
            .nome("nome2")
            .cnpj("cnpj2")
            .telefone("telefone2")
            .cep("cep2")
            .endereco("endereco2")
            .bairro("bairro2")
            .cidade("cidade2")
            .numero("numero2")
            .uf("uf2");
    }

    public static Fornecedor getFornecedorRandomSampleGenerator() {
        return new Fornecedor()
            .id(UUID.randomUUID().toString())
            .nome(UUID.randomUUID().toString())
            .cnpj(UUID.randomUUID().toString())
            .telefone(UUID.randomUUID().toString())
            .cep(UUID.randomUUID().toString())
            .endereco(UUID.randomUUID().toString())
            .bairro(UUID.randomUUID().toString())
            .cidade(UUID.randomUUID().toString())
            .numero(UUID.randomUUID().toString())
            .uf(UUID.randomUUID().toString());
    }
}
