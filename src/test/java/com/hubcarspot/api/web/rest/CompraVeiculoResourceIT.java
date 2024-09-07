package com.hubcarspot.api.web.rest;

import static com.hubcarspot.api.domain.CompraVeiculoAsserts.*;
import static com.hubcarspot.api.web.rest.TestUtil.createUpdateProxyForBean;
import static com.hubcarspot.api.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hubcarspot.api.IntegrationTest;
import com.hubcarspot.api.domain.CompraVeiculo;
import com.hubcarspot.api.repository.CompraVeiculoRepository;
import com.hubcarspot.api.service.CompraVeiculoService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Integration tests for the {@link CompraVeiculoResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CompraVeiculoResourceIT {

    private static final Integer DEFAULT_KM_ENTRADA = 1;
    private static final Integer UPDATED_KM_ENTRADA = 2;

    private static final BigDecimal DEFAULT_VALOR = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALOR = new BigDecimal(2);

    private static final BigDecimal DEFAULT_VALOR_ESTIMADO = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALOR_ESTIMADO = new BigDecimal(2);

    private static final String DEFAULT_ENDERECO_CRLV = "AAAAAAAAAA";
    private static final String UPDATED_ENDERECO_CRLV = "BBBBBBBBBB";

    private static final String DEFAULT_CIDADE_CRLV = "AAAAAAAAAA";
    private static final String UPDATED_CIDADE_CRLV = "BBBBBBBBBB";

    private static final String DEFAULT_UF_CRLV = "AAAAAAAAAA";
    private static final String UPDATED_UF_CRLV = "BBBBBBBBBB";

    private static final String DEFAULT_CPF_CRLV = "AAAAAAAAAA";
    private static final String UPDATED_CPF_CRLV = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATA_COMPRA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_COMPRA = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_CONDICAO_PAGAMENTO = "AAAAAAAAAA";
    private static final String UPDATED_CONDICAO_PAGAMENTO = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_VALOR_PAGO = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALOR_PAGO = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/compra-veiculos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CompraVeiculoRepository compraVeiculoRepository;

    @Mock
    private CompraVeiculoRepository compraVeiculoRepositoryMock;

    @Mock
    private CompraVeiculoService compraVeiculoServiceMock;

    @Autowired
    private MockMvc restCompraVeiculoMockMvc;

    private CompraVeiculo compraVeiculo;

    private CompraVeiculo insertedCompraVeiculo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CompraVeiculo createEntity() {
        return new CompraVeiculo()
            .kmEntrada(DEFAULT_KM_ENTRADA)
            .valor(DEFAULT_VALOR)
            .valorEstimado(DEFAULT_VALOR_ESTIMADO)
            .enderecoCrlv(DEFAULT_ENDERECO_CRLV)
            .cidadeCrlv(DEFAULT_CIDADE_CRLV)
            .ufCrlv(DEFAULT_UF_CRLV)
            .cpfCrlv(DEFAULT_CPF_CRLV)
            .dataCompra(DEFAULT_DATA_COMPRA)
            .condicaoPagamento(DEFAULT_CONDICAO_PAGAMENTO)
            .valorPago(DEFAULT_VALOR_PAGO);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CompraVeiculo createUpdatedEntity() {
        return new CompraVeiculo()
            .kmEntrada(UPDATED_KM_ENTRADA)
            .valor(UPDATED_VALOR)
            .valorEstimado(UPDATED_VALOR_ESTIMADO)
            .enderecoCrlv(UPDATED_ENDERECO_CRLV)
            .cidadeCrlv(UPDATED_CIDADE_CRLV)
            .ufCrlv(UPDATED_UF_CRLV)
            .cpfCrlv(UPDATED_CPF_CRLV)
            .dataCompra(UPDATED_DATA_COMPRA)
            .condicaoPagamento(UPDATED_CONDICAO_PAGAMENTO)
            .valorPago(UPDATED_VALOR_PAGO);
    }

    @BeforeEach
    public void initTest() {
        compraVeiculo = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedCompraVeiculo != null) {
            compraVeiculoRepository.delete(insertedCompraVeiculo);
            insertedCompraVeiculo = null;
        }
    }

    @Test
    void createCompraVeiculo() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CompraVeiculo
        var returnedCompraVeiculo = om.readValue(
            restCompraVeiculoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(compraVeiculo)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CompraVeiculo.class
        );

        // Validate the CompraVeiculo in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCompraVeiculoUpdatableFieldsEquals(returnedCompraVeiculo, getPersistedCompraVeiculo(returnedCompraVeiculo));

        insertedCompraVeiculo = returnedCompraVeiculo;
    }

    @Test
    void createCompraVeiculoWithExistingId() throws Exception {
        // Create the CompraVeiculo with an existing ID
        compraVeiculo.setId("existing_id");

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCompraVeiculoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(compraVeiculo)))
            .andExpect(status().isBadRequest());

        // Validate the CompraVeiculo in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkKmEntradaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        compraVeiculo.setKmEntrada(null);

        // Create the CompraVeiculo, which fails.

        restCompraVeiculoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(compraVeiculo)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkValorIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        compraVeiculo.setValor(null);

        // Create the CompraVeiculo, which fails.

        restCompraVeiculoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(compraVeiculo)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkDataCompraIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        compraVeiculo.setDataCompra(null);

        // Create the CompraVeiculo, which fails.

        restCompraVeiculoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(compraVeiculo)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkCondicaoPagamentoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        compraVeiculo.setCondicaoPagamento(null);

        // Create the CompraVeiculo, which fails.

        restCompraVeiculoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(compraVeiculo)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllCompraVeiculos() throws Exception {
        // Initialize the database
        insertedCompraVeiculo = compraVeiculoRepository.save(compraVeiculo);

        // Get all the compraVeiculoList
        restCompraVeiculoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(compraVeiculo.getId())))
            .andExpect(jsonPath("$.[*].kmEntrada").value(hasItem(DEFAULT_KM_ENTRADA)))
            .andExpect(jsonPath("$.[*].valor").value(hasItem(sameNumber(DEFAULT_VALOR))))
            .andExpect(jsonPath("$.[*].valorEstimado").value(hasItem(sameNumber(DEFAULT_VALOR_ESTIMADO))))
            .andExpect(jsonPath("$.[*].enderecoCrlv").value(hasItem(DEFAULT_ENDERECO_CRLV)))
            .andExpect(jsonPath("$.[*].cidadeCrlv").value(hasItem(DEFAULT_CIDADE_CRLV)))
            .andExpect(jsonPath("$.[*].ufCrlv").value(hasItem(DEFAULT_UF_CRLV)))
            .andExpect(jsonPath("$.[*].cpfCrlv").value(hasItem(DEFAULT_CPF_CRLV)))
            .andExpect(jsonPath("$.[*].dataCompra").value(hasItem(DEFAULT_DATA_COMPRA.toString())))
            .andExpect(jsonPath("$.[*].condicaoPagamento").value(hasItem(DEFAULT_CONDICAO_PAGAMENTO)))
            .andExpect(jsonPath("$.[*].valorPago").value(hasItem(sameNumber(DEFAULT_VALOR_PAGO))));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCompraVeiculosWithEagerRelationshipsIsEnabled() throws Exception {
        when(compraVeiculoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCompraVeiculoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(compraVeiculoServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCompraVeiculosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(compraVeiculoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCompraVeiculoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(compraVeiculoRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void getCompraVeiculo() throws Exception {
        // Initialize the database
        insertedCompraVeiculo = compraVeiculoRepository.save(compraVeiculo);

        // Get the compraVeiculo
        restCompraVeiculoMockMvc
            .perform(get(ENTITY_API_URL_ID, compraVeiculo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(compraVeiculo.getId()))
            .andExpect(jsonPath("$.kmEntrada").value(DEFAULT_KM_ENTRADA))
            .andExpect(jsonPath("$.valor").value(sameNumber(DEFAULT_VALOR)))
            .andExpect(jsonPath("$.valorEstimado").value(sameNumber(DEFAULT_VALOR_ESTIMADO)))
            .andExpect(jsonPath("$.enderecoCrlv").value(DEFAULT_ENDERECO_CRLV))
            .andExpect(jsonPath("$.cidadeCrlv").value(DEFAULT_CIDADE_CRLV))
            .andExpect(jsonPath("$.ufCrlv").value(DEFAULT_UF_CRLV))
            .andExpect(jsonPath("$.cpfCrlv").value(DEFAULT_CPF_CRLV))
            .andExpect(jsonPath("$.dataCompra").value(DEFAULT_DATA_COMPRA.toString()))
            .andExpect(jsonPath("$.condicaoPagamento").value(DEFAULT_CONDICAO_PAGAMENTO))
            .andExpect(jsonPath("$.valorPago").value(sameNumber(DEFAULT_VALOR_PAGO)));
    }

    @Test
    void getNonExistingCompraVeiculo() throws Exception {
        // Get the compraVeiculo
        restCompraVeiculoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingCompraVeiculo() throws Exception {
        // Initialize the database
        insertedCompraVeiculo = compraVeiculoRepository.save(compraVeiculo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the compraVeiculo
        CompraVeiculo updatedCompraVeiculo = compraVeiculoRepository.findById(compraVeiculo.getId()).orElseThrow();
        updatedCompraVeiculo
            .kmEntrada(UPDATED_KM_ENTRADA)
            .valor(UPDATED_VALOR)
            .valorEstimado(UPDATED_VALOR_ESTIMADO)
            .enderecoCrlv(UPDATED_ENDERECO_CRLV)
            .cidadeCrlv(UPDATED_CIDADE_CRLV)
            .ufCrlv(UPDATED_UF_CRLV)
            .cpfCrlv(UPDATED_CPF_CRLV)
            .dataCompra(UPDATED_DATA_COMPRA)
            .condicaoPagamento(UPDATED_CONDICAO_PAGAMENTO)
            .valorPago(UPDATED_VALOR_PAGO);

        restCompraVeiculoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCompraVeiculo.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCompraVeiculo))
            )
            .andExpect(status().isOk());

        // Validate the CompraVeiculo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCompraVeiculoToMatchAllProperties(updatedCompraVeiculo);
    }

    @Test
    void putNonExistingCompraVeiculo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        compraVeiculo.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompraVeiculoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, compraVeiculo.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(compraVeiculo))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompraVeiculo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchCompraVeiculo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        compraVeiculo.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompraVeiculoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(compraVeiculo))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompraVeiculo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamCompraVeiculo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        compraVeiculo.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompraVeiculoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(compraVeiculo)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CompraVeiculo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateCompraVeiculoWithPatch() throws Exception {
        // Initialize the database
        insertedCompraVeiculo = compraVeiculoRepository.save(compraVeiculo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the compraVeiculo using partial update
        CompraVeiculo partialUpdatedCompraVeiculo = new CompraVeiculo();
        partialUpdatedCompraVeiculo.setId(compraVeiculo.getId());

        partialUpdatedCompraVeiculo
            .kmEntrada(UPDATED_KM_ENTRADA)
            .enderecoCrlv(UPDATED_ENDERECO_CRLV)
            .cidadeCrlv(UPDATED_CIDADE_CRLV)
            .ufCrlv(UPDATED_UF_CRLV)
            .valorPago(UPDATED_VALOR_PAGO);

        restCompraVeiculoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompraVeiculo.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCompraVeiculo))
            )
            .andExpect(status().isOk());

        // Validate the CompraVeiculo in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCompraVeiculoUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCompraVeiculo, compraVeiculo),
            getPersistedCompraVeiculo(compraVeiculo)
        );
    }

    @Test
    void fullUpdateCompraVeiculoWithPatch() throws Exception {
        // Initialize the database
        insertedCompraVeiculo = compraVeiculoRepository.save(compraVeiculo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the compraVeiculo using partial update
        CompraVeiculo partialUpdatedCompraVeiculo = new CompraVeiculo();
        partialUpdatedCompraVeiculo.setId(compraVeiculo.getId());

        partialUpdatedCompraVeiculo
            .kmEntrada(UPDATED_KM_ENTRADA)
            .valor(UPDATED_VALOR)
            .valorEstimado(UPDATED_VALOR_ESTIMADO)
            .enderecoCrlv(UPDATED_ENDERECO_CRLV)
            .cidadeCrlv(UPDATED_CIDADE_CRLV)
            .ufCrlv(UPDATED_UF_CRLV)
            .cpfCrlv(UPDATED_CPF_CRLV)
            .dataCompra(UPDATED_DATA_COMPRA)
            .condicaoPagamento(UPDATED_CONDICAO_PAGAMENTO)
            .valorPago(UPDATED_VALOR_PAGO);

        restCompraVeiculoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompraVeiculo.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCompraVeiculo))
            )
            .andExpect(status().isOk());

        // Validate the CompraVeiculo in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCompraVeiculoUpdatableFieldsEquals(partialUpdatedCompraVeiculo, getPersistedCompraVeiculo(partialUpdatedCompraVeiculo));
    }

    @Test
    void patchNonExistingCompraVeiculo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        compraVeiculo.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompraVeiculoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, compraVeiculo.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(compraVeiculo))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompraVeiculo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchCompraVeiculo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        compraVeiculo.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompraVeiculoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(compraVeiculo))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompraVeiculo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamCompraVeiculo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        compraVeiculo.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompraVeiculoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(compraVeiculo)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CompraVeiculo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteCompraVeiculo() throws Exception {
        // Initialize the database
        insertedCompraVeiculo = compraVeiculoRepository.save(compraVeiculo);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the compraVeiculo
        restCompraVeiculoMockMvc
            .perform(delete(ENTITY_API_URL_ID, compraVeiculo.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return compraVeiculoRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected CompraVeiculo getPersistedCompraVeiculo(CompraVeiculo compraVeiculo) {
        return compraVeiculoRepository.findById(compraVeiculo.getId()).orElseThrow();
    }

    protected void assertPersistedCompraVeiculoToMatchAllProperties(CompraVeiculo expectedCompraVeiculo) {
        assertCompraVeiculoAllPropertiesEquals(expectedCompraVeiculo, getPersistedCompraVeiculo(expectedCompraVeiculo));
    }

    protected void assertPersistedCompraVeiculoToMatchUpdatableProperties(CompraVeiculo expectedCompraVeiculo) {
        assertCompraVeiculoAllUpdatablePropertiesEquals(expectedCompraVeiculo, getPersistedCompraVeiculo(expectedCompraVeiculo));
    }
}
