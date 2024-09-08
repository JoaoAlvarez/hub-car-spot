package com.hubcarspot.api.web.rest;

import static com.hubcarspot.api.domain.FilialAsserts.*;
import static com.hubcarspot.api.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hubcarspot.api.IntegrationTest;
import com.hubcarspot.api.domain.Filial;
import com.hubcarspot.api.repository.FilialRepository;
import com.hubcarspot.api.service.FilialService;
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
 * Integration tests for the {@link FilialResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class FilialResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFONE = "AAAAAAAAAA";
    private static final String UPDATED_TELEFONE = "BBBBBBBBBB";

    private static final String DEFAULT_CNPJ = "AAAAAAAAAA";
    private static final String UPDATED_CNPJ = "BBBBBBBBBB";

    private static final String DEFAULT_CEP = "AAAAAAAAAA";
    private static final String UPDATED_CEP = "BBBBBBBBBB";

    private static final String DEFAULT_ENDERECO = "AAAAAAAAAA";
    private static final String UPDATED_ENDERECO = "BBBBBBBBBB";

    private static final String DEFAULT_BAIRRO = "AAAAAAAAAA";
    private static final String UPDATED_BAIRRO = "BBBBBBBBBB";

    private static final String DEFAULT_CIDADE = "AAAAAAAAAA";
    private static final String UPDATED_CIDADE = "BBBBBBBBBB";

    private static final String DEFAULT_NUMERO = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO = "BBBBBBBBBB";

    private static final String DEFAULT_UF = "AAAAAAAAAA";
    private static final String UPDATED_UF = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/filials";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FilialRepository filialRepository;

    @Mock
    private FilialRepository filialRepositoryMock;

    @Mock
    private FilialService filialServiceMock;

    @Autowired
    private MockMvc restFilialMockMvc;

    private Filial filial;

    private Filial insertedFilial;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Filial createEntity() {
        return new Filial()
            .nome(DEFAULT_NOME)
            .telefone(DEFAULT_TELEFONE)
            .cnpj(DEFAULT_CNPJ)
            .cep(DEFAULT_CEP)
            .endereco(DEFAULT_ENDERECO)
            .bairro(DEFAULT_BAIRRO)
            .cidade(DEFAULT_CIDADE)
            .numero(DEFAULT_NUMERO)
            .uf(DEFAULT_UF);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Filial createUpdatedEntity() {
        return new Filial()
            .nome(UPDATED_NOME)
            .telefone(UPDATED_TELEFONE)
            .cnpj(UPDATED_CNPJ)
            .cep(UPDATED_CEP)
            .endereco(UPDATED_ENDERECO)
            .bairro(UPDATED_BAIRRO)
            .cidade(UPDATED_CIDADE)
            .numero(UPDATED_NUMERO)
            .uf(UPDATED_UF);
    }

    @BeforeEach
    public void initTest() {
        filial = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedFilial != null) {
            filialRepository.delete(insertedFilial);
            insertedFilial = null;
        }
    }

    @Test
    void createFilial() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Filial
        var returnedFilial = om.readValue(
            restFilialMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(filial)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Filial.class
        );

        // Validate the Filial in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertFilialUpdatableFieldsEquals(returnedFilial, getPersistedFilial(returnedFilial));

        insertedFilial = returnedFilial;
    }

    @Test
    void createFilialWithExistingId() throws Exception {
        // Create the Filial with an existing ID
        filial.setId("existing_id");

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFilialMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(filial)))
            .andExpect(status().isBadRequest());

        // Validate the Filial in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkTelefoneIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        filial.setTelefone(null);

        // Create the Filial, which fails.

        restFilialMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(filial)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllFilials() throws Exception {
        // Initialize the database
        insertedFilial = filialRepository.save(filial);

        // Get all the filialList
        restFilialMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(filial.getId())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].telefone").value(hasItem(DEFAULT_TELEFONE)))
            .andExpect(jsonPath("$.[*].cnpj").value(hasItem(DEFAULT_CNPJ)))
            .andExpect(jsonPath("$.[*].cep").value(hasItem(DEFAULT_CEP)))
            .andExpect(jsonPath("$.[*].endereco").value(hasItem(DEFAULT_ENDERECO)))
            .andExpect(jsonPath("$.[*].bairro").value(hasItem(DEFAULT_BAIRRO)))
            .andExpect(jsonPath("$.[*].cidade").value(hasItem(DEFAULT_CIDADE)))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)))
            .andExpect(jsonPath("$.[*].uf").value(hasItem(DEFAULT_UF)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFilialsWithEagerRelationshipsIsEnabled() throws Exception {
        when(filialServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFilialMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(filialServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFilialsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(filialServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFilialMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(filialRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void getFilial() throws Exception {
        // Initialize the database
        insertedFilial = filialRepository.save(filial);

        // Get the filial
        restFilialMockMvc
            .perform(get(ENTITY_API_URL_ID, filial.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(filial.getId()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.telefone").value(DEFAULT_TELEFONE))
            .andExpect(jsonPath("$.cnpj").value(DEFAULT_CNPJ))
            .andExpect(jsonPath("$.cep").value(DEFAULT_CEP))
            .andExpect(jsonPath("$.endereco").value(DEFAULT_ENDERECO))
            .andExpect(jsonPath("$.bairro").value(DEFAULT_BAIRRO))
            .andExpect(jsonPath("$.cidade").value(DEFAULT_CIDADE))
            .andExpect(jsonPath("$.numero").value(DEFAULT_NUMERO))
            .andExpect(jsonPath("$.uf").value(DEFAULT_UF));
    }

    @Test
    void getNonExistingFilial() throws Exception {
        // Get the filial
        restFilialMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingFilial() throws Exception {
        // Initialize the database
        insertedFilial = filialRepository.save(filial);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the filial
        Filial updatedFilial = filialRepository.findById(filial.getId()).orElseThrow();
        updatedFilial
            .nome(UPDATED_NOME)
            .telefone(UPDATED_TELEFONE)
            .cnpj(UPDATED_CNPJ)
            .cep(UPDATED_CEP)
            .endereco(UPDATED_ENDERECO)
            .bairro(UPDATED_BAIRRO)
            .cidade(UPDATED_CIDADE)
            .numero(UPDATED_NUMERO)
            .uf(UPDATED_UF);

        restFilialMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFilial.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedFilial))
            )
            .andExpect(status().isOk());

        // Validate the Filial in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFilialToMatchAllProperties(updatedFilial);
    }

    @Test
    void putNonExistingFilial() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        filial.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFilialMockMvc
            .perform(put(ENTITY_API_URL_ID, filial.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(filial)))
            .andExpect(status().isBadRequest());

        // Validate the Filial in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchFilial() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        filial.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFilialMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(filial))
            )
            .andExpect(status().isBadRequest());

        // Validate the Filial in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamFilial() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        filial.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFilialMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(filial)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Filial in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateFilialWithPatch() throws Exception {
        // Initialize the database
        insertedFilial = filialRepository.save(filial);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the filial using partial update
        Filial partialUpdatedFilial = new Filial();
        partialUpdatedFilial.setId(filial.getId());

        partialUpdatedFilial
            .nome(UPDATED_NOME)
            .telefone(UPDATED_TELEFONE)
            .cnpj(UPDATED_CNPJ)
            .cep(UPDATED_CEP)
            .endereco(UPDATED_ENDERECO)
            .numero(UPDATED_NUMERO)
            .uf(UPDATED_UF);

        restFilialMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFilial.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFilial))
            )
            .andExpect(status().isOk());

        // Validate the Filial in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFilialUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedFilial, filial), getPersistedFilial(filial));
    }

    @Test
    void fullUpdateFilialWithPatch() throws Exception {
        // Initialize the database
        insertedFilial = filialRepository.save(filial);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the filial using partial update
        Filial partialUpdatedFilial = new Filial();
        partialUpdatedFilial.setId(filial.getId());

        partialUpdatedFilial
            .nome(UPDATED_NOME)
            .telefone(UPDATED_TELEFONE)
            .cnpj(UPDATED_CNPJ)
            .cep(UPDATED_CEP)
            .endereco(UPDATED_ENDERECO)
            .bairro(UPDATED_BAIRRO)
            .cidade(UPDATED_CIDADE)
            .numero(UPDATED_NUMERO)
            .uf(UPDATED_UF);

        restFilialMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFilial.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFilial))
            )
            .andExpect(status().isOk());

        // Validate the Filial in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFilialUpdatableFieldsEquals(partialUpdatedFilial, getPersistedFilial(partialUpdatedFilial));
    }

    @Test
    void patchNonExistingFilial() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        filial.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFilialMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, filial.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(filial))
            )
            .andExpect(status().isBadRequest());

        // Validate the Filial in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchFilial() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        filial.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFilialMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(filial))
            )
            .andExpect(status().isBadRequest());

        // Validate the Filial in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamFilial() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        filial.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFilialMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(filial)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Filial in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteFilial() throws Exception {
        // Initialize the database
        insertedFilial = filialRepository.save(filial);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the filial
        restFilialMockMvc
            .perform(delete(ENTITY_API_URL_ID, filial.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return filialRepository.count();
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

    protected Filial getPersistedFilial(Filial filial) {
        return filialRepository.findById(filial.getId()).orElseThrow();
    }

    protected void assertPersistedFilialToMatchAllProperties(Filial expectedFilial) {
        assertFilialAllPropertiesEquals(expectedFilial, getPersistedFilial(expectedFilial));
    }

    protected void assertPersistedFilialToMatchUpdatableProperties(Filial expectedFilial) {
        assertFilialAllUpdatablePropertiesEquals(expectedFilial, getPersistedFilial(expectedFilial));
    }
}
