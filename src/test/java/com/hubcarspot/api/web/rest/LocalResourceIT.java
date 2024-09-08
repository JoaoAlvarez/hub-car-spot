package com.hubcarspot.api.web.rest;

import static com.hubcarspot.api.domain.LocalAsserts.*;
import static com.hubcarspot.api.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hubcarspot.api.IntegrationTest;
import com.hubcarspot.api.domain.Local;
import com.hubcarspot.api.repository.LocalRepository;
import com.hubcarspot.api.service.LocalService;
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
 * Integration tests for the {@link LocalResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class LocalResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/locals";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private LocalRepository localRepository;

    @Mock
    private LocalRepository localRepositoryMock;

    @Mock
    private LocalService localServiceMock;

    @Autowired
    private MockMvc restLocalMockMvc;

    private Local local;

    private Local insertedLocal;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Local createEntity() {
        return new Local().nome(DEFAULT_NOME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Local createUpdatedEntity() {
        return new Local().nome(UPDATED_NOME);
    }

    @BeforeEach
    public void initTest() {
        local = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedLocal != null) {
            localRepository.delete(insertedLocal);
            insertedLocal = null;
        }
    }

    @Test
    void createLocal() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Local
        var returnedLocal = om.readValue(
            restLocalMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(local)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Local.class
        );

        // Validate the Local in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertLocalUpdatableFieldsEquals(returnedLocal, getPersistedLocal(returnedLocal));

        insertedLocal = returnedLocal;
    }

    @Test
    void createLocalWithExistingId() throws Exception {
        // Create the Local with an existing ID
        local.setId("existing_id");

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLocalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(local)))
            .andExpect(status().isBadRequest());

        // Validate the Local in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void getAllLocals() throws Exception {
        // Initialize the database
        insertedLocal = localRepository.save(local);

        // Get all the localList
        restLocalMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(local.getId())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllLocalsWithEagerRelationshipsIsEnabled() throws Exception {
        when(localServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restLocalMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(localServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllLocalsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(localServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restLocalMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(localRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void getLocal() throws Exception {
        // Initialize the database
        insertedLocal = localRepository.save(local);

        // Get the local
        restLocalMockMvc
            .perform(get(ENTITY_API_URL_ID, local.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(local.getId()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME));
    }

    @Test
    void getNonExistingLocal() throws Exception {
        // Get the local
        restLocalMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingLocal() throws Exception {
        // Initialize the database
        insertedLocal = localRepository.save(local);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the local
        Local updatedLocal = localRepository.findById(local.getId()).orElseThrow();
        updatedLocal.nome(UPDATED_NOME);

        restLocalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedLocal.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedLocal))
            )
            .andExpect(status().isOk());

        // Validate the Local in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedLocalToMatchAllProperties(updatedLocal);
    }

    @Test
    void putNonExistingLocal() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        local.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLocalMockMvc
            .perform(put(ENTITY_API_URL_ID, local.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(local)))
            .andExpect(status().isBadRequest());

        // Validate the Local in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchLocal() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        local.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(local))
            )
            .andExpect(status().isBadRequest());

        // Validate the Local in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamLocal() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        local.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocalMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(local)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Local in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateLocalWithPatch() throws Exception {
        // Initialize the database
        insertedLocal = localRepository.save(local);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the local using partial update
        Local partialUpdatedLocal = new Local();
        partialUpdatedLocal.setId(local.getId());

        partialUpdatedLocal.nome(UPDATED_NOME);

        restLocalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLocal.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLocal))
            )
            .andExpect(status().isOk());

        // Validate the Local in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLocalUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedLocal, local), getPersistedLocal(local));
    }

    @Test
    void fullUpdateLocalWithPatch() throws Exception {
        // Initialize the database
        insertedLocal = localRepository.save(local);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the local using partial update
        Local partialUpdatedLocal = new Local();
        partialUpdatedLocal.setId(local.getId());

        partialUpdatedLocal.nome(UPDATED_NOME);

        restLocalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLocal.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLocal))
            )
            .andExpect(status().isOk());

        // Validate the Local in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLocalUpdatableFieldsEquals(partialUpdatedLocal, getPersistedLocal(partialUpdatedLocal));
    }

    @Test
    void patchNonExistingLocal() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        local.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLocalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, local.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(local))
            )
            .andExpect(status().isBadRequest());

        // Validate the Local in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchLocal() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        local.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(local))
            )
            .andExpect(status().isBadRequest());

        // Validate the Local in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamLocal() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        local.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocalMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(local)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Local in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteLocal() throws Exception {
        // Initialize the database
        insertedLocal = localRepository.save(local);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the local
        restLocalMockMvc
            .perform(delete(ENTITY_API_URL_ID, local.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return localRepository.count();
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

    protected Local getPersistedLocal(Local local) {
        return localRepository.findById(local.getId()).orElseThrow();
    }

    protected void assertPersistedLocalToMatchAllProperties(Local expectedLocal) {
        assertLocalAllPropertiesEquals(expectedLocal, getPersistedLocal(expectedLocal));
    }

    protected void assertPersistedLocalToMatchUpdatableProperties(Local expectedLocal) {
        assertLocalAllUpdatablePropertiesEquals(expectedLocal, getPersistedLocal(expectedLocal));
    }
}
