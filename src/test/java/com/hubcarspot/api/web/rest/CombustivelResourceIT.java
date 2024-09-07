package com.hubcarspot.api.web.rest;

import static com.hubcarspot.api.domain.CombustivelAsserts.*;
import static com.hubcarspot.api.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hubcarspot.api.IntegrationTest;
import com.hubcarspot.api.domain.Combustivel;
import com.hubcarspot.api.repository.CombustivelRepository;
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
 * Integration tests for the {@link CombustivelResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CombustivelResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/combustivels";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CombustivelRepository combustivelRepository;

    @Autowired
    private MockMvc restCombustivelMockMvc;

    private Combustivel combustivel;

    private Combustivel insertedCombustivel;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Combustivel createEntity() {
        return new Combustivel().nome(DEFAULT_NOME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Combustivel createUpdatedEntity() {
        return new Combustivel().nome(UPDATED_NOME);
    }

    @BeforeEach
    public void initTest() {
        combustivel = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedCombustivel != null) {
            combustivelRepository.delete(insertedCombustivel);
            insertedCombustivel = null;
        }
    }

    @Test
    void createCombustivel() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Combustivel
        var returnedCombustivel = om.readValue(
            restCombustivelMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(combustivel)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Combustivel.class
        );

        // Validate the Combustivel in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCombustivelUpdatableFieldsEquals(returnedCombustivel, getPersistedCombustivel(returnedCombustivel));

        insertedCombustivel = returnedCombustivel;
    }

    @Test
    void createCombustivelWithExistingId() throws Exception {
        // Create the Combustivel with an existing ID
        combustivel.setId("existing_id");

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCombustivelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(combustivel)))
            .andExpect(status().isBadRequest());

        // Validate the Combustivel in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void getAllCombustivels() throws Exception {
        // Initialize the database
        insertedCombustivel = combustivelRepository.save(combustivel);

        // Get all the combustivelList
        restCombustivelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(combustivel.getId())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)));
    }

    @Test
    void getCombustivel() throws Exception {
        // Initialize the database
        insertedCombustivel = combustivelRepository.save(combustivel);

        // Get the combustivel
        restCombustivelMockMvc
            .perform(get(ENTITY_API_URL_ID, combustivel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(combustivel.getId()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME));
    }

    @Test
    void getNonExistingCombustivel() throws Exception {
        // Get the combustivel
        restCombustivelMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingCombustivel() throws Exception {
        // Initialize the database
        insertedCombustivel = combustivelRepository.save(combustivel);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the combustivel
        Combustivel updatedCombustivel = combustivelRepository.findById(combustivel.getId()).orElseThrow();
        updatedCombustivel.nome(UPDATED_NOME);

        restCombustivelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCombustivel.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCombustivel))
            )
            .andExpect(status().isOk());

        // Validate the Combustivel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCombustivelToMatchAllProperties(updatedCombustivel);
    }

    @Test
    void putNonExistingCombustivel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        combustivel.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCombustivelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, combustivel.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(combustivel))
            )
            .andExpect(status().isBadRequest());

        // Validate the Combustivel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchCombustivel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        combustivel.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCombustivelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(combustivel))
            )
            .andExpect(status().isBadRequest());

        // Validate the Combustivel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamCombustivel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        combustivel.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCombustivelMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(combustivel)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Combustivel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateCombustivelWithPatch() throws Exception {
        // Initialize the database
        insertedCombustivel = combustivelRepository.save(combustivel);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the combustivel using partial update
        Combustivel partialUpdatedCombustivel = new Combustivel();
        partialUpdatedCombustivel.setId(combustivel.getId());

        partialUpdatedCombustivel.nome(UPDATED_NOME);

        restCombustivelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCombustivel.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCombustivel))
            )
            .andExpect(status().isOk());

        // Validate the Combustivel in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCombustivelUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCombustivel, combustivel),
            getPersistedCombustivel(combustivel)
        );
    }

    @Test
    void fullUpdateCombustivelWithPatch() throws Exception {
        // Initialize the database
        insertedCombustivel = combustivelRepository.save(combustivel);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the combustivel using partial update
        Combustivel partialUpdatedCombustivel = new Combustivel();
        partialUpdatedCombustivel.setId(combustivel.getId());

        partialUpdatedCombustivel.nome(UPDATED_NOME);

        restCombustivelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCombustivel.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCombustivel))
            )
            .andExpect(status().isOk());

        // Validate the Combustivel in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCombustivelUpdatableFieldsEquals(partialUpdatedCombustivel, getPersistedCombustivel(partialUpdatedCombustivel));
    }

    @Test
    void patchNonExistingCombustivel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        combustivel.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCombustivelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, combustivel.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(combustivel))
            )
            .andExpect(status().isBadRequest());

        // Validate the Combustivel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchCombustivel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        combustivel.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCombustivelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(combustivel))
            )
            .andExpect(status().isBadRequest());

        // Validate the Combustivel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamCombustivel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        combustivel.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCombustivelMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(combustivel)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Combustivel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteCombustivel() throws Exception {
        // Initialize the database
        insertedCombustivel = combustivelRepository.save(combustivel);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the combustivel
        restCombustivelMockMvc
            .perform(delete(ENTITY_API_URL_ID, combustivel.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return combustivelRepository.count();
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

    protected Combustivel getPersistedCombustivel(Combustivel combustivel) {
        return combustivelRepository.findById(combustivel.getId()).orElseThrow();
    }

    protected void assertPersistedCombustivelToMatchAllProperties(Combustivel expectedCombustivel) {
        assertCombustivelAllPropertiesEquals(expectedCombustivel, getPersistedCombustivel(expectedCombustivel));
    }

    protected void assertPersistedCombustivelToMatchUpdatableProperties(Combustivel expectedCombustivel) {
        assertCombustivelAllUpdatablePropertiesEquals(expectedCombustivel, getPersistedCombustivel(expectedCombustivel));
    }
}
