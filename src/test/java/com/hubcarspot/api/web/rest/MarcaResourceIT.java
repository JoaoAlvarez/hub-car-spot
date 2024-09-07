package com.hubcarspot.api.web.rest;

import static com.hubcarspot.api.domain.MarcaAsserts.*;
import static com.hubcarspot.api.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hubcarspot.api.IntegrationTest;
import com.hubcarspot.api.domain.Marca;
import com.hubcarspot.api.repository.MarcaRepository;
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
 * Integration tests for the {@link MarcaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MarcaResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/marcas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MarcaRepository marcaRepository;

    @Autowired
    private MockMvc restMarcaMockMvc;

    private Marca marca;

    private Marca insertedMarca;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Marca createEntity() {
        return new Marca().nome(DEFAULT_NOME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Marca createUpdatedEntity() {
        return new Marca().nome(UPDATED_NOME);
    }

    @BeforeEach
    public void initTest() {
        marca = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedMarca != null) {
            marcaRepository.delete(insertedMarca);
            insertedMarca = null;
        }
    }

    @Test
    void createMarca() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Marca
        var returnedMarca = om.readValue(
            restMarcaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(marca)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Marca.class
        );

        // Validate the Marca in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertMarcaUpdatableFieldsEquals(returnedMarca, getPersistedMarca(returnedMarca));

        insertedMarca = returnedMarca;
    }

    @Test
    void createMarcaWithExistingId() throws Exception {
        // Create the Marca with an existing ID
        marca.setId("existing_id");

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMarcaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(marca)))
            .andExpect(status().isBadRequest());

        // Validate the Marca in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void getAllMarcas() throws Exception {
        // Initialize the database
        insertedMarca = marcaRepository.save(marca);

        // Get all the marcaList
        restMarcaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(marca.getId())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)));
    }

    @Test
    void getMarca() throws Exception {
        // Initialize the database
        insertedMarca = marcaRepository.save(marca);

        // Get the marca
        restMarcaMockMvc
            .perform(get(ENTITY_API_URL_ID, marca.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(marca.getId()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME));
    }

    @Test
    void getNonExistingMarca() throws Exception {
        // Get the marca
        restMarcaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingMarca() throws Exception {
        // Initialize the database
        insertedMarca = marcaRepository.save(marca);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the marca
        Marca updatedMarca = marcaRepository.findById(marca.getId()).orElseThrow();
        updatedMarca.nome(UPDATED_NOME);

        restMarcaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMarca.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedMarca))
            )
            .andExpect(status().isOk());

        // Validate the Marca in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMarcaToMatchAllProperties(updatedMarca);
    }

    @Test
    void putNonExistingMarca() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        marca.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMarcaMockMvc
            .perform(put(ENTITY_API_URL_ID, marca.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(marca)))
            .andExpect(status().isBadRequest());

        // Validate the Marca in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchMarca() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        marca.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMarcaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(marca))
            )
            .andExpect(status().isBadRequest());

        // Validate the Marca in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamMarca() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        marca.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMarcaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(marca)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Marca in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateMarcaWithPatch() throws Exception {
        // Initialize the database
        insertedMarca = marcaRepository.save(marca);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the marca using partial update
        Marca partialUpdatedMarca = new Marca();
        partialUpdatedMarca.setId(marca.getId());

        restMarcaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMarca.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMarca))
            )
            .andExpect(status().isOk());

        // Validate the Marca in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMarcaUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedMarca, marca), getPersistedMarca(marca));
    }

    @Test
    void fullUpdateMarcaWithPatch() throws Exception {
        // Initialize the database
        insertedMarca = marcaRepository.save(marca);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the marca using partial update
        Marca partialUpdatedMarca = new Marca();
        partialUpdatedMarca.setId(marca.getId());

        partialUpdatedMarca.nome(UPDATED_NOME);

        restMarcaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMarca.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMarca))
            )
            .andExpect(status().isOk());

        // Validate the Marca in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMarcaUpdatableFieldsEquals(partialUpdatedMarca, getPersistedMarca(partialUpdatedMarca));
    }

    @Test
    void patchNonExistingMarca() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        marca.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMarcaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, marca.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(marca))
            )
            .andExpect(status().isBadRequest());

        // Validate the Marca in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchMarca() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        marca.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMarcaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(marca))
            )
            .andExpect(status().isBadRequest());

        // Validate the Marca in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamMarca() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        marca.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMarcaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(marca)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Marca in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteMarca() throws Exception {
        // Initialize the database
        insertedMarca = marcaRepository.save(marca);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the marca
        restMarcaMockMvc
            .perform(delete(ENTITY_API_URL_ID, marca.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return marcaRepository.count();
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

    protected Marca getPersistedMarca(Marca marca) {
        return marcaRepository.findById(marca.getId()).orElseThrow();
    }

    protected void assertPersistedMarcaToMatchAllProperties(Marca expectedMarca) {
        assertMarcaAllPropertiesEquals(expectedMarca, getPersistedMarca(expectedMarca));
    }

    protected void assertPersistedMarcaToMatchUpdatableProperties(Marca expectedMarca) {
        assertMarcaAllUpdatablePropertiesEquals(expectedMarca, getPersistedMarca(expectedMarca));
    }
}
