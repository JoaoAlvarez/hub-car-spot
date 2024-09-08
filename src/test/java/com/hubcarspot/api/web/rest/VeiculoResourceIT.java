package com.hubcarspot.api.web.rest;

import static com.hubcarspot.api.domain.VeiculoAsserts.*;
import static com.hubcarspot.api.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hubcarspot.api.IntegrationTest;
import com.hubcarspot.api.domain.Veiculo;
import com.hubcarspot.api.domain.enumeration.EspecieVeiculo;
import com.hubcarspot.api.domain.enumeration.StatusVeiculo;
import com.hubcarspot.api.repository.VeiculoRepository;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Integration tests for the {@link VeiculoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VeiculoResourceIT {

    private static final LocalDate DEFAULT_CREATED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_AT = LocalDate.now(ZoneId.systemDefault());

    private static final EspecieVeiculo DEFAULT_ESPECIE = EspecieVeiculo.CARRO;
    private static final EspecieVeiculo UPDATED_ESPECIE = EspecieVeiculo.MOTO;

    private static final String DEFAULT_PLACA = "AAAAAAAAAA";
    private static final String UPDATED_PLACA = "BBBBBBBBBB";

    private static final String DEFAULT_MARCA = "AAAAAAAAAA";
    private static final String UPDATED_MARCA = "BBBBBBBBBB";

    private static final String DEFAULT_MODELO = "AAAAAAAAAA";
    private static final String UPDATED_MODELO = "BBBBBBBBBB";

    private static final Integer DEFAULT_ANO_FABRICACAO = 1;
    private static final Integer UPDATED_ANO_FABRICACAO = 2;

    private static final Integer DEFAULT_ANO_MODELO = 1;
    private static final Integer UPDATED_ANO_MODELO = 2;

    private static final String DEFAULT_COR = "AAAAAAAAAA";
    private static final String UPDATED_COR = "BBBBBBBBBB";

    private static final String DEFAULT_COMBUSTIVEL = "AAAAAAAAAA";
    private static final String UPDATED_COMBUSTIVEL = "BBBBBBBBBB";

    private static final String DEFAULT_CAMBIO = "AAAAAAAAAA";
    private static final String UPDATED_CAMBIO = "BBBBBBBBBB";

    private static final StatusVeiculo DEFAULT_STATUS = StatusVeiculo.NOVO;
    private static final StatusVeiculo UPDATED_STATUS = StatusVeiculo.USADO;

    private static final String DEFAULT_CHASSI = "AAAAAAAAAA";
    private static final String UPDATED_CHASSI = "BBBBBBBBBB";

    private static final String DEFAULT_RENAVAM = "AAAAAAAAAA";
    private static final String UPDATED_RENAVAM = "BBBBBBBBBB";

    private static final String DEFAULT_NUMERO_MOTOR = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO_MOTOR = "BBBBBBBBBB";

    private static final String DEFAULT_NUMERO_CAMBIO = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO_CAMBIO = "BBBBBBBBBB";

    private static final Integer DEFAULT_QUILOMETRAEGEM = 1;
    private static final Integer UPDATED_QUILOMETRAEGEM = 2;

    private static final Integer DEFAULT_KM_SAIDA = 1;
    private static final Integer UPDATED_KM_SAIDA = 2;

    private static final String DEFAULT_CAVALOS = "AAAAAAAAAA";
    private static final String UPDATED_CAVALOS = "BBBBBBBBBB";

    private static final String DEFAULT_MOTORIZACAO = "AAAAAAAAAA";
    private static final String UPDATED_MOTORIZACAO = "BBBBBBBBBB";

    private static final String DEFAULT_ADICIONAL = "AAAAAAAAAA";
    private static final String UPDATED_ADICIONAL = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRITIVO_CURTO_ACESSORIOS = "AAAAAAAAAA";
    private static final String UPDATED_DESCRITIVO_CURTO_ACESSORIOS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/veiculos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private VeiculoRepository veiculoRepository;

    @Autowired
    private MockMvc restVeiculoMockMvc;

    private Veiculo veiculo;

    private Veiculo insertedVeiculo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Veiculo createEntity() {
        return new Veiculo()
            .createdAt(DEFAULT_CREATED_AT)
            .especie(DEFAULT_ESPECIE)
            .placa(DEFAULT_PLACA)
            .marca(DEFAULT_MARCA)
            .modelo(DEFAULT_MODELO)
            .anoFabricacao(DEFAULT_ANO_FABRICACAO)
            .anoModelo(DEFAULT_ANO_MODELO)
            .cor(DEFAULT_COR)
            .combustivel(DEFAULT_COMBUSTIVEL)
            .cambio(DEFAULT_CAMBIO)
            .status(DEFAULT_STATUS)
            .chassi(DEFAULT_CHASSI)
            .renavam(DEFAULT_RENAVAM)
            .numeroMotor(DEFAULT_NUMERO_MOTOR)
            .numeroCambio(DEFAULT_NUMERO_CAMBIO)
            .quilometraegem(DEFAULT_QUILOMETRAEGEM)
            .kmSaida(DEFAULT_KM_SAIDA)
            .cavalos(DEFAULT_CAVALOS)
            .motorizacao(DEFAULT_MOTORIZACAO)
            .adicional(DEFAULT_ADICIONAL)
            .descritivoCurtoAcessorios(DEFAULT_DESCRITIVO_CURTO_ACESSORIOS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Veiculo createUpdatedEntity() {
        return new Veiculo()
            .createdAt(UPDATED_CREATED_AT)
            .especie(UPDATED_ESPECIE)
            .placa(UPDATED_PLACA)
            .marca(UPDATED_MARCA)
            .modelo(UPDATED_MODELO)
            .anoFabricacao(UPDATED_ANO_FABRICACAO)
            .anoModelo(UPDATED_ANO_MODELO)
            .cor(UPDATED_COR)
            .combustivel(UPDATED_COMBUSTIVEL)
            .cambio(UPDATED_CAMBIO)
            .status(UPDATED_STATUS)
            .chassi(UPDATED_CHASSI)
            .renavam(UPDATED_RENAVAM)
            .numeroMotor(UPDATED_NUMERO_MOTOR)
            .numeroCambio(UPDATED_NUMERO_CAMBIO)
            .quilometraegem(UPDATED_QUILOMETRAEGEM)
            .kmSaida(UPDATED_KM_SAIDA)
            .cavalos(UPDATED_CAVALOS)
            .motorizacao(UPDATED_MOTORIZACAO)
            .adicional(UPDATED_ADICIONAL)
            .descritivoCurtoAcessorios(UPDATED_DESCRITIVO_CURTO_ACESSORIOS);
    }

    @BeforeEach
    public void initTest() {
        veiculo = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedVeiculo != null) {
            veiculoRepository.delete(insertedVeiculo);
            insertedVeiculo = null;
        }
    }

    @Test
    void createVeiculo() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Veiculo
        var returnedVeiculo = om.readValue(
            restVeiculoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(veiculo)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Veiculo.class
        );

        // Validate the Veiculo in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertVeiculoUpdatableFieldsEquals(returnedVeiculo, getPersistedVeiculo(returnedVeiculo));

        insertedVeiculo = returnedVeiculo;
    }

    @Test
    void createVeiculoWithExistingId() throws Exception {
        // Create the Veiculo with an existing ID
        veiculo.setId("existing_id");

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVeiculoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(veiculo)))
            .andExpect(status().isBadRequest());

        // Validate the Veiculo in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkCreatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        veiculo.setCreatedAt(null);

        // Create the Veiculo, which fails.

        restVeiculoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(veiculo)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkEspecieIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        veiculo.setEspecie(null);

        // Create the Veiculo, which fails.

        restVeiculoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(veiculo)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkPlacaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        veiculo.setPlaca(null);

        // Create the Veiculo, which fails.

        restVeiculoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(veiculo)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkMarcaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        veiculo.setMarca(null);

        // Create the Veiculo, which fails.

        restVeiculoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(veiculo)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkAnoFabricacaoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        veiculo.setAnoFabricacao(null);

        // Create the Veiculo, which fails.

        restVeiculoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(veiculo)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkAnoModeloIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        veiculo.setAnoModelo(null);

        // Create the Veiculo, which fails.

        restVeiculoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(veiculo)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkCombustivelIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        veiculo.setCombustivel(null);

        // Create the Veiculo, which fails.

        restVeiculoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(veiculo)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllVeiculos() throws Exception {
        // Initialize the database
        insertedVeiculo = veiculoRepository.save(veiculo);

        // Get all the veiculoList
        restVeiculoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(veiculo.getId())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].especie").value(hasItem(DEFAULT_ESPECIE.toString())))
            .andExpect(jsonPath("$.[*].placa").value(hasItem(DEFAULT_PLACA)))
            .andExpect(jsonPath("$.[*].marca").value(hasItem(DEFAULT_MARCA)))
            .andExpect(jsonPath("$.[*].modelo").value(hasItem(DEFAULT_MODELO)))
            .andExpect(jsonPath("$.[*].anoFabricacao").value(hasItem(DEFAULT_ANO_FABRICACAO)))
            .andExpect(jsonPath("$.[*].anoModelo").value(hasItem(DEFAULT_ANO_MODELO)))
            .andExpect(jsonPath("$.[*].cor").value(hasItem(DEFAULT_COR)))
            .andExpect(jsonPath("$.[*].combustivel").value(hasItem(DEFAULT_COMBUSTIVEL)))
            .andExpect(jsonPath("$.[*].cambio").value(hasItem(DEFAULT_CAMBIO)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].chassi").value(hasItem(DEFAULT_CHASSI)))
            .andExpect(jsonPath("$.[*].renavam").value(hasItem(DEFAULT_RENAVAM)))
            .andExpect(jsonPath("$.[*].numeroMotor").value(hasItem(DEFAULT_NUMERO_MOTOR)))
            .andExpect(jsonPath("$.[*].numeroCambio").value(hasItem(DEFAULT_NUMERO_CAMBIO)))
            .andExpect(jsonPath("$.[*].quilometraegem").value(hasItem(DEFAULT_QUILOMETRAEGEM)))
            .andExpect(jsonPath("$.[*].kmSaida").value(hasItem(DEFAULT_KM_SAIDA)))
            .andExpect(jsonPath("$.[*].cavalos").value(hasItem(DEFAULT_CAVALOS)))
            .andExpect(jsonPath("$.[*].motorizacao").value(hasItem(DEFAULT_MOTORIZACAO)))
            .andExpect(jsonPath("$.[*].adicional").value(hasItem(DEFAULT_ADICIONAL)))
            .andExpect(jsonPath("$.[*].descritivoCurtoAcessorios").value(hasItem(DEFAULT_DESCRITIVO_CURTO_ACESSORIOS)));
    }

    @Test
    void getVeiculo() throws Exception {
        // Initialize the database
        insertedVeiculo = veiculoRepository.save(veiculo);

        // Get the veiculo
        restVeiculoMockMvc
            .perform(get(ENTITY_API_URL_ID, veiculo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(veiculo.getId()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.especie").value(DEFAULT_ESPECIE.toString()))
            .andExpect(jsonPath("$.placa").value(DEFAULT_PLACA))
            .andExpect(jsonPath("$.marca").value(DEFAULT_MARCA))
            .andExpect(jsonPath("$.modelo").value(DEFAULT_MODELO))
            .andExpect(jsonPath("$.anoFabricacao").value(DEFAULT_ANO_FABRICACAO))
            .andExpect(jsonPath("$.anoModelo").value(DEFAULT_ANO_MODELO))
            .andExpect(jsonPath("$.cor").value(DEFAULT_COR))
            .andExpect(jsonPath("$.combustivel").value(DEFAULT_COMBUSTIVEL))
            .andExpect(jsonPath("$.cambio").value(DEFAULT_CAMBIO))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.chassi").value(DEFAULT_CHASSI))
            .andExpect(jsonPath("$.renavam").value(DEFAULT_RENAVAM))
            .andExpect(jsonPath("$.numeroMotor").value(DEFAULT_NUMERO_MOTOR))
            .andExpect(jsonPath("$.numeroCambio").value(DEFAULT_NUMERO_CAMBIO))
            .andExpect(jsonPath("$.quilometraegem").value(DEFAULT_QUILOMETRAEGEM))
            .andExpect(jsonPath("$.kmSaida").value(DEFAULT_KM_SAIDA))
            .andExpect(jsonPath("$.cavalos").value(DEFAULT_CAVALOS))
            .andExpect(jsonPath("$.motorizacao").value(DEFAULT_MOTORIZACAO))
            .andExpect(jsonPath("$.adicional").value(DEFAULT_ADICIONAL))
            .andExpect(jsonPath("$.descritivoCurtoAcessorios").value(DEFAULT_DESCRITIVO_CURTO_ACESSORIOS));
    }

    @Test
    void getNonExistingVeiculo() throws Exception {
        // Get the veiculo
        restVeiculoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingVeiculo() throws Exception {
        // Initialize the database
        insertedVeiculo = veiculoRepository.save(veiculo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the veiculo
        Veiculo updatedVeiculo = veiculoRepository.findById(veiculo.getId()).orElseThrow();
        updatedVeiculo
            .createdAt(UPDATED_CREATED_AT)
            .especie(UPDATED_ESPECIE)
            .placa(UPDATED_PLACA)
            .marca(UPDATED_MARCA)
            .modelo(UPDATED_MODELO)
            .anoFabricacao(UPDATED_ANO_FABRICACAO)
            .anoModelo(UPDATED_ANO_MODELO)
            .cor(UPDATED_COR)
            .combustivel(UPDATED_COMBUSTIVEL)
            .cambio(UPDATED_CAMBIO)
            .status(UPDATED_STATUS)
            .chassi(UPDATED_CHASSI)
            .renavam(UPDATED_RENAVAM)
            .numeroMotor(UPDATED_NUMERO_MOTOR)
            .numeroCambio(UPDATED_NUMERO_CAMBIO)
            .quilometraegem(UPDATED_QUILOMETRAEGEM)
            .kmSaida(UPDATED_KM_SAIDA)
            .cavalos(UPDATED_CAVALOS)
            .motorizacao(UPDATED_MOTORIZACAO)
            .adicional(UPDATED_ADICIONAL)
            .descritivoCurtoAcessorios(UPDATED_DESCRITIVO_CURTO_ACESSORIOS);

        restVeiculoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedVeiculo.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedVeiculo))
            )
            .andExpect(status().isOk());

        // Validate the Veiculo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedVeiculoToMatchAllProperties(updatedVeiculo);
    }

    @Test
    void putNonExistingVeiculo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        veiculo.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVeiculoMockMvc
            .perform(put(ENTITY_API_URL_ID, veiculo.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(veiculo)))
            .andExpect(status().isBadRequest());

        // Validate the Veiculo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchVeiculo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        veiculo.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVeiculoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(veiculo))
            )
            .andExpect(status().isBadRequest());

        // Validate the Veiculo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamVeiculo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        veiculo.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVeiculoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(veiculo)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Veiculo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateVeiculoWithPatch() throws Exception {
        // Initialize the database
        insertedVeiculo = veiculoRepository.save(veiculo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the veiculo using partial update
        Veiculo partialUpdatedVeiculo = new Veiculo();
        partialUpdatedVeiculo.setId(veiculo.getId());

        partialUpdatedVeiculo
            .marca(UPDATED_MARCA)
            .anoFabricacao(UPDATED_ANO_FABRICACAO)
            .cor(UPDATED_COR)
            .cambio(UPDATED_CAMBIO)
            .numeroMotor(UPDATED_NUMERO_MOTOR)
            .numeroCambio(UPDATED_NUMERO_CAMBIO)
            .motorizacao(UPDATED_MOTORIZACAO);

        restVeiculoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVeiculo.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVeiculo))
            )
            .andExpect(status().isOk());

        // Validate the Veiculo in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVeiculoUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedVeiculo, veiculo), getPersistedVeiculo(veiculo));
    }

    @Test
    void fullUpdateVeiculoWithPatch() throws Exception {
        // Initialize the database
        insertedVeiculo = veiculoRepository.save(veiculo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the veiculo using partial update
        Veiculo partialUpdatedVeiculo = new Veiculo();
        partialUpdatedVeiculo.setId(veiculo.getId());

        partialUpdatedVeiculo
            .createdAt(UPDATED_CREATED_AT)
            .especie(UPDATED_ESPECIE)
            .placa(UPDATED_PLACA)
            .marca(UPDATED_MARCA)
            .modelo(UPDATED_MODELO)
            .anoFabricacao(UPDATED_ANO_FABRICACAO)
            .anoModelo(UPDATED_ANO_MODELO)
            .cor(UPDATED_COR)
            .combustivel(UPDATED_COMBUSTIVEL)
            .cambio(UPDATED_CAMBIO)
            .status(UPDATED_STATUS)
            .chassi(UPDATED_CHASSI)
            .renavam(UPDATED_RENAVAM)
            .numeroMotor(UPDATED_NUMERO_MOTOR)
            .numeroCambio(UPDATED_NUMERO_CAMBIO)
            .quilometraegem(UPDATED_QUILOMETRAEGEM)
            .kmSaida(UPDATED_KM_SAIDA)
            .cavalos(UPDATED_CAVALOS)
            .motorizacao(UPDATED_MOTORIZACAO)
            .adicional(UPDATED_ADICIONAL)
            .descritivoCurtoAcessorios(UPDATED_DESCRITIVO_CURTO_ACESSORIOS);

        restVeiculoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVeiculo.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVeiculo))
            )
            .andExpect(status().isOk());

        // Validate the Veiculo in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVeiculoUpdatableFieldsEquals(partialUpdatedVeiculo, getPersistedVeiculo(partialUpdatedVeiculo));
    }

    @Test
    void patchNonExistingVeiculo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        veiculo.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVeiculoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, veiculo.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(veiculo))
            )
            .andExpect(status().isBadRequest());

        // Validate the Veiculo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchVeiculo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        veiculo.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVeiculoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(veiculo))
            )
            .andExpect(status().isBadRequest());

        // Validate the Veiculo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamVeiculo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        veiculo.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVeiculoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(veiculo)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Veiculo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteVeiculo() throws Exception {
        // Initialize the database
        insertedVeiculo = veiculoRepository.save(veiculo);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the veiculo
        restVeiculoMockMvc
            .perform(delete(ENTITY_API_URL_ID, veiculo.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return veiculoRepository.count();
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

    protected Veiculo getPersistedVeiculo(Veiculo veiculo) {
        return veiculoRepository.findById(veiculo.getId()).orElseThrow();
    }

    protected void assertPersistedVeiculoToMatchAllProperties(Veiculo expectedVeiculo) {
        assertVeiculoAllPropertiesEquals(expectedVeiculo, getPersistedVeiculo(expectedVeiculo));
    }

    protected void assertPersistedVeiculoToMatchUpdatableProperties(Veiculo expectedVeiculo) {
        assertVeiculoAllUpdatablePropertiesEquals(expectedVeiculo, getPersistedVeiculo(expectedVeiculo));
    }
}
