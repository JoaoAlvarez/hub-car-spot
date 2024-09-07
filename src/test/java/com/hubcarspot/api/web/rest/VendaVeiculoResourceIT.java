package com.hubcarspot.api.web.rest;

import static com.hubcarspot.api.domain.VendaVeiculoAsserts.*;
import static com.hubcarspot.api.web.rest.TestUtil.createUpdateProxyForBean;
import static com.hubcarspot.api.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hubcarspot.api.IntegrationTest;
import com.hubcarspot.api.domain.VendaVeiculo;
import com.hubcarspot.api.repository.VendaVeiculoRepository;
import com.hubcarspot.api.service.VendaVeiculoService;
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
 * Integration tests for the {@link VendaVeiculoResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class VendaVeiculoResourceIT {

    private static final Integer DEFAULT_KM_SAIDA = 1;
    private static final Integer UPDATED_KM_SAIDA = 2;

    private static final BigDecimal DEFAULT_VALOR_COMPRA = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALOR_COMPRA = new BigDecimal(2);

    private static final BigDecimal DEFAULT_VALOR_TABELA = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALOR_TABELA = new BigDecimal(2);

    private static final BigDecimal DEFAULT_VALOR_VENDA = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALOR_VENDA = new BigDecimal(2);

    private static final LocalDate DEFAULT_DATA_VENDA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_VENDA = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_CONDICAO_RECEBIMENTO = "AAAAAAAAAA";
    private static final String UPDATED_CONDICAO_RECEBIMENTO = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_VALOR_ENTRADA = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALOR_ENTRADA = new BigDecimal(2);

    private static final BigDecimal DEFAULT_VALOR_FINANCIADO = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALOR_FINANCIADO = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/venda-veiculos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private VendaVeiculoRepository vendaVeiculoRepository;

    @Mock
    private VendaVeiculoRepository vendaVeiculoRepositoryMock;

    @Mock
    private VendaVeiculoService vendaVeiculoServiceMock;

    @Autowired
    private MockMvc restVendaVeiculoMockMvc;

    private VendaVeiculo vendaVeiculo;

    private VendaVeiculo insertedVendaVeiculo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VendaVeiculo createEntity() {
        return new VendaVeiculo()
            .kmSaida(DEFAULT_KM_SAIDA)
            .valorCompra(DEFAULT_VALOR_COMPRA)
            .valorTabela(DEFAULT_VALOR_TABELA)
            .valorVenda(DEFAULT_VALOR_VENDA)
            .dataVenda(DEFAULT_DATA_VENDA)
            .condicaoRecebimento(DEFAULT_CONDICAO_RECEBIMENTO)
            .valorEntrada(DEFAULT_VALOR_ENTRADA)
            .valorFinanciado(DEFAULT_VALOR_FINANCIADO);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VendaVeiculo createUpdatedEntity() {
        return new VendaVeiculo()
            .kmSaida(UPDATED_KM_SAIDA)
            .valorCompra(UPDATED_VALOR_COMPRA)
            .valorTabela(UPDATED_VALOR_TABELA)
            .valorVenda(UPDATED_VALOR_VENDA)
            .dataVenda(UPDATED_DATA_VENDA)
            .condicaoRecebimento(UPDATED_CONDICAO_RECEBIMENTO)
            .valorEntrada(UPDATED_VALOR_ENTRADA)
            .valorFinanciado(UPDATED_VALOR_FINANCIADO);
    }

    @BeforeEach
    public void initTest() {
        vendaVeiculo = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedVendaVeiculo != null) {
            vendaVeiculoRepository.delete(insertedVendaVeiculo);
            insertedVendaVeiculo = null;
        }
    }

    @Test
    void createVendaVeiculo() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the VendaVeiculo
        var returnedVendaVeiculo = om.readValue(
            restVendaVeiculoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vendaVeiculo)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            VendaVeiculo.class
        );

        // Validate the VendaVeiculo in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertVendaVeiculoUpdatableFieldsEquals(returnedVendaVeiculo, getPersistedVendaVeiculo(returnedVendaVeiculo));

        insertedVendaVeiculo = returnedVendaVeiculo;
    }

    @Test
    void createVendaVeiculoWithExistingId() throws Exception {
        // Create the VendaVeiculo with an existing ID
        vendaVeiculo.setId("existing_id");

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVendaVeiculoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vendaVeiculo)))
            .andExpect(status().isBadRequest());

        // Validate the VendaVeiculo in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkKmSaidaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        vendaVeiculo.setKmSaida(null);

        // Create the VendaVeiculo, which fails.

        restVendaVeiculoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vendaVeiculo)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkValorCompraIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        vendaVeiculo.setValorCompra(null);

        // Create the VendaVeiculo, which fails.

        restVendaVeiculoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vendaVeiculo)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkValorVendaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        vendaVeiculo.setValorVenda(null);

        // Create the VendaVeiculo, which fails.

        restVendaVeiculoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vendaVeiculo)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkDataVendaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        vendaVeiculo.setDataVenda(null);

        // Create the VendaVeiculo, which fails.

        restVendaVeiculoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vendaVeiculo)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkCondicaoRecebimentoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        vendaVeiculo.setCondicaoRecebimento(null);

        // Create the VendaVeiculo, which fails.

        restVendaVeiculoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vendaVeiculo)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllVendaVeiculos() throws Exception {
        // Initialize the database
        insertedVendaVeiculo = vendaVeiculoRepository.save(vendaVeiculo);

        // Get all the vendaVeiculoList
        restVendaVeiculoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vendaVeiculo.getId())))
            .andExpect(jsonPath("$.[*].kmSaida").value(hasItem(DEFAULT_KM_SAIDA)))
            .andExpect(jsonPath("$.[*].valorCompra").value(hasItem(sameNumber(DEFAULT_VALOR_COMPRA))))
            .andExpect(jsonPath("$.[*].valorTabela").value(hasItem(sameNumber(DEFAULT_VALOR_TABELA))))
            .andExpect(jsonPath("$.[*].valorVenda").value(hasItem(sameNumber(DEFAULT_VALOR_VENDA))))
            .andExpect(jsonPath("$.[*].dataVenda").value(hasItem(DEFAULT_DATA_VENDA.toString())))
            .andExpect(jsonPath("$.[*].condicaoRecebimento").value(hasItem(DEFAULT_CONDICAO_RECEBIMENTO)))
            .andExpect(jsonPath("$.[*].valorEntrada").value(hasItem(sameNumber(DEFAULT_VALOR_ENTRADA))))
            .andExpect(jsonPath("$.[*].valorFinanciado").value(hasItem(sameNumber(DEFAULT_VALOR_FINANCIADO))));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllVendaVeiculosWithEagerRelationshipsIsEnabled() throws Exception {
        when(vendaVeiculoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restVendaVeiculoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(vendaVeiculoServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllVendaVeiculosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(vendaVeiculoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restVendaVeiculoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(vendaVeiculoRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void getVendaVeiculo() throws Exception {
        // Initialize the database
        insertedVendaVeiculo = vendaVeiculoRepository.save(vendaVeiculo);

        // Get the vendaVeiculo
        restVendaVeiculoMockMvc
            .perform(get(ENTITY_API_URL_ID, vendaVeiculo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vendaVeiculo.getId()))
            .andExpect(jsonPath("$.kmSaida").value(DEFAULT_KM_SAIDA))
            .andExpect(jsonPath("$.valorCompra").value(sameNumber(DEFAULT_VALOR_COMPRA)))
            .andExpect(jsonPath("$.valorTabela").value(sameNumber(DEFAULT_VALOR_TABELA)))
            .andExpect(jsonPath("$.valorVenda").value(sameNumber(DEFAULT_VALOR_VENDA)))
            .andExpect(jsonPath("$.dataVenda").value(DEFAULT_DATA_VENDA.toString()))
            .andExpect(jsonPath("$.condicaoRecebimento").value(DEFAULT_CONDICAO_RECEBIMENTO))
            .andExpect(jsonPath("$.valorEntrada").value(sameNumber(DEFAULT_VALOR_ENTRADA)))
            .andExpect(jsonPath("$.valorFinanciado").value(sameNumber(DEFAULT_VALOR_FINANCIADO)));
    }

    @Test
    void getNonExistingVendaVeiculo() throws Exception {
        // Get the vendaVeiculo
        restVendaVeiculoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingVendaVeiculo() throws Exception {
        // Initialize the database
        insertedVendaVeiculo = vendaVeiculoRepository.save(vendaVeiculo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vendaVeiculo
        VendaVeiculo updatedVendaVeiculo = vendaVeiculoRepository.findById(vendaVeiculo.getId()).orElseThrow();
        updatedVendaVeiculo
            .kmSaida(UPDATED_KM_SAIDA)
            .valorCompra(UPDATED_VALOR_COMPRA)
            .valorTabela(UPDATED_VALOR_TABELA)
            .valorVenda(UPDATED_VALOR_VENDA)
            .dataVenda(UPDATED_DATA_VENDA)
            .condicaoRecebimento(UPDATED_CONDICAO_RECEBIMENTO)
            .valorEntrada(UPDATED_VALOR_ENTRADA)
            .valorFinanciado(UPDATED_VALOR_FINANCIADO);

        restVendaVeiculoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedVendaVeiculo.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedVendaVeiculo))
            )
            .andExpect(status().isOk());

        // Validate the VendaVeiculo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedVendaVeiculoToMatchAllProperties(updatedVendaVeiculo);
    }

    @Test
    void putNonExistingVendaVeiculo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vendaVeiculo.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVendaVeiculoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vendaVeiculo.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(vendaVeiculo))
            )
            .andExpect(status().isBadRequest());

        // Validate the VendaVeiculo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchVendaVeiculo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vendaVeiculo.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVendaVeiculoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(vendaVeiculo))
            )
            .andExpect(status().isBadRequest());

        // Validate the VendaVeiculo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamVendaVeiculo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vendaVeiculo.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVendaVeiculoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vendaVeiculo)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the VendaVeiculo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateVendaVeiculoWithPatch() throws Exception {
        // Initialize the database
        insertedVendaVeiculo = vendaVeiculoRepository.save(vendaVeiculo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vendaVeiculo using partial update
        VendaVeiculo partialUpdatedVendaVeiculo = new VendaVeiculo();
        partialUpdatedVendaVeiculo.setId(vendaVeiculo.getId());

        partialUpdatedVendaVeiculo.valorTabela(UPDATED_VALOR_TABELA).valorVenda(UPDATED_VALOR_VENDA).dataVenda(UPDATED_DATA_VENDA);

        restVendaVeiculoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVendaVeiculo.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVendaVeiculo))
            )
            .andExpect(status().isOk());

        // Validate the VendaVeiculo in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVendaVeiculoUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedVendaVeiculo, vendaVeiculo),
            getPersistedVendaVeiculo(vendaVeiculo)
        );
    }

    @Test
    void fullUpdateVendaVeiculoWithPatch() throws Exception {
        // Initialize the database
        insertedVendaVeiculo = vendaVeiculoRepository.save(vendaVeiculo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vendaVeiculo using partial update
        VendaVeiculo partialUpdatedVendaVeiculo = new VendaVeiculo();
        partialUpdatedVendaVeiculo.setId(vendaVeiculo.getId());

        partialUpdatedVendaVeiculo
            .kmSaida(UPDATED_KM_SAIDA)
            .valorCompra(UPDATED_VALOR_COMPRA)
            .valorTabela(UPDATED_VALOR_TABELA)
            .valorVenda(UPDATED_VALOR_VENDA)
            .dataVenda(UPDATED_DATA_VENDA)
            .condicaoRecebimento(UPDATED_CONDICAO_RECEBIMENTO)
            .valorEntrada(UPDATED_VALOR_ENTRADA)
            .valorFinanciado(UPDATED_VALOR_FINANCIADO);

        restVendaVeiculoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVendaVeiculo.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVendaVeiculo))
            )
            .andExpect(status().isOk());

        // Validate the VendaVeiculo in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVendaVeiculoUpdatableFieldsEquals(partialUpdatedVendaVeiculo, getPersistedVendaVeiculo(partialUpdatedVendaVeiculo));
    }

    @Test
    void patchNonExistingVendaVeiculo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vendaVeiculo.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVendaVeiculoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, vendaVeiculo.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(vendaVeiculo))
            )
            .andExpect(status().isBadRequest());

        // Validate the VendaVeiculo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchVendaVeiculo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vendaVeiculo.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVendaVeiculoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(vendaVeiculo))
            )
            .andExpect(status().isBadRequest());

        // Validate the VendaVeiculo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamVendaVeiculo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vendaVeiculo.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVendaVeiculoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(vendaVeiculo)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the VendaVeiculo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteVendaVeiculo() throws Exception {
        // Initialize the database
        insertedVendaVeiculo = vendaVeiculoRepository.save(vendaVeiculo);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the vendaVeiculo
        restVendaVeiculoMockMvc
            .perform(delete(ENTITY_API_URL_ID, vendaVeiculo.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return vendaVeiculoRepository.count();
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

    protected VendaVeiculo getPersistedVendaVeiculo(VendaVeiculo vendaVeiculo) {
        return vendaVeiculoRepository.findById(vendaVeiculo.getId()).orElseThrow();
    }

    protected void assertPersistedVendaVeiculoToMatchAllProperties(VendaVeiculo expectedVendaVeiculo) {
        assertVendaVeiculoAllPropertiesEquals(expectedVendaVeiculo, getPersistedVendaVeiculo(expectedVendaVeiculo));
    }

    protected void assertPersistedVendaVeiculoToMatchUpdatableProperties(VendaVeiculo expectedVendaVeiculo) {
        assertVendaVeiculoAllUpdatablePropertiesEquals(expectedVendaVeiculo, getPersistedVendaVeiculo(expectedVendaVeiculo));
    }
}
