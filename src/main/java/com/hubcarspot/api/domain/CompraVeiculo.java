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
 * A CompraVeiculo.
 */
@Document(collection = "compra_veiculo")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CompraVeiculo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("km_entrada")
    private Integer kmEntrada;

    @NotNull
    @Field("valor")
    private BigDecimal valor;

    @Field("valor_estimado")
    private BigDecimal valorEstimado;

    @Field("endereco_crlv")
    private String enderecoCrlv;

    @Field("cidade_crlv")
    private String cidadeCrlv;

    @Field("uf_crlv")
    private String ufCrlv;

    @Field("cpf_crlv")
    private String cpfCrlv;

    @NotNull
    @Field("data_compra")
    private LocalDate dataCompra;

    @NotNull
    @Field("condicao_pagamento")
    private String condicaoPagamento;

    @Field("valor_pago")
    private BigDecimal valorPago;

    @DBRef
    @Field("instituicao")
    private Instituicao instituicao;

    @DBRef
    @Field("veiculo")
    private Veiculo veiculo;

    @DBRef
    @Field("filial")
    @JsonIgnoreProperties(value = { "instituicao" }, allowSetters = true)
    private Filial filial;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public CompraVeiculo id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getKmEntrada() {
        return this.kmEntrada;
    }

    public CompraVeiculo kmEntrada(Integer kmEntrada) {
        this.setKmEntrada(kmEntrada);
        return this;
    }

    public void setKmEntrada(Integer kmEntrada) {
        this.kmEntrada = kmEntrada;
    }

    public BigDecimal getValor() {
        return this.valor;
    }

    public CompraVeiculo valor(BigDecimal valor) {
        this.setValor(valor);
        return this;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public BigDecimal getValorEstimado() {
        return this.valorEstimado;
    }

    public CompraVeiculo valorEstimado(BigDecimal valorEstimado) {
        this.setValorEstimado(valorEstimado);
        return this;
    }

    public void setValorEstimado(BigDecimal valorEstimado) {
        this.valorEstimado = valorEstimado;
    }

    public String getEnderecoCrlv() {
        return this.enderecoCrlv;
    }

    public CompraVeiculo enderecoCrlv(String enderecoCrlv) {
        this.setEnderecoCrlv(enderecoCrlv);
        return this;
    }

    public void setEnderecoCrlv(String enderecoCrlv) {
        this.enderecoCrlv = enderecoCrlv;
    }

    public String getCidadeCrlv() {
        return this.cidadeCrlv;
    }

    public CompraVeiculo cidadeCrlv(String cidadeCrlv) {
        this.setCidadeCrlv(cidadeCrlv);
        return this;
    }

    public void setCidadeCrlv(String cidadeCrlv) {
        this.cidadeCrlv = cidadeCrlv;
    }

    public String getUfCrlv() {
        return this.ufCrlv;
    }

    public CompraVeiculo ufCrlv(String ufCrlv) {
        this.setUfCrlv(ufCrlv);
        return this;
    }

    public void setUfCrlv(String ufCrlv) {
        this.ufCrlv = ufCrlv;
    }

    public String getCpfCrlv() {
        return this.cpfCrlv;
    }

    public CompraVeiculo cpfCrlv(String cpfCrlv) {
        this.setCpfCrlv(cpfCrlv);
        return this;
    }

    public void setCpfCrlv(String cpfCrlv) {
        this.cpfCrlv = cpfCrlv;
    }

    public LocalDate getDataCompra() {
        return this.dataCompra;
    }

    public CompraVeiculo dataCompra(LocalDate dataCompra) {
        this.setDataCompra(dataCompra);
        return this;
    }

    public void setDataCompra(LocalDate dataCompra) {
        this.dataCompra = dataCompra;
    }

    public String getCondicaoPagamento() {
        return this.condicaoPagamento;
    }

    public CompraVeiculo condicaoPagamento(String condicaoPagamento) {
        this.setCondicaoPagamento(condicaoPagamento);
        return this;
    }

    public void setCondicaoPagamento(String condicaoPagamento) {
        this.condicaoPagamento = condicaoPagamento;
    }

    public BigDecimal getValorPago() {
        return this.valorPago;
    }

    public CompraVeiculo valorPago(BigDecimal valorPago) {
        this.setValorPago(valorPago);
        return this;
    }

    public void setValorPago(BigDecimal valorPago) {
        this.valorPago = valorPago;
    }

    public Instituicao getInstituicao() {
        return this.instituicao;
    }

    public void setInstituicao(Instituicao instituicao) {
        this.instituicao = instituicao;
    }

    public CompraVeiculo instituicao(Instituicao instituicao) {
        this.setInstituicao(instituicao);
        return this;
    }

    public Veiculo getVeiculo() {
        return this.veiculo;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }

    public CompraVeiculo veiculo(Veiculo veiculo) {
        this.setVeiculo(veiculo);
        return this;
    }

    public Filial getFilial() {
        return this.filial;
    }

    public void setFilial(Filial filial) {
        this.filial = filial;
    }

    public CompraVeiculo filial(Filial filial) {
        this.setFilial(filial);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CompraVeiculo)) {
            return false;
        }
        return getId() != null && getId().equals(((CompraVeiculo) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CompraVeiculo{" +
            "id=" + getId() +
            ", kmEntrada=" + getKmEntrada() +
            ", valor=" + getValor() +
            ", valorEstimado=" + getValorEstimado() +
            ", enderecoCrlv='" + getEnderecoCrlv() + "'" +
            ", cidadeCrlv='" + getCidadeCrlv() + "'" +
            ", ufCrlv='" + getUfCrlv() + "'" +
            ", cpfCrlv='" + getCpfCrlv() + "'" +
            ", dataCompra='" + getDataCompra() + "'" +
            ", condicaoPagamento='" + getCondicaoPagamento() + "'" +
            ", valorPago=" + getValorPago() +
            "}";
    }
}
