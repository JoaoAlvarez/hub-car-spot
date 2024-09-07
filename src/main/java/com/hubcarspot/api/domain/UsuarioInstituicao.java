package com.hubcarspot.api.domain;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A UsuarioInstituicao.
 */
@Document(collection = "usuario_instituicao")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UsuarioInstituicao implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("identificador")
    private String identificador;

    @NotNull
    @Field("is_master")
    private Boolean isMaster;

    @NotNull
    @Field("role")
    private String role;

    @NotNull
    @Field("read")
    private Boolean read;

    @NotNull
    @Field("write")
    private Boolean write;

    @NotNull
    @Field("update")
    private Boolean update;

    @DBRef
    @Field("instituicao")
    private Instituicao instituicao;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public UsuarioInstituicao id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdentificador() {
        return this.identificador;
    }

    public UsuarioInstituicao identificador(String identificador) {
        this.setIdentificador(identificador);
        return this;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public Boolean getIsMaster() {
        return this.isMaster;
    }

    public UsuarioInstituicao isMaster(Boolean isMaster) {
        this.setIsMaster(isMaster);
        return this;
    }

    public void setIsMaster(Boolean isMaster) {
        this.isMaster = isMaster;
    }

    public String getRole() {
        return this.role;
    }

    public UsuarioInstituicao role(String role) {
        this.setRole(role);
        return this;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Boolean getRead() {
        return this.read;
    }

    public UsuarioInstituicao read(Boolean read) {
        this.setRead(read);
        return this;
    }

    public void setRead(Boolean read) {
        this.read = read;
    }

    public Boolean getWrite() {
        return this.write;
    }

    public UsuarioInstituicao write(Boolean write) {
        this.setWrite(write);
        return this;
    }

    public void setWrite(Boolean write) {
        this.write = write;
    }

    public Boolean getUpdate() {
        return this.update;
    }

    public UsuarioInstituicao update(Boolean update) {
        this.setUpdate(update);
        return this;
    }

    public void setUpdate(Boolean update) {
        this.update = update;
    }

    public Instituicao getInstituicao() {
        return this.instituicao;
    }

    public void setInstituicao(Instituicao instituicao) {
        this.instituicao = instituicao;
    }

    public UsuarioInstituicao instituicao(Instituicao instituicao) {
        this.setInstituicao(instituicao);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UsuarioInstituicao)) {
            return false;
        }
        return getId() != null && getId().equals(((UsuarioInstituicao) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UsuarioInstituicao{" +
            "id=" + getId() +
            ", identificador='" + getIdentificador() + "'" +
            ", isMaster='" + getIsMaster() + "'" +
            ", role='" + getRole() + "'" +
            ", read='" + getRead() + "'" +
            ", write='" + getWrite() + "'" +
            ", update='" + getUpdate() + "'" +
            "}";
    }
}
