package com.hubcarspot.api.web.rest;

import static com.hubcarspot.api.domain.FornecedorAsserts.*;
import static com.hubcarspot.api.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hubcarspot.api.IntegrationTest;
import com.hubcarspot.api.domain.Fornecedor;
import com.hubcarspot.api.repository.FornecedorRepository;
import com.hubcarspot.api.service.FornecedorService;
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
 * Integration tests for the {@link FornecedorResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class FornecedorResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_CNPJ = "AAAAAAAAAA";
    private static final String UPDATED_CNPJ = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFONE = "AAAAAAAAAA";
    private static final String UPDATED_TELEFONE = "BBBBBBBBBB";

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

    private static final String ENTITY_API_URL = "/api/fornecedors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FornecedorRepository fornecedorRepository;

    @Mock
    private FornecedorRepository fornecedorRepositoryMock;

    @Mock
    private FornecedorService fornecedorServiceMock;

    @Autowired
    private MockMvc restFornecedorMockMvc;

    private Fornecedor fornecedor;

    private Fornecedor insertedFornecedor;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Fornecedor createEntity() {
        return new Fornecedor()
            .nome(DEFAULT_NOME)
            .cnpj(DEFAULT_CNPJ)
            .telefone(DEFAULT_TELEFONE)
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
    public static Fornecedor createUpdatedEntity() {
        return new Fornecedor()
            .nome(UPDATED_NOME)
            .cnpj(UPDATED_CNPJ)
            .telefone(UPDATED_TELEFONE)
            .cep(UPDATED_CEP)
            .endereco(UPDATED_ENDERECO)
            .bairro(UPDATED_BAIRRO)
            .cidade(UPDATED_CIDADE)
            .numero(UPDATED_NUMERO)
            .uf(UPDATED_UF);
    }

    @BeforeEach
    public void initTest() {
        fornecedor = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedFornecedor != null) {
            fornecedorRepository.delete(insertedFornecedor);
            insertedFornecedor = null;
        }
    }

    @Test
    void createFornecedor() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Fornecedor
        var returnedFornecedor = om.readValue(
            restFornecedorMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fornecedor)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Fornecedor.class
        );

        // Validate the Fornecedor in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertFornecedorUpdatableFieldsEquals(returnedFornecedor, getPersistedFornecedor(returnedFornecedor));

        insertedFornecedor = returnedFornecedor;
    }

    @Test
    void createFornecedorWithExistingId() throws Exception {
        // Create the Fornecedor with an existing ID
        fornecedor.setId("existing_id");

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFornecedorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fornecedor)))
            .andExpect(status().isBadRequest());

        // Validate the Fornecedor in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkTelefoneIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        fornecedor.setTelefone(null);

        // Create the Fornecedor, which fails.

        restFornecedorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fornecedor)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllFornecedors() throws Exception {
        // Initialize the database
        insertedFornecedor = fornecedorRepository.save(fornecedor);

        // Get all the fornecedorList
        restFornecedorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fornecedor.getId())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].cnpj").value(hasItem(DEFAULT_CNPJ)))
            .andExpect(jsonPath("$.[*].telefone").value(hasItem(DEFAULT_TELEFONE)))
            .andExpect(jsonPath("$.[*].cep").value(hasItem(DEFAULT_CEP)))
            .andExpect(jsonPath("$.[*].endereco").value(hasItem(DEFAULT_ENDERECO)))
            .andExpect(jsonPath("$.[*].bairro").value(hasItem(DEFAULT_BAIRRO)))
            .andExpect(jsonPath("$.[*].cidade").value(hasItem(DEFAULT_CIDADE)))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)))
            .andExpect(jsonPath("$.[*].uf").value(hasItem(DEFAULT_UF)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFornecedorsWithEagerRelationshipsIsEnabled() throws Exception {
        when(fornecedorServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFornecedorMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(fornecedorServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFornecedorsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(fornecedorServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFornecedorMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(fornecedorRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void getFornecedor() throws Exception {
        // Initialize the database
        insertedFornecedor = fornecedorRepository.save(fornecedor);

        // Get the fornecedor
        restFornecedorMockMvc
            .perform(get(ENTITY_API_URL_ID, fornecedor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(fornecedor.getId()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.cnpj").value(DEFAULT_CNPJ))
            .andExpect(jsonPath("$.telefone").value(DEFAULT_TELEFONE))
            .andExpect(jsonPath("$.cep").value(DEFAULT_CEP))
            .andExpect(jsonPath("$.endereco").value(DEFAULT_ENDERECO))
            .andExpect(jsonPath("$.bairro").value(DEFAULT_BAIRRO))
            .andExpect(jsonPath("$.cidade").value(DEFAULT_CIDADE))
            .andExpect(jsonPath("$.numero").value(DEFAULT_NUMERO))
            .andExpect(jsonPath("$.uf").value(DEFAULT_UF));
    }

    @Test
    void getNonExistingFornecedor() throws Exception {
        // Get the fornecedor
        restFornecedorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingFornecedor() throws Exception {
        // Initialize the database
        insertedFornecedor = fornecedorRepository.save(fornecedor);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the fornecedor
        Fornecedor updatedFornecedor = fornecedorRepository.findById(fornecedor.getId()).orElseThrow();
        updatedFornecedor
            .nome(UPDATED_NOME)
            .cnpj(UPDATED_CNPJ)
            .telefone(UPDATED_TELEFONE)
            .cep(UPDATED_CEP)
            .endereco(UPDATED_ENDERECO)
            .bairro(UPDATED_BAIRRO)
            .cidade(UPDATED_CIDADE)
            .numero(UPDATED_NUMERO)
            .uf(UPDATED_UF);

        restFornecedorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFornecedor.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedFornecedor))
            )
            .andExpect(status().isOk());

        // Validate the Fornecedor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFornecedorToMatchAllProperties(updatedFornecedor);
    }

    @Test
    void putNonExistingFornecedor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fornecedor.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFornecedorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fornecedor.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fornecedor))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fornecedor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchFornecedor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fornecedor.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFornecedorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(fornecedor))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fornecedor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamFornecedor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fornecedor.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFornecedorMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fornecedor)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Fornecedor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateFornecedorWithPatch() throws Exception {
        // Initialize the database
        insertedFornecedor = fornecedorRepository.save(fornecedor);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the fornecedor using partial update
        Fornecedor partialUpdatedFornecedor = new Fornecedor();
        partialUpdatedFornecedor.setId(fornecedor.getId());

        partialUpdatedFornecedor.telefone(UPDATED_TELEFONE).cep(UPDATED_CEP).cidade(UPDATED_CIDADE).uf(UPDATED_UF);

        restFornecedorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFornecedor.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFornecedor))
            )
            .andExpect(status().isOk());

        // Validate the Fornecedor in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFornecedorUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedFornecedor, fornecedor),
            getPersistedFornecedor(fornecedor)
        );
    }

    @Test
    void fullUpdateFornecedorWithPatch() throws Exception {
        // Initialize the database
        insertedFornecedor = fornecedorRepository.save(fornecedor);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the fornecedor using partial update
        Fornecedor partialUpdatedFornecedor = new Fornecedor();
        partialUpdatedFornecedor.setId(fornecedor.getId());

        partialUpdatedFornecedor
            .nome(UPDATED_NOME)
            .cnpj(UPDATED_CNPJ)
            .telefone(UPDATED_TELEFONE)
            .cep(UPDATED_CEP)
            .endereco(UPDATED_ENDERECO)
            .bairro(UPDATED_BAIRRO)
            .cidade(UPDATED_CIDADE)
            .numero(UPDATED_NUMERO)
            .uf(UPDATED_UF);

        restFornecedorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFornecedor.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFornecedor))
            )
            .andExpect(status().isOk());

        // Validate the Fornecedor in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFornecedorUpdatableFieldsEquals(partialUpdatedFornecedor, getPersistedFornecedor(partialUpdatedFornecedor));
    }

    @Test
    void patchNonExistingFornecedor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fornecedor.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFornecedorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, fornecedor.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(fornecedor))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fornecedor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchFornecedor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fornecedor.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFornecedorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(fornecedor))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fornecedor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamFornecedor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fornecedor.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFornecedorMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(fornecedor)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Fornecedor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteFornecedor() throws Exception {
        // Initialize the database
        insertedFornecedor = fornecedorRepository.save(fornecedor);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the fornecedor
        restFornecedorMockMvc
            .perform(delete(ENTITY_API_URL_ID, fornecedor.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return fornecedorRepository.count();
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

    protected Fornecedor getPersistedFornecedor(Fornecedor fornecedor) {
        return fornecedorRepository.findById(fornecedor.getId()).orElseThrow();
    }

    protected void assertPersistedFornecedorToMatchAllProperties(Fornecedor expectedFornecedor) {
        assertFornecedorAllPropertiesEquals(expectedFornecedor, getPersistedFornecedor(expectedFornecedor));
    }

    protected void assertPersistedFornecedorToMatchUpdatableProperties(Fornecedor expectedFornecedor) {
        assertFornecedorAllUpdatablePropertiesEquals(expectedFornecedor, getPersistedFornecedor(expectedFornecedor));
    }
}
