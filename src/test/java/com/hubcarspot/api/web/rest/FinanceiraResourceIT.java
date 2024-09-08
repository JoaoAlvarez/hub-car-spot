package com.hubcarspot.api.web.rest;

import static com.hubcarspot.api.domain.FinanceiraAsserts.*;
import static com.hubcarspot.api.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hubcarspot.api.IntegrationTest;
import com.hubcarspot.api.domain.Financeira;
import com.hubcarspot.api.repository.FinanceiraRepository;
import com.hubcarspot.api.service.FinanceiraService;
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
 * Integration tests for the {@link FinanceiraResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class FinanceiraResourceIT {

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

    private static final String ENTITY_API_URL = "/api/financeiras";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FinanceiraRepository financeiraRepository;

    @Mock
    private FinanceiraRepository financeiraRepositoryMock;

    @Mock
    private FinanceiraService financeiraServiceMock;

    @Autowired
    private MockMvc restFinanceiraMockMvc;

    private Financeira financeira;

    private Financeira insertedFinanceira;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Financeira createEntity() {
        return new Financeira()
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
    public static Financeira createUpdatedEntity() {
        return new Financeira()
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
        financeira = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedFinanceira != null) {
            financeiraRepository.delete(insertedFinanceira);
            insertedFinanceira = null;
        }
    }

    @Test
    void createFinanceira() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Financeira
        var returnedFinanceira = om.readValue(
            restFinanceiraMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(financeira)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Financeira.class
        );

        // Validate the Financeira in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertFinanceiraUpdatableFieldsEquals(returnedFinanceira, getPersistedFinanceira(returnedFinanceira));

        insertedFinanceira = returnedFinanceira;
    }

    @Test
    void createFinanceiraWithExistingId() throws Exception {
        // Create the Financeira with an existing ID
        financeira.setId("existing_id");

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFinanceiraMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(financeira)))
            .andExpect(status().isBadRequest());

        // Validate the Financeira in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkTelefoneIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        financeira.setTelefone(null);

        // Create the Financeira, which fails.

        restFinanceiraMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(financeira)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllFinanceiras() throws Exception {
        // Initialize the database
        insertedFinanceira = financeiraRepository.save(financeira);

        // Get all the financeiraList
        restFinanceiraMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(financeira.getId())))
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
    void getAllFinanceirasWithEagerRelationshipsIsEnabled() throws Exception {
        when(financeiraServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFinanceiraMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(financeiraServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFinanceirasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(financeiraServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFinanceiraMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(financeiraRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void getFinanceira() throws Exception {
        // Initialize the database
        insertedFinanceira = financeiraRepository.save(financeira);

        // Get the financeira
        restFinanceiraMockMvc
            .perform(get(ENTITY_API_URL_ID, financeira.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(financeira.getId()))
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
    void getNonExistingFinanceira() throws Exception {
        // Get the financeira
        restFinanceiraMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingFinanceira() throws Exception {
        // Initialize the database
        insertedFinanceira = financeiraRepository.save(financeira);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the financeira
        Financeira updatedFinanceira = financeiraRepository.findById(financeira.getId()).orElseThrow();
        updatedFinanceira
            .nome(UPDATED_NOME)
            .telefone(UPDATED_TELEFONE)
            .cnpj(UPDATED_CNPJ)
            .cep(UPDATED_CEP)
            .endereco(UPDATED_ENDERECO)
            .bairro(UPDATED_BAIRRO)
            .cidade(UPDATED_CIDADE)
            .numero(UPDATED_NUMERO)
            .uf(UPDATED_UF);

        restFinanceiraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFinanceira.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedFinanceira))
            )
            .andExpect(status().isOk());

        // Validate the Financeira in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFinanceiraToMatchAllProperties(updatedFinanceira);
    }

    @Test
    void putNonExistingFinanceira() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        financeira.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFinanceiraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, financeira.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(financeira))
            )
            .andExpect(status().isBadRequest());

        // Validate the Financeira in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchFinanceira() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        financeira.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFinanceiraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(financeira))
            )
            .andExpect(status().isBadRequest());

        // Validate the Financeira in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamFinanceira() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        financeira.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFinanceiraMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(financeira)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Financeira in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateFinanceiraWithPatch() throws Exception {
        // Initialize the database
        insertedFinanceira = financeiraRepository.save(financeira);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the financeira using partial update
        Financeira partialUpdatedFinanceira = new Financeira();
        partialUpdatedFinanceira.setId(financeira.getId());

        partialUpdatedFinanceira.nome(UPDATED_NOME).bairro(UPDATED_BAIRRO).numero(UPDATED_NUMERO);

        restFinanceiraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFinanceira.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFinanceira))
            )
            .andExpect(status().isOk());

        // Validate the Financeira in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFinanceiraUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedFinanceira, financeira),
            getPersistedFinanceira(financeira)
        );
    }

    @Test
    void fullUpdateFinanceiraWithPatch() throws Exception {
        // Initialize the database
        insertedFinanceira = financeiraRepository.save(financeira);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the financeira using partial update
        Financeira partialUpdatedFinanceira = new Financeira();
        partialUpdatedFinanceira.setId(financeira.getId());

        partialUpdatedFinanceira
            .nome(UPDATED_NOME)
            .telefone(UPDATED_TELEFONE)
            .cnpj(UPDATED_CNPJ)
            .cep(UPDATED_CEP)
            .endereco(UPDATED_ENDERECO)
            .bairro(UPDATED_BAIRRO)
            .cidade(UPDATED_CIDADE)
            .numero(UPDATED_NUMERO)
            .uf(UPDATED_UF);

        restFinanceiraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFinanceira.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFinanceira))
            )
            .andExpect(status().isOk());

        // Validate the Financeira in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFinanceiraUpdatableFieldsEquals(partialUpdatedFinanceira, getPersistedFinanceira(partialUpdatedFinanceira));
    }

    @Test
    void patchNonExistingFinanceira() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        financeira.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFinanceiraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, financeira.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(financeira))
            )
            .andExpect(status().isBadRequest());

        // Validate the Financeira in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchFinanceira() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        financeira.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFinanceiraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(financeira))
            )
            .andExpect(status().isBadRequest());

        // Validate the Financeira in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamFinanceira() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        financeira.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFinanceiraMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(financeira)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Financeira in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteFinanceira() throws Exception {
        // Initialize the database
        insertedFinanceira = financeiraRepository.save(financeira);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the financeira
        restFinanceiraMockMvc
            .perform(delete(ENTITY_API_URL_ID, financeira.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return financeiraRepository.count();
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

    protected Financeira getPersistedFinanceira(Financeira financeira) {
        return financeiraRepository.findById(financeira.getId()).orElseThrow();
    }

    protected void assertPersistedFinanceiraToMatchAllProperties(Financeira expectedFinanceira) {
        assertFinanceiraAllPropertiesEquals(expectedFinanceira, getPersistedFinanceira(expectedFinanceira));
    }

    protected void assertPersistedFinanceiraToMatchUpdatableProperties(Financeira expectedFinanceira) {
        assertFinanceiraAllUpdatablePropertiesEquals(expectedFinanceira, getPersistedFinanceira(expectedFinanceira));
    }
}
