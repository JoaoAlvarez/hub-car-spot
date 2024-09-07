package com.hubcarspot.api.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A TrocaVeiculo.
 */
@Document(collection = "troca_veiculo")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TrocaVeiculo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("carro_entrada_id")
    private String carroEntradaId;

    @NotNull
    @Field("carro_saida_id")
    private String carroSaidaId;

    @NotNull
    @Field("data_troca")
    private LocalDate dataTroca;

    @Field("condicao_pagamento")
    private String condicaoPagamento;

    @Field("valor_pago")
    private BigDecimal valorPago;

    @Field("valor_recebido")
    private BigDecimal valorRecebido;

    @DBRef
    @Field("veiculoEntrada")
    private Veiculo veiculoEntrada;

    @DBRef
    @Field("veiculoSaida")
    private Veiculo veiculoSaida;

    @DBRef
    @Field("filial")
    @JsonIgnoreProperties(value = { "instituicao" }, allowSetters = true)
    private Filial filial;

    @DBRef
    @Field("instituicao")
    private Instituicao instituicao;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public TrocaVeiculo id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCarroEntradaId() {
        return this.carroEntradaId;
    }

    public TrocaVeiculo carroEntradaId(String carroEntradaId) {
        this.setCarroEntradaId(carroEntradaId);
        return this;
    }

    public void setCarroEntradaId(String carroEntradaId) {
        this.carroEntradaId = carroEntradaId;
    }

    public String getCarroSaidaId() {
        return this.carroSaidaId;
    }

    public TrocaVeiculo carroSaidaId(String carroSaidaId) {
        this.setCarroSaidaId(carroSaidaId);
        return this;
    }

    public void setCarroSaidaId(String carroSaidaId) {
        this.carroSaidaId = carroSaidaId;
    }

    public LocalDate getDataTroca() {
        return this.dataTroca;
    }

    public TrocaVeiculo dataTroca(LocalDate dataTroca) {
        this.setDataTroca(dataTroca);
        return this;
    }

    public void setDataTroca(LocalDate dataTroca) {
        this.dataTroca = dataTroca;
    }

    public String getCondicaoPagamento() {
        return this.condicaoPagamento;
    }

    public TrocaVeiculo condicaoPagamento(String condicaoPagamento) {
        this.setCondicaoPagamento(condicaoPagamento);
        return this;
    }

    public void setCondicaoPagamento(String condicaoPagamento) {
        this.condicaoPagamento = condicaoPagamento;
    }

    public BigDecimal getValorPago() {
        return this.valorPago;
    }

    public TrocaVeiculo valorPago(BigDecimal valorPago) {
        this.setValorPago(valorPago);
        return this;
    }

    public void setValorPago(BigDecimal valorPago) {
        this.valorPago = valorPago;
    }

    public BigDecimal getValorRecebido() {
        return this.valorRecebido;
    }

    public TrocaVeiculo valorRecebido(BigDecimal valorRecebido) {
        this.setValorRecebido(valorRecebido);
        return this;
    }

    public void setValorRecebido(BigDecimal valorRecebido) {
        this.valorRecebido = valorRecebido;
    }

    public Veiculo getVeiculoEntrada() {
        return this.veiculoEntrada;
    }

    public void setVeiculoEntrada(Veiculo veiculo) {
        this.veiculoEntrada = veiculo;
    }

    public TrocaVeiculo veiculoEntrada(Veiculo veiculo) {
        this.setVeiculoEntrada(veiculo);
        return this;
    }

    public Veiculo getVeiculoSaida() {
        return this.veiculoSaida;
    }

    public void setVeiculoSaida(Veiculo veiculo) {
        this.veiculoSaida = veiculo;
    }

    public TrocaVeiculo veiculoSaida(Veiculo veiculo) {
        this.setVeiculoSaida(veiculo);
        return this;
    }

    public Filial getFilial() {
        return this.filial;
    }

    public void setFilial(Filial filial) {
        this.filial = filial;
    }

    public TrocaVeiculo filial(Filial filial) {
        this.setFilial(filial);
        return this;
    }

    public Instituicao getInstituicao() {
        return this.instituicao;
    }

    public void setInstituicao(Instituicao instituicao) {
        this.instituicao = instituicao;
    }

    public TrocaVeiculo instituicao(Instituicao instituicao) {
        this.setInstituicao(instituicao);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TrocaVeiculo)) {
            return false;
        }
        return getId() != null && getId().equals(((TrocaVeiculo) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TrocaVeiculo{" +
            "id=" + getId() +
            ", carroEntradaId='" + getCarroEntradaId() + "'" +
            ", carroSaidaId='" + getCarroSaidaId() + "'" +
            ", dataTroca='" + getDataTroca() + "'" +
            ", condicaoPagamento='" + getCondicaoPagamento() + "'" +
            ", valorPago=" + getValorPago() +
            ", valorRecebido=" + getValorRecebido() +
            "}";
    }
}
