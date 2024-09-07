package com.hubcarspot.api.web.rest;

import static com.hubcarspot.api.domain.InstituicaoAsserts.*;
import static com.hubcarspot.api.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hubcarspot.api.IntegrationTest;
import com.hubcarspot.api.domain.Instituicao;
import com.hubcarspot.api.repository.InstituicaoRepository;
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
 * Integration tests for the {@link InstituicaoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InstituicaoResourceIT {

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

    private static final String DEFAULT_COMPLEMENTO = "AAAAAAAAAA";
    private static final String UPDATED_COMPLEMENTO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/instituicaos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private InstituicaoRepository instituicaoRepository;

    @Autowired
    private MockMvc restInstituicaoMockMvc;

    private Instituicao instituicao;

    private Instituicao insertedInstituicao;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Instituicao createEntity() {
        return new Instituicao()
            .nome(DEFAULT_NOME)
            .telefone(DEFAULT_TELEFONE)
            .cnpj(DEFAULT_CNPJ)
            .cep(DEFAULT_CEP)
            .endereco(DEFAULT_ENDERECO)
            .bairro(DEFAULT_BAIRRO)
            .cidade(DEFAULT_CIDADE)
            .numero(DEFAULT_NUMERO)
            .uf(DEFAULT_UF)
            .complemento(DEFAULT_COMPLEMENTO);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Instituicao createUpdatedEntity() {
        return new Instituicao()
            .nome(UPDATED_NOME)
            .telefone(UPDATED_TELEFONE)
            .cnpj(UPDATED_CNPJ)
            .cep(UPDATED_CEP)
            .endereco(UPDATED_ENDERECO)
            .bairro(UPDATED_BAIRRO)
            .cidade(UPDATED_CIDADE)
            .numero(UPDATED_NUMERO)
            .uf(UPDATED_UF)
            .complemento(UPDATED_COMPLEMENTO);
    }

    @BeforeEach
    public void initTest() {
        instituicao = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedInstituicao != null) {
            instituicaoRepository.delete(insertedInstituicao);
            insertedInstituicao = null;
        }
    }

    @Test
    void createInstituicao() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Instituicao
        var returnedInstituicao = om.readValue(
            restInstituicaoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(instituicao)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Instituicao.class
        );

        // Validate the Instituicao in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertInstituicaoUpdatableFieldsEquals(returnedInstituicao, getPersistedInstituicao(returnedInstituicao));

        insertedInstituicao = returnedInstituicao;
    }

    @Test
    void createInstituicaoWithExistingId() throws Exception {
        // Create the Instituicao with an existing ID
        instituicao.setId("existing_id");

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInstituicaoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(instituicao)))
            .andExpect(status().isBadRequest());

        // Validate the Instituicao in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkTelefoneIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        instituicao.setTelefone(null);

        // Create the Instituicao, which fails.

        restInstituicaoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(instituicao)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllInstituicaos() throws Exception {
        // Initialize the database
        insertedInstituicao = instituicaoRepository.save(instituicao);

        // Get all the instituicaoList
        restInstituicaoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(instituicao.getId())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].telefone").value(hasItem(DEFAULT_TELEFONE)))
            .andExpect(jsonPath("$.[*].cnpj").value(hasItem(DEFAULT_CNPJ)))
            .andExpect(jsonPath("$.[*].cep").value(hasItem(DEFAULT_CEP)))
            .andExpect(jsonPath("$.[*].endereco").value(hasItem(DEFAULT_ENDERECO)))
            .andExpect(jsonPath("$.[*].bairro").value(hasItem(DEFAULT_BAIRRO)))
            .andExpect(jsonPath("$.[*].cidade").value(hasItem(DEFAULT_CIDADE)))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)))
            .andExpect(jsonPath("$.[*].uf").value(hasItem(DEFAULT_UF)))
            .andExpect(jsonPath("$.[*].complemento").value(hasItem(DEFAULT_COMPLEMENTO)));
    }

    @Test
    void getInstituicao() throws Exception {
        // Initialize the database
        insertedInstituicao = instituicaoRepository.save(instituicao);

        // Get the instituicao
        restInstituicaoMockMvc
            .perform(get(ENTITY_API_URL_ID, instituicao.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(instituicao.getId()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.telefone").value(DEFAULT_TELEFONE))
            .andExpect(jsonPath("$.cnpj").value(DEFAULT_CNPJ))
            .andExpect(jsonPath("$.cep").value(DEFAULT_CEP))
            .andExpect(jsonPath("$.endereco").value(DEFAULT_ENDERECO))
            .andExpect(jsonPath("$.bairro").value(DEFAULT_BAIRRO))
            .andExpect(jsonPath("$.cidade").value(DEFAULT_CIDADE))
            .andExpect(jsonPath("$.numero").value(DEFAULT_NUMERO))
            .andExpect(jsonPath("$.uf").value(DEFAULT_UF))
            .andExpect(jsonPath("$.complemento").value(DEFAULT_COMPLEMENTO));
    }

    @Test
    void getNonExistingInstituicao() throws Exception {
        // Get the instituicao
        restInstituicaoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingInstituicao() throws Exception {
        // Initialize the database
        insertedInstituicao = instituicaoRepository.save(instituicao);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the instituicao
        Instituicao updatedInstituicao = instituicaoRepository.findById(instituicao.getId()).orElseThrow();
        updatedInstituicao
            .nome(UPDATED_NOME)
            .telefone(UPDATED_TELEFONE)
            .cnpj(UPDATED_CNPJ)
            .cep(UPDATED_CEP)
            .endereco(UPDATED_ENDERECO)
            .bairro(UPDATED_BAIRRO)
            .cidade(UPDATED_CIDADE)
            .numero(UPDATED_NUMERO)
            .uf(UPDATED_UF)
            .complemento(UPDATED_COMPLEMENTO);

        restInstituicaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedInstituicao.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedInstituicao))
            )
            .andExpect(status().isOk());

        // Validate the Instituicao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedInstituicaoToMatchAllProperties(updatedInstituicao);
    }

    @Test
    void putNonExistingInstituicao() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        instituicao.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInstituicaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, instituicao.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(instituicao))
            )
            .andExpect(status().isBadRequest());

        // Validate the Instituicao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchInstituicao() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        instituicao.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInstituicaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(instituicao))
            )
            .andExpect(status().isBadRequest());

        // Validate the Instituicao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamInstituicao() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        instituicao.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInstituicaoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(instituicao)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Instituicao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateInstituicaoWithPatch() throws Exception {
        // Initialize the database
        insertedInstituicao = instituicaoRepository.save(instituicao);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the instituicao using partial update
        Instituicao partialUpdatedInstituicao = new Instituicao();
        partialUpdatedInstituicao.setId(instituicao.getId());

        partialUpdatedInstituicao
            .nome(UPDATED_NOME)
            .cnpj(UPDATED_CNPJ)
            .cep(UPDATED_CEP)
            .numero(UPDATED_NUMERO)
            .complemento(UPDATED_COMPLEMENTO);

        restInstituicaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInstituicao.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInstituicao))
            )
            .andExpect(status().isOk());

        // Validate the Instituicao in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInstituicaoUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedInstituicao, instituicao),
            getPersistedInstituicao(instituicao)
        );
    }

    @Test
    void fullUpdateInstituicaoWithPatch() throws Exception {
        // Initialize the database
        insertedInstituicao = instituicaoRepository.save(instituicao);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the instituicao using partial update
        Instituicao partialUpdatedInstituicao = new Instituicao();
        partialUpdatedInstituicao.setId(instituicao.getId());

        partialUpdatedInstituicao
            .nome(UPDATED_NOME)
            .telefone(UPDATED_TELEFONE)
            .cnpj(UPDATED_CNPJ)
            .cep(UPDATED_CEP)
            .endereco(UPDATED_ENDERECO)
            .bairro(UPDATED_BAIRRO)
            .cidade(UPDATED_CIDADE)
            .numero(UPDATED_NUMERO)
            .uf(UPDATED_UF)
            .complemento(UPDATED_COMPLEMENTO);

        restInstituicaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInstituicao.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInstituicao))
            )
            .andExpect(status().isOk());

        // Validate the Instituicao in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInstituicaoUpdatableFieldsEquals(partialUpdatedInstituicao, getPersistedInstituicao(partialUpdatedInstituicao));
    }

    @Test
    void patchNonExistingInstituicao() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        instituicao.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInstituicaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, instituicao.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(instituicao))
            )
            .andExpect(status().isBadRequest());

        // Validate the Instituicao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchInstituicao() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        instituicao.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInstituicaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(instituicao))
            )
            .andExpect(status().isBadRequest());

        // Validate the Instituicao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamInstituicao() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        instituicao.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInstituicaoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(instituicao)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Instituicao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteInstituicao() throws Exception {
        // Initialize the database
        insertedInstituicao = instituicaoRepository.save(instituicao);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the instituicao
        restInstituicaoMockMvc
            .perform(delete(ENTITY_API_URL_ID, instituicao.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return instituicaoRepository.count();
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

    protected Instituicao getPersistedInstituicao(Instituicao instituicao) {
        return instituicaoRepository.findById(instituicao.getId()).orElseThrow();
    }

    protected void assertPersistedInstituicaoToMatchAllProperties(Instituicao expectedInstituicao) {
        assertInstituicaoAllPropertiesEquals(expectedInstituicao, getPersistedInstituicao(expectedInstituicao));
    }

    protected void assertPersistedInstituicaoToMatchUpdatableProperties(Instituicao expectedInstituicao) {
        assertInstituicaoAllUpdatablePropertiesEquals(expectedInstituicao, getPersistedInstituicao(expectedInstituicao));
    }
}
