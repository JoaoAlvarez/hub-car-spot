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
 * A VendaVeiculo.
 */
@Document(collection = "venda_veiculo")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VendaVeiculo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("km_saida")
    private Integer kmSaida;

    @NotNull
    @Field("valor_compra")
    private BigDecimal valorCompra;

    @Field("valor_tabela")
    private BigDecimal valorTabela;

    @NotNull
    @Field("valor_venda")
    private BigDecimal valorVenda;

    @NotNull
    @Field("data_venda")
    private LocalDate dataVenda;

    @NotNull
    @Field("condicao_recebimento")
    private String condicaoRecebimento;

    @Field("valor_entrada")
    private BigDecimal valorEntrada;

    @Field("valor_financiado")
    private BigDecimal valorFinanciado;

    @DBRef
    @Field("veiculo")
    private Veiculo veiculo;

    @DBRef
    @Field("instituicao")
    private Instituicao instituicao;

    @DBRef
    @Field("filial")
    @JsonIgnoreProperties(value = { "instituicao" }, allowSetters = true)
    private Filial filial;

    @DBRef
    @Field("financeira")
    @JsonIgnoreProperties(value = { "instituicao" }, allowSetters = true)
    private Financeira financeira;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public VendaVeiculo id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getKmSaida() {
        return this.kmSaida;
    }

    public VendaVeiculo kmSaida(Integer kmSaida) {
        this.setKmSaida(kmSaida);
        return this;
    }

    public void setKmSaida(Integer kmSaida) {
        this.kmSaida = kmSaida;
    }

    public BigDecimal getValorCompra() {
        return this.valorCompra;
    }

    public VendaVeiculo valorCompra(BigDecimal valorCompra) {
        this.setValorCompra(valorCompra);
        return this;
    }

    public void setValorCompra(BigDecimal valorCompra) {
        this.valorCompra = valorCompra;
    }

    public BigDecimal getValorTabela() {
        return this.valorTabela;
    }

    public VendaVeiculo valorTabela(BigDecimal valorTabela) {
        this.setValorTabela(valorTabela);
        return this;
    }

    public void setValorTabela(BigDecimal valorTabela) {
        this.valorTabela = valorTabela;
    }

    public BigDecimal getValorVenda() {
        return this.valorVenda;
    }

    public VendaVeiculo valorVenda(BigDecimal valorVenda) {
        this.setValorVenda(valorVenda);
        return this;
    }

    public void setValorVenda(BigDecimal valorVenda) {
        this.valorVenda = valorVenda;
    }

    public LocalDate getDataVenda() {
        return this.dataVenda;
    }

    public VendaVeiculo dataVenda(LocalDate dataVenda) {
        this.setDataVenda(dataVenda);
        return this;
    }

    public void setDataVenda(LocalDate dataVenda) {
        this.dataVenda = dataVenda;
    }

    public String getCondicaoRecebimento() {
        return this.condicaoRecebimento;
    }

    public VendaVeiculo condicaoRecebimento(String condicaoRecebimento) {
        this.setCondicaoRecebimento(condicaoRecebimento);
        return this;
    }

    public void setCondicaoRecebimento(String condicaoRecebimento) {
        this.condicaoRecebimento = condicaoRecebimento;
    }

    public BigDecimal getValorEntrada() {
        return this.valorEntrada;
    }

    public VendaVeiculo valorEntrada(BigDecimal valorEntrada) {
        this.setValorEntrada(valorEntrada);
        return this;
    }

    public void setValorEntrada(BigDecimal valorEntrada) {
        this.valorEntrada = valorEntrada;
    }

    public BigDecimal getValorFinanciado() {
        return this.valorFinanciado;
    }

    public VendaVeiculo valorFinanciado(BigDecimal valorFinanciado) {
        this.setValorFinanciado(valorFinanciado);
        return this;
    }

    public void setValorFinanciado(BigDecimal valorFinanciado) {
        this.valorFinanciado = valorFinanciado;
    }

    public Veiculo getVeiculo() {
        return this.veiculo;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }

    public VendaVeiculo veiculo(Veiculo veiculo) {
        this.setVeiculo(veiculo);
        return this;
    }

    public Instituicao getInstituicao() {
        return this.instituicao;
    }

    public void setInstituicao(Instituicao instituicao) {
        this.instituicao = instituicao;
    }

    public VendaVeiculo instituicao(Instituicao instituicao) {
        this.setInstituicao(instituicao);
        return this;
    }

    public Filial getFilial() {
        return this.filial;
    }

    public void setFilial(Filial filial) {
        this.filial = filial;
    }

    public VendaVeiculo filial(Filial filial) {
        this.setFilial(filial);
        return this;
    }

    public Financeira getFinanceira() {
        return this.financeira;
    }

    public void setFinanceira(Financeira financeira) {
        this.financeira = financeira;
    }

    public VendaVeiculo financeira(Financeira financeira) {
        this.setFinanceira(financeira);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VendaVeiculo)) {
            return false;
        }
        return getId() != null && getId().equals(((VendaVeiculo) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VendaVeiculo{" +
            "id=" + getId() +
            ", kmSaida=" + getKmSaida() +
            ", valorCompra=" + getValorCompra() +
            ", valorTabela=" + getValorTabela() +
            ", valorVenda=" + getValorVenda() +
            ", dataVenda='" + getDataVenda() + "'" +
            ", condicaoRecebimento='" + getCondicaoRecebimento() + "'" +
            ", valorEntrada=" + getValorEntrada() +
            ", valorFinanciado=" + getValorFinanciado() +
            "}";
    }
}
