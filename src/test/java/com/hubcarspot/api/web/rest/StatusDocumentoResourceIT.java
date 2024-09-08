package com.hubcarspot.api.web.rest;

import static com.hubcarspot.api.domain.StatusDocumentoAsserts.*;
import static com.hubcarspot.api.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hubcarspot.api.IntegrationTest;
import com.hubcarspot.api.domain.StatusDocumento;
import com.hubcarspot.api.repository.StatusDocumentoRepository;
import com.hubcarspot.api.service.StatusDocumentoService;
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
 * Integration tests for the {@link StatusDocumentoResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class StatusDocumentoResourceIT {

    private static final String DEFAULT_INSTITUICAO_ID = "AAAAAAAAAA";
    private static final String UPDATED_INSTITUICAO_ID = "BBBBBBBBBB";

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/status-documentos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private StatusDocumentoRepository statusDocumentoRepository;

    @Mock
    private StatusDocumentoRepository statusDocumentoRepositoryMock;

    @Mock
    private StatusDocumentoService statusDocumentoServiceMock;

    @Autowired
    private MockMvc restStatusDocumentoMockMvc;

    private StatusDocumento statusDocumento;

    private StatusDocumento insertedStatusDocumento;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StatusDocumento createEntity() {
        return new StatusDocumento().instituicaoId(DEFAULT_INSTITUICAO_ID).nome(DEFAULT_NOME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StatusDocumento createUpdatedEntity() {
        return new StatusDocumento().instituicaoId(UPDATED_INSTITUICAO_ID).nome(UPDATED_NOME);
    }

    @BeforeEach
    public void initTest() {
        statusDocumento = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedStatusDocumento != null) {
            statusDocumentoRepository.delete(insertedStatusDocumento);
            insertedStatusDocumento = null;
        }
    }

    @Test
    void createStatusDocumento() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the StatusDocumento
        var returnedStatusDocumento = om.readValue(
            restStatusDocumentoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(statusDocumento)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            StatusDocumento.class
        );

        // Validate the StatusDocumento in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertStatusDocumentoUpdatableFieldsEquals(returnedStatusDocumento, getPersistedStatusDocumento(returnedStatusDocumento));

        insertedStatusDocumento = returnedStatusDocumento;
    }

    @Test
    void createStatusDocumentoWithExistingId() throws Exception {
        // Create the StatusDocumento with an existing ID
        statusDocumento.setId("existing_id");

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStatusDocumentoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(statusDocumento)))
            .andExpect(status().isBadRequest());

        // Validate the StatusDocumento in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkInstituicaoIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        statusDocumento.setInstituicaoId(null);

        // Create the StatusDocumento, which fails.

        restStatusDocumentoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(statusDocumento)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllStatusDocumentos() throws Exception {
        // Initialize the database
        insertedStatusDocumento = statusDocumentoRepository.save(statusDocumento);

        // Get all the statusDocumentoList
        restStatusDocumentoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(statusDocumento.getId())))
            .andExpect(jsonPath("$.[*].instituicaoId").value(hasItem(DEFAULT_INSTITUICAO_ID)))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllStatusDocumentosWithEagerRelationshipsIsEnabled() throws Exception {
        when(statusDocumentoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restStatusDocumentoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(statusDocumentoServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllStatusDocumentosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(statusDocumentoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restStatusDocumentoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(statusDocumentoRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void getStatusDocumento() throws Exception {
        // Initialize the database
        insertedStatusDocumento = statusDocumentoRepository.save(statusDocumento);

        // Get the statusDocumento
        restStatusDocumentoMockMvc
            .perform(get(ENTITY_API_URL_ID, statusDocumento.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(statusDocumento.getId()))
            .andExpect(jsonPath("$.instituicaoId").value(DEFAULT_INSTITUICAO_ID))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME));
    }

    @Test
    void getNonExistingStatusDocumento() throws Exception {
        // Get the statusDocumento
        restStatusDocumentoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingStatusDocumento() throws Exception {
        // Initialize the database
        insertedStatusDocumento = statusDocumentoRepository.save(statusDocumento);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the statusDocumento
        StatusDocumento updatedStatusDocumento = statusDocumentoRepository.findById(statusDocumento.getId()).orElseThrow();
        updatedStatusDocumento.instituicaoId(UPDATED_INSTITUICAO_ID).nome(UPDATED_NOME);

        restStatusDocumentoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedStatusDocumento.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedStatusDocumento))
            )
            .andExpect(status().isOk());

        // Validate the StatusDocumento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedStatusDocumentoToMatchAllProperties(updatedStatusDocumento);
    }

    @Test
    void putNonExistingStatusDocumento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        statusDocumento.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStatusDocumentoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, statusDocumento.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(statusDocumento))
            )
            .andExpect(status().isBadRequest());

        // Validate the StatusDocumento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchStatusDocumento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        statusDocumento.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStatusDocumentoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(statusDocumento))
            )
            .andExpect(status().isBadRequest());

        // Validate the StatusDocumento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamStatusDocumento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        statusDocumento.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStatusDocumentoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(statusDocumento)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the StatusDocumento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateStatusDocumentoWithPatch() throws Exception {
        // Initialize the database
        insertedStatusDocumento = statusDocumentoRepository.save(statusDocumento);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the statusDocumento using partial update
        StatusDocumento partialUpdatedStatusDocumento = new StatusDocumento();
        partialUpdatedStatusDocumento.setId(statusDocumento.getId());

        partialUpdatedStatusDocumento.instituicaoId(UPDATED_INSTITUICAO_ID).nome(UPDATED_NOME);

        restStatusDocumentoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStatusDocumento.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedStatusDocumento))
            )
            .andExpect(status().isOk());

        // Validate the StatusDocumento in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertStatusDocumentoUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedStatusDocumento, statusDocumento),
            getPersistedStatusDocumento(statusDocumento)
        );
    }

    @Test
    void fullUpdateStatusDocumentoWithPatch() throws Exception {
        // Initialize the database
        insertedStatusDocumento = statusDocumentoRepository.save(statusDocumento);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the statusDocumento using partial update
        StatusDocumento partialUpdatedStatusDocumento = new StatusDocumento();
        partialUpdatedStatusDocumento.setId(statusDocumento.getId());

        partialUpdatedStatusDocumento.instituicaoId(UPDATED_INSTITUICAO_ID).nome(UPDATED_NOME);

        restStatusDocumentoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStatusDocumento.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedStatusDocumento))
            )
            .andExpect(status().isOk());

        // Validate the StatusDocumento in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertStatusDocumentoUpdatableFieldsEquals(
            partialUpdatedStatusDocumento,
            getPersistedStatusDocumento(partialUpdatedStatusDocumento)
        );
    }

    @Test
    void patchNonExistingStatusDocumento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        statusDocumento.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStatusDocumentoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, statusDocumento.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(statusDocumento))
            )
            .andExpect(status().isBadRequest());

        // Validate the StatusDocumento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchStatusDocumento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        statusDocumento.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStatusDocumentoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(statusDocumento))
            )
            .andExpect(status().isBadRequest());

        // Validate the StatusDocumento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamStatusDocumento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        statusDocumento.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStatusDocumentoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(statusDocumento)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the StatusDocumento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteStatusDocumento() throws Exception {
        // Initialize the database
        insertedStatusDocumento = statusDocumentoRepository.save(statusDocumento);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the statusDocumento
        restStatusDocumentoMockMvc
            .perform(delete(ENTITY_API_URL_ID, statusDocumento.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return statusDocumentoRepository.count();
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

    protected StatusDocumento getPersistedStatusDocumento(StatusDocumento statusDocumento) {
        return statusDocumentoRepository.findById(statusDocumento.getId()).orElseThrow();
    }

    protected void assertPersistedStatusDocumentoToMatchAllProperties(StatusDocumento expectedStatusDocumento) {
        assertStatusDocumentoAllPropertiesEquals(expectedStatusDocumento, getPersistedStatusDocumento(expectedStatusDocumento));
    }

    protected void assertPersistedStatusDocumentoToMatchUpdatableProperties(StatusDocumento expectedStatusDocumento) {
        assertStatusDocumentoAllUpdatablePropertiesEquals(expectedStatusDocumento, getPersistedStatusDocumento(expectedStatusDocumento));
    }
}
