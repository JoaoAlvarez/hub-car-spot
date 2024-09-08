package com.hubcarspot.api.web.rest;

import static com.hubcarspot.api.domain.TaxasAsserts.*;
import static com.hubcarspot.api.web.rest.TestUtil.createUpdateProxyForBean;
import static com.hubcarspot.api.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hubcarspot.api.IntegrationTest;
import com.hubcarspot.api.domain.Taxas;
import com.hubcarspot.api.repository.TaxasRepository;
import com.hubcarspot.api.service.TaxasService;
import java.math.BigDecimal;
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
 * Integration tests for the {@link TaxasResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class TaxasResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_VALOR = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALOR = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/taxas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TaxasRepository taxasRepository;

    @Mock
    private TaxasRepository taxasRepositoryMock;

    @Mock
    private TaxasService taxasServiceMock;

    @Autowired
    private MockMvc restTaxasMockMvc;

    private Taxas taxas;

    private Taxas insertedTaxas;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Taxas createEntity() {
        return new Taxas().nome(DEFAULT_NOME).valor(DEFAULT_VALOR);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Taxas createUpdatedEntity() {
        return new Taxas().nome(UPDATED_NOME).valor(UPDATED_VALOR);
    }

    @BeforeEach
    public void initTest() {
        taxas = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedTaxas != null) {
            taxasRepository.delete(insertedTaxas);
            insertedTaxas = null;
        }
    }

    @Test
    void createTaxas() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Taxas
        var returnedTaxas = om.readValue(
            restTaxasMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(taxas)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Taxas.class
        );

        // Validate the Taxas in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertTaxasUpdatableFieldsEquals(returnedTaxas, getPersistedTaxas(returnedTaxas));

        insertedTaxas = returnedTaxas;
    }

    @Test
    void createTaxasWithExistingId() throws Exception {
        // Create the Taxas with an existing ID
        taxas.setId("existing_id");

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTaxasMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(taxas)))
            .andExpect(status().isBadRequest());

        // Validate the Taxas in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void getAllTaxas() throws Exception {
        // Initialize the database
        insertedTaxas = taxasRepository.save(taxas);

        // Get all the taxasList
        restTaxasMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(taxas.getId())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].valor").value(hasItem(sameNumber(DEFAULT_VALOR))));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTaxasWithEagerRelationshipsIsEnabled() throws Exception {
        when(taxasServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTaxasMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(taxasServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTaxasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(taxasServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTaxasMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(taxasRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void getTaxas() throws Exception {
        // Initialize the database
        insertedTaxas = taxasRepository.save(taxas);

        // Get the taxas
        restTaxasMockMvc
            .perform(get(ENTITY_API_URL_ID, taxas.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(taxas.getId()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.valor").value(sameNumber(DEFAULT_VALOR)));
    }

    @Test
    void getNonExistingTaxas() throws Exception {
        // Get the taxas
        restTaxasMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingTaxas() throws Exception {
        // Initialize the database
        insertedTaxas = taxasRepository.save(taxas);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the taxas
        Taxas updatedTaxas = taxasRepository.findById(taxas.getId()).orElseThrow();
        updatedTaxas.nome(UPDATED_NOME).valor(UPDATED_VALOR);

        restTaxasMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTaxas.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedTaxas))
            )
            .andExpect(status().isOk());

        // Validate the Taxas in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTaxasToMatchAllProperties(updatedTaxas);
    }

    @Test
    void putNonExistingTaxas() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        taxas.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTaxasMockMvc
            .perform(put(ENTITY_API_URL_ID, taxas.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(taxas)))
            .andExpect(status().isBadRequest());

        // Validate the Taxas in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchTaxas() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        taxas.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaxasMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(taxas))
            )
            .andExpect(status().isBadRequest());

        // Validate the Taxas in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamTaxas() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        taxas.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaxasMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(taxas)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Taxas in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateTaxasWithPatch() throws Exception {
        // Initialize the database
        insertedTaxas = taxasRepository.save(taxas);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the taxas using partial update
        Taxas partialUpdatedTaxas = new Taxas();
        partialUpdatedTaxas.setId(taxas.getId());

        restTaxasMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTaxas.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTaxas))
            )
            .andExpect(status().isOk());

        // Validate the Taxas in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTaxasUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedTaxas, taxas), getPersistedTaxas(taxas));
    }

    @Test
    void fullUpdateTaxasWithPatch() throws Exception {
        // Initialize the database
        insertedTaxas = taxasRepository.save(taxas);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the taxas using partial update
        Taxas partialUpdatedTaxas = new Taxas();
        partialUpdatedTaxas.setId(taxas.getId());

        partialUpdatedTaxas.nome(UPDATED_NOME).valor(UPDATED_VALOR);

        restTaxasMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTaxas.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTaxas))
            )
            .andExpect(status().isOk());

        // Validate the Taxas in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTaxasUpdatableFieldsEquals(partialUpdatedTaxas, getPersistedTaxas(partialUpdatedTaxas));
    }

    @Test
    void patchNonExistingTaxas() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        taxas.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTaxasMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, taxas.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(taxas))
            )
            .andExpect(status().isBadRequest());

        // Validate the Taxas in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchTaxas() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        taxas.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaxasMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(taxas))
            )
            .andExpect(status().isBadRequest());

        // Validate the Taxas in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamTaxas() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        taxas.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaxasMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(taxas)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Taxas in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteTaxas() throws Exception {
        // Initialize the database
        insertedTaxas = taxasRepository.save(taxas);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the taxas
        restTaxasMockMvc
            .perform(delete(ENTITY_API_URL_ID, taxas.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return taxasRepository.count();
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

    protected Taxas getPersistedTaxas(Taxas taxas) {
        return taxasRepository.findById(taxas.getId()).orElseThrow();
    }

    protected void assertPersistedTaxasToMatchAllProperties(Taxas expectedTaxas) {
        assertTaxasAllPropertiesEquals(expectedTaxas, getPersistedTaxas(expectedTaxas));
    }

    protected void assertPersistedTaxasToMatchUpdatableProperties(Taxas expectedTaxas) {
        assertTaxasAllUpdatablePropertiesEquals(expectedTaxas, getPersistedTaxas(expectedTaxas));
    }
}
