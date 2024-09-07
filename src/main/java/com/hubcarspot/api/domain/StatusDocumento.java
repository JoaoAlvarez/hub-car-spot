package com.hubcarspot.api.domain;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A StatusDocumento.
 */
@Document(collection = "status_documento")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StatusDocumento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("instituicao_id")
    private String instituicaoId;

    @Field("nome")
    private String nome;

    @DBRef
    @Field("instituicao")
    private Instituicao instituicao;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public StatusDocumento id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInstituicaoId() {
        return this.instituicaoId;
    }

    public StatusDocumento instituicaoId(String instituicaoId) {
        this.setInstituicaoId(instituicaoId);
        return this;
    }

    public void setInstituicaoId(String instituicaoId) {
        this.instituicaoId = instituicaoId;
    }

    public String getNome() {
        return this.nome;
    }

    public StatusDocumento nome(String nome) {
        this.setNome(nome);
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Instituicao getInstituicao() {
        return this.instituicao;
    }

    public void setInstituicao(Instituicao instituicao) {
        this.instituicao = instituicao;
    }

    public StatusDocumento instituicao(Instituicao instituicao) {
        this.setInstituicao(instituicao);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StatusDocumento)) {
            return false;
        }
        return getId() != null && getId().equals(((StatusDocumento) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StatusDocumento{" +
            "id=" + getId() +
            ", instituicaoId='" + getInstituicaoId() + "'" +
            ", nome='" + getNome() + "'" +
            "}";
    }
}
