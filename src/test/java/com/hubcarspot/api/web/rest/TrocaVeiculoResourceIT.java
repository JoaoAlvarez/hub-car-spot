package com.hubcarspot.api.web.rest;

import static com.hubcarspot.api.domain.TrocaVeiculoAsserts.*;
import static com.hubcarspot.api.web.rest.TestUtil.createUpdateProxyForBean;
import static com.hubcarspot.api.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hubcarspot.api.IntegrationTest;
import com.hubcarspot.api.domain.TrocaVeiculo;
import com.hubcarspot.api.repository.TrocaVeiculoRepository;
import com.hubcarspot.api.service.TrocaVeiculoService;
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
 * Integration tests for the {@link TrocaVeiculoResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class TrocaVeiculoResourceIT {

    private static final String DEFAULT_CARRO_ENTRADA_ID = "AAAAAAAAAA";
    private static final String UPDATED_CARRO_ENTRADA_ID = "BBBBBBBBBB";

    private static final String DEFAULT_CARRO_SAIDA_ID = "AAAAAAAAAA";
    private static final String UPDATED_CARRO_SAIDA_ID = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATA_TROCA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_TROCA = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_CONDICAO_PAGAMENTO = "AAAAAAAAAA";
    private static final String UPDATED_CONDICAO_PAGAMENTO = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_VALOR_PAGO = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALOR_PAGO = new BigDecimal(2);

    private static final BigDecimal DEFAULT_VALOR_RECEBIDO = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALOR_RECEBIDO = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/troca-veiculos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TrocaVeiculoRepository trocaVeiculoRepository;

    @Mock
    private TrocaVeiculoRepository trocaVeiculoRepositoryMock;

    @Mock
    private TrocaVeiculoService trocaVeiculoServiceMock;

    @Autowired
    private MockMvc restTrocaVeiculoMockMvc;

    private TrocaVeiculo trocaVeiculo;

    private TrocaVeiculo insertedTrocaVeiculo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TrocaVeiculo createEntity() {
        return new TrocaVeiculo()
            .carroEntradaId(DEFAULT_CARRO_ENTRADA_ID)
            .carroSaidaId(DEFAULT_CARRO_SAIDA_ID)
            .dataTroca(DEFAULT_DATA_TROCA)
            .condicaoPagamento(DEFAULT_CONDICAO_PAGAMENTO)
            .valorPago(DEFAULT_VALOR_PAGO)
            .valorRecebido(DEFAULT_VALOR_RECEBIDO);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TrocaVeiculo createUpdatedEntity() {
        return new TrocaVeiculo()
            .carroEntradaId(UPDATED_CARRO_ENTRADA_ID)
            .carroSaidaId(UPDATED_CARRO_SAIDA_ID)
            .dataTroca(UPDATED_DATA_TROCA)
            .condicaoPagamento(UPDATED_CONDICAO_PAGAMENTO)
            .valorPago(UPDATED_VALOR_PAGO)
            .valorRecebido(UPDATED_VALOR_RECEBIDO);
    }

    @BeforeEach
    public void initTest() {
        trocaVeiculo = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedTrocaVeiculo != null) {
            trocaVeiculoRepository.delete(insertedTrocaVeiculo);
            insertedTrocaVeiculo = null;
        }
    }

    @Test
    void createTrocaVeiculo() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the TrocaVeiculo
        var returnedTrocaVeiculo = om.readValue(
            restTrocaVeiculoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(trocaVeiculo)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TrocaVeiculo.class
        );

        // Validate the TrocaVeiculo in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertTrocaVeiculoUpdatableFieldsEquals(returnedTrocaVeiculo, getPersistedTrocaVeiculo(returnedTrocaVeiculo));

        insertedTrocaVeiculo = returnedTrocaVeiculo;
    }

    @Test
    void createTrocaVeiculoWithExistingId() throws Exception {
        // Create the TrocaVeiculo with an existing ID
        trocaVeiculo.setId("existing_id");

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTrocaVeiculoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(trocaVeiculo)))
            .andExpect(status().isBadRequest());

        // Validate the TrocaVeiculo in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkCarroEntradaIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        trocaVeiculo.setCarroEntradaId(null);

        // Create the TrocaVeiculo, which fails.

        restTrocaVeiculoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(trocaVeiculo)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkCarroSaidaIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        trocaVeiculo.setCarroSaidaId(null);

        // Create the TrocaVeiculo, which fails.

        restTrocaVeiculoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(trocaVeiculo)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkDataTrocaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        trocaVeiculo.setDataTroca(null);

        // Create the TrocaVeiculo, which fails.

        restTrocaVeiculoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(trocaVeiculo)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllTrocaVeiculos() throws Exception {
        // Initialize the database
        insertedTrocaVeiculo = trocaVeiculoRepository.save(trocaVeiculo);

        // Get all the trocaVeiculoList
        restTrocaVeiculoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(trocaVeiculo.getId())))
            .andExpect(jsonPath("$.[*].carroEntradaId").value(hasItem(DEFAULT_CARRO_ENTRADA_ID)))
            .andExpect(jsonPath("$.[*].carroSaidaId").value(hasItem(DEFAULT_CARRO_SAIDA_ID)))
            .andExpect(jsonPath("$.[*].dataTroca").value(hasItem(DEFAULT_DATA_TROCA.toString())))
            .andExpect(jsonPath("$.[*].condicaoPagamento").value(hasItem(DEFAULT_CONDICAO_PAGAMENTO)))
            .andExpect(jsonPath("$.[*].valorPago").value(hasItem(sameNumber(DEFAULT_VALOR_PAGO))))
            .andExpect(jsonPath("$.[*].valorRecebido").value(hasItem(sameNumber(DEFAULT_VALOR_RECEBIDO))));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTrocaVeiculosWithEagerRelationshipsIsEnabled() throws Exception {
        when(trocaVeiculoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTrocaVeiculoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(trocaVeiculoServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTrocaVeiculosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(trocaVeiculoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTrocaVeiculoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(trocaVeiculoRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void getTrocaVeiculo() throws Exception {
        // Initialize the database
        insertedTrocaVeiculo = trocaVeiculoRepository.save(trocaVeiculo);

        // Get the trocaVeiculo
        restTrocaVeiculoMockMvc
            .perform(get(ENTITY_API_URL_ID, trocaVeiculo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(trocaVeiculo.getId()))
            .andExpect(jsonPath("$.carroEntradaId").value(DEFAULT_CARRO_ENTRADA_ID))
            .andExpect(jsonPath("$.carroSaidaId").value(DEFAULT_CARRO_SAIDA_ID))
            .andExpect(jsonPath("$.dataTroca").value(DEFAULT_DATA_TROCA.toString()))
            .andExpect(jsonPath("$.condicaoPagamento").value(DEFAULT_CONDICAO_PAGAMENTO))
            .andExpect(jsonPath("$.valorPago").value(sameNumber(DEFAULT_VALOR_PAGO)))
            .andExpect(jsonPath("$.valorRecebido").value(sameNumber(DEFAULT_VALOR_RECEBIDO)));
    }

    @Test
    void getNonExistingTrocaVeiculo() throws Exception {
        // Get the trocaVeiculo
        restTrocaVeiculoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingTrocaVeiculo() throws Exception {
        // Initialize the database
        insertedTrocaVeiculo = trocaVeiculoRepository.save(trocaVeiculo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the trocaVeiculo
        TrocaVeiculo updatedTrocaVeiculo = trocaVeiculoRepository.findById(trocaVeiculo.getId()).orElseThrow();
        updatedTrocaVeiculo
            .carroEntradaId(UPDATED_CARRO_ENTRADA_ID)
            .carroSaidaId(UPDATED_CARRO_SAIDA_ID)
            .dataTroca(UPDATED_DATA_TROCA)
            .condicaoPagamento(UPDATED_CONDICAO_PAGAMENTO)
            .valorPago(UPDATED_VALOR_PAGO)
            .valorRecebido(UPDATED_VALOR_RECEBIDO);

        restTrocaVeiculoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTrocaVeiculo.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedTrocaVeiculo))
            )
            .andExpect(status().isOk());

        // Validate the TrocaVeiculo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTrocaVeiculoToMatchAllProperties(updatedTrocaVeiculo);
    }

    @Test
    void putNonExistingTrocaVeiculo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trocaVeiculo.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTrocaVeiculoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, trocaVeiculo.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(trocaVeiculo))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrocaVeiculo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchTrocaVeiculo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trocaVeiculo.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrocaVeiculoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(trocaVeiculo))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrocaVeiculo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamTrocaVeiculo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trocaVeiculo.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrocaVeiculoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(trocaVeiculo)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TrocaVeiculo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateTrocaVeiculoWithPatch() throws Exception {
        // Initialize the database
        insertedTrocaVeiculo = trocaVeiculoRepository.save(trocaVeiculo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the trocaVeiculo using partial update
        TrocaVeiculo partialUpdatedTrocaVeiculo = new TrocaVeiculo();
        partialUpdatedTrocaVeiculo.setId(trocaVeiculo.getId());

        partialUpdatedTrocaVeiculo.dataTroca(UPDATED_DATA_TROCA).condicaoPagamento(UPDATED_CONDICAO_PAGAMENTO);

        restTrocaVeiculoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTrocaVeiculo.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTrocaVeiculo))
            )
            .andExpect(status().isOk());

        // Validate the TrocaVeiculo in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTrocaVeiculoUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTrocaVeiculo, trocaVeiculo),
            getPersistedTrocaVeiculo(trocaVeiculo)
        );
    }

    @Test
    void fullUpdateTrocaVeiculoWithPatch() throws Exception {
        // Initialize the database
        insertedTrocaVeiculo = trocaVeiculoRepository.save(trocaVeiculo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the trocaVeiculo using partial update
        TrocaVeiculo partialUpdatedTrocaVeiculo = new TrocaVeiculo();
        partialUpdatedTrocaVeiculo.setId(trocaVeiculo.getId());

        partialUpdatedTrocaVeiculo
            .carroEntradaId(UPDATED_CARRO_ENTRADA_ID)
            .carroSaidaId(UPDATED_CARRO_SAIDA_ID)
            .dataTroca(UPDATED_DATA_TROCA)
            .condicaoPagamento(UPDATED_CONDICAO_PAGAMENTO)
            .valorPago(UPDATED_VALOR_PAGO)
            .valorRecebido(UPDATED_VALOR_RECEBIDO);

        restTrocaVeiculoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTrocaVeiculo.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTrocaVeiculo))
            )
            .andExpect(status().isOk());

        // Validate the TrocaVeiculo in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTrocaVeiculoUpdatableFieldsEquals(partialUpdatedTrocaVeiculo, getPersistedTrocaVeiculo(partialUpdatedTrocaVeiculo));
    }

    @Test
    void patchNonExistingTrocaVeiculo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trocaVeiculo.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTrocaVeiculoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, trocaVeiculo.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(trocaVeiculo))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrocaVeiculo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchTrocaVeiculo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trocaVeiculo.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrocaVeiculoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(trocaVeiculo))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrocaVeiculo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamTrocaVeiculo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trocaVeiculo.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrocaVeiculoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(trocaVeiculo)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TrocaVeiculo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteTrocaVeiculo() throws Exception {
        // Initialize the database
        insertedTrocaVeiculo = trocaVeiculoRepository.save(trocaVeiculo);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the trocaVeiculo
        restTrocaVeiculoMockMvc
            .perform(delete(ENTITY_API_URL_ID, trocaVeiculo.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return trocaVeiculoRepository.count();
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

    protected TrocaVeiculo getPersistedTrocaVeiculo(TrocaVeiculo trocaVeiculo) {
        return trocaVeiculoRepository.findById(trocaVeiculo.getId()).orElseThrow();
    }

    protected void assertPersistedTrocaVeiculoToMatchAllProperties(TrocaVeiculo expectedTrocaVeiculo) {
        assertTrocaVeiculoAllPropertiesEquals(expectedTrocaVeiculo, getPersistedTrocaVeiculo(expectedTrocaVeiculo));
    }

    protected void assertPersistedTrocaVeiculoToMatchUpdatableProperties(TrocaVeiculo expectedTrocaVeiculo) {
        assertTrocaVeiculoAllUpdatablePropertiesEquals(expectedTrocaVeiculo, getPersistedTrocaVeiculo(expectedTrocaVeiculo));
    }
}
