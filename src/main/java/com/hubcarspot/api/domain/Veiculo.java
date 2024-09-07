package com.hubcarspot.api.domain;

import com.hubcarspot.api.domain.enumeration.EspecieVeiculo;
import com.hubcarspot.api.domain.enumeration.StatusVeiculo;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Veiculo.
 */
@Document(collection = "veiculo")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Veiculo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("created_at")
    private LocalDate createdAt;

    @NotNull
    @Field("especie")
    private EspecieVeiculo especie;

    @NotNull
    @Field("placa")
    private String placa;

    @NotNull
    @Field("marca")
    private String marca;

    @Field("modelo")
    private String modelo;

    @NotNull
    @Field("ano_fabricacao")
    private Integer anoFabricacao;

    @NotNull
    @Field("ano_modelo")
    private Integer anoModelo;

    @Field("cor")
    private String cor;

    @NotNull
    @Field("combustivel")
    private String combustivel;

    @Field("cambio")
    private String cambio;

    @Field("status")
    private StatusVeiculo status;

    @Field("chassi")
    private String chassi;

    @Field("renavam")
    private String renavam;

    @Field("numero_motor")
    private String numeroMotor;

    @Field("numero_cambio")
    private String numeroCambio;

    @Field("quilometraegem")
    private Integer quilometraegem;

    @Field("km_saida")
    private Integer kmSaida;

    @Field("cavalos")
    private String cavalos;

    @Field("motorizacao")
    private String motorizacao;

    @Field("adicional")
    private String adicional;

    @Field("descritivo_curto_acessorios")
    private String descritivoCurtoAcessorios;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Veiculo id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDate getCreatedAt() {
        return this.createdAt;
    }

    public Veiculo createdAt(LocalDate createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public EspecieVeiculo getEspecie() {
        return this.especie;
    }

    public Veiculo especie(EspecieVeiculo especie) {
        this.setEspecie(especie);
        return this;
    }

    public void setEspecie(EspecieVeiculo especie) {
        this.especie = especie;
    }

    public String getPlaca() {
        return this.placa;
    }

    public Veiculo placa(String placa) {
        this.setPlaca(placa);
        return this;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getMarca() {
        return this.marca;
    }

    public Veiculo marca(String marca) {
        this.setMarca(marca);
        return this;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return this.modelo;
    }

    public Veiculo modelo(String modelo) {
        this.setModelo(modelo);
        return this;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public Integer getAnoFabricacao() {
        return this.anoFabricacao;
    }

    public Veiculo anoFabricacao(Integer anoFabricacao) {
        this.setAnoFabricacao(anoFabricacao);
        return this;
    }

    public void setAnoFabricacao(Integer anoFabricacao) {
        this.anoFabricacao = anoFabricacao;
    }

    public Integer getAnoModelo() {
        return this.anoModelo;
    }

    public Veiculo anoModelo(Integer anoModelo) {
        this.setAnoModelo(anoModelo);
        return this;
    }

    public void setAnoModelo(Integer anoModelo) {
        this.anoModelo = anoModelo;
    }

    public String getCor() {
        return this.cor;
    }

    public Veiculo cor(String cor) {
        this.setCor(cor);
        return this;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public String getCombustivel() {
        return this.combustivel;
    }

    public Veiculo combustivel(String combustivel) {
        this.setCombustivel(combustivel);
        return this;
    }

    public void setCombustivel(String combustivel) {
        this.combustivel = combustivel;
    }

    public String getCambio() {
        return this.cambio;
    }

    public Veiculo cambio(String cambio) {
        this.setCambio(cambio);
        return this;
    }

    public void setCambio(String cambio) {
        this.cambio = cambio;
    }

    public StatusVeiculo getStatus() {
        return this.status;
    }

    public Veiculo status(StatusVeiculo status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(StatusVeiculo status) {
        this.status = status;
    }

    public String getChassi() {
        return this.chassi;
    }

    public Veiculo chassi(String chassi) {
        this.setChassi(chassi);
        return this;
    }

    public void setChassi(String chassi) {
        this.chassi = chassi;
    }

    public String getRenavam() {
        return this.renavam;
    }

    public Veiculo renavam(String renavam) {
        this.setRenavam(renavam);
        return this;
    }

    public void setRenavam(String renavam) {
        this.renavam = renavam;
    }

    public String getNumeroMotor() {
        return this.numeroMotor;
    }

    public Veiculo numeroMotor(String numeroMotor) {
        this.setNumeroMotor(numeroMotor);
        return this;
    }

    public void setNumeroMotor(String numeroMotor) {
        this.numeroMotor = numeroMotor;
    }

    public String getNumeroCambio() {
        return this.numeroCambio;
    }

    public Veiculo numeroCambio(String numeroCambio) {
        this.setNumeroCambio(numeroCambio);
        return this;
    }

    public void setNumeroCambio(String numeroCambio) {
        this.numeroCambio = numeroCambio;
    }

    public Integer getQuilometraegem() {
        return this.quilometraegem;
    }

    public Veiculo quilometraegem(Integer quilometraegem) {
        this.setQuilometraegem(quilometraegem);
        return this;
    }

    public void setQuilometraegem(Integer quilometraegem) {
        this.quilometraegem = quilometraegem;
    }

    public Integer getKmSaida() {
        return this.kmSaida;
    }

    public Veiculo kmSaida(Integer kmSaida) {
        this.setKmSaida(kmSaida);
        return this;
    }

    public void setKmSaida(Integer kmSaida) {
        this.kmSaida = kmSaida;
    }

    public String getCavalos() {
        return this.cavalos;
    }

    public Veiculo cavalos(String cavalos) {
        this.setCavalos(cavalos);
        return this;
    }

    public void setCavalos(String cavalos) {
        this.cavalos = cavalos;
    }

    public String getMotorizacao() {
        return this.motorizacao;
    }

    public Veiculo motorizacao(String motorizacao) {
        this.setMotorizacao(motorizacao);
        return this;
    }

    public void setMotorizacao(String motorizacao) {
        this.motorizacao = motorizacao;
    }

    public String getAdicional() {
        return this.adicional;
    }

    public Veiculo adicional(String adicional) {
        this.setAdicional(adicional);
        return this;
    }

    public void setAdicional(String adicional) {
        this.adicional = adicional;
    }

    public String getDescritivoCurtoAcessorios() {
        return this.descritivoCurtoAcessorios;
    }

    public Veiculo descritivoCurtoAcessorios(String descritivoCurtoAcessorios) {
        this.setDescritivoCurtoAcessorios(descritivoCurtoAcessorios);
        return this;
    }

    public void setDescritivoCurtoAcessorios(String descritivoCurtoAcessorios) {
        this.descritivoCurtoAcessorios = descritivoCurtoAcessorios;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Veiculo)) {
            return false;
        }
        return getId() != null && getId().equals(((Veiculo) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Veiculo{" +
            "id=" + getId() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", especie='" + getEspecie() + "'" +
            ", placa='" + getPlaca() + "'" +
            ", marca='" + getMarca() + "'" +
            ", modelo='" + getModelo() + "'" +
            ", anoFabricacao=" + getAnoFabricacao() +
            ", anoModelo=" + getAnoModelo() +
            ", cor='" + getCor() + "'" +
            ", combustivel='" + getCombustivel() + "'" +
            ", cambio='" + getCambio() + "'" +
            ", status='" + getStatus() + "'" +
            ", chassi='" + getChassi() + "'" +
            ", renavam='" + getRenavam() + "'" +
            ", numeroMotor='" + getNumeroMotor() + "'" +
            ", numeroCambio='" + getNumeroCambio() + "'" +
            ", quilometraegem=" + getQuilometraegem() +
            ", kmSaida=" + getKmSaida() +
            ", cavalos='" + getCavalos() + "'" +
            ", motorizacao='" + getMotorizacao() + "'" +
            ", adicional='" + getAdicional() + "'" +
            ", descritivoCurtoAcessorios='" + getDescritivoCurtoAcessorios() + "'" +
            "}";
    }
}
