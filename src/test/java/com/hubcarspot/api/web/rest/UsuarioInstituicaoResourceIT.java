package com.hubcarspot.api.web.rest;

import static com.hubcarspot.api.domain.UsuarioInstituicaoAsserts.*;
import static com.hubcarspot.api.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hubcarspot.api.IntegrationTest;
import com.hubcarspot.api.domain.UsuarioInstituicao;
import com.hubcarspot.api.repository.UsuarioInstituicaoRepository;
import com.hubcarspot.api.service.UsuarioInstituicaoService;
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
 * Integration tests for the {@link UsuarioInstituicaoResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class UsuarioInstituicaoResourceIT {

    private static final String DEFAULT_IDENTIFICADOR = "AAAAAAAAAA";
    private static final String UPDATED_IDENTIFICADOR = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_MASTER = false;
    private static final Boolean UPDATED_IS_MASTER = true;

    private static final String DEFAULT_ROLE = "AAAAAAAAAA";
    private static final String UPDATED_ROLE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_READ = false;
    private static final Boolean UPDATED_READ = true;

    private static final Boolean DEFAULT_WRITE = false;
    private static final Boolean UPDATED_WRITE = true;

    private static final Boolean DEFAULT_UPDATE = false;
    private static final Boolean UPDATED_UPDATE = true;

    private static final String ENTITY_API_URL = "/api/usuario-instituicaos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UsuarioInstituicaoRepository usuarioInstituicaoRepository;

    @Mock
    private UsuarioInstituicaoRepository usuarioInstituicaoRepositoryMock;

    @Mock
    private UsuarioInstituicaoService usuarioInstituicaoServiceMock;

    @Autowired
    private MockMvc restUsuarioInstituicaoMockMvc;

    private UsuarioInstituicao usuarioInstituicao;

    private UsuarioInstituicao insertedUsuarioInstituicao;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UsuarioInstituicao createEntity() {
        return new UsuarioInstituicao()
            .identificador(DEFAULT_IDENTIFICADOR)
            .isMaster(DEFAULT_IS_MASTER)
            .role(DEFAULT_ROLE)
            .read(DEFAULT_READ)
            .write(DEFAULT_WRITE)
            .update(DEFAULT_UPDATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UsuarioInstituicao createUpdatedEntity() {
        return new UsuarioInstituicao()
            .identificador(UPDATED_IDENTIFICADOR)
            .isMaster(UPDATED_IS_MASTER)
            .role(UPDATED_ROLE)
            .read(UPDATED_READ)
            .write(UPDATED_WRITE)
            .update(UPDATED_UPDATE);
    }

    @BeforeEach
    public void initTest() {
        usuarioInstituicao = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedUsuarioInstituicao != null) {
            usuarioInstituicaoRepository.delete(insertedUsuarioInstituicao);
            insertedUsuarioInstituicao = null;
        }
    }

    @Test
    void createUsuarioInstituicao() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the UsuarioInstituicao
        var returnedUsuarioInstituicao = om.readValue(
            restUsuarioInstituicaoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(usuarioInstituicao)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            UsuarioInstituicao.class
        );

        // Validate the UsuarioInstituicao in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertUsuarioInstituicaoUpdatableFieldsEquals(
            returnedUsuarioInstituicao,
            getPersistedUsuarioInstituicao(returnedUsuarioInstituicao)
        );

        insertedUsuarioInstituicao = returnedUsuarioInstituicao;
    }

    @Test
    void createUsuarioInstituicaoWithExistingId() throws Exception {
        // Create the UsuarioInstituicao with an existing ID
        usuarioInstituicao.setId("existing_id");

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUsuarioInstituicaoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(usuarioInstituicao)))
            .andExpect(status().isBadRequest());

        // Validate the UsuarioInstituicao in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkIdentificadorIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        usuarioInstituicao.setIdentificador(null);

        // Create the UsuarioInstituicao, which fails.

        restUsuarioInstituicaoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(usuarioInstituicao)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkIsMasterIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        usuarioInstituicao.setIsMaster(null);

        // Create the UsuarioInstituicao, which fails.

        restUsuarioInstituicaoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(usuarioInstituicao)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkRoleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        usuarioInstituicao.setRole(null);

        // Create the UsuarioInstituicao, which fails.

        restUsuarioInstituicaoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(usuarioInstituicao)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkReadIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        usuarioInstituicao.setRead(null);

        // Create the UsuarioInstituicao, which fails.

        restUsuarioInstituicaoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(usuarioInstituicao)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkWriteIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        usuarioInstituicao.setWrite(null);

        // Create the UsuarioInstituicao, which fails.

        restUsuarioInstituicaoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(usuarioInstituicao)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkUpdateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        usuarioInstituicao.setUpdate(null);

        // Create the UsuarioInstituicao, which fails.

        restUsuarioInstituicaoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(usuarioInstituicao)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllUsuarioInstituicaos() throws Exception {
        // Initialize the database
        insertedUsuarioInstituicao = usuarioInstituicaoRepository.save(usuarioInstituicao);

        // Get all the usuarioInstituicaoList
        restUsuarioInstituicaoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(usuarioInstituicao.getId())))
            .andExpect(jsonPath("$.[*].identificador").value(hasItem(DEFAULT_IDENTIFICADOR)))
            .andExpect(jsonPath("$.[*].isMaster").value(hasItem(DEFAULT_IS_MASTER.booleanValue())))
            .andExpect(jsonPath("$.[*].role").value(hasItem(DEFAULT_ROLE)))
            .andExpect(jsonPath("$.[*].read").value(hasItem(DEFAULT_READ.booleanValue())))
            .andExpect(jsonPath("$.[*].write").value(hasItem(DEFAULT_WRITE.booleanValue())))
            .andExpect(jsonPath("$.[*].update").value(hasItem(DEFAULT_UPDATE.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUsuarioInstituicaosWithEagerRelationshipsIsEnabled() throws Exception {
        when(usuarioInstituicaoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUsuarioInstituicaoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(usuarioInstituicaoServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUsuarioInstituicaosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(usuarioInstituicaoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUsuarioInstituicaoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(usuarioInstituicaoRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void getUsuarioInstituicao() throws Exception {
        // Initialize the database
        insertedUsuarioInstituicao = usuarioInstituicaoRepository.save(usuarioInstituicao);

        // Get the usuarioInstituicao
        restUsuarioInstituicaoMockMvc
            .perform(get(ENTITY_API_URL_ID, usuarioInstituicao.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(usuarioInstituicao.getId()))
            .andExpect(jsonPath("$.identificador").value(DEFAULT_IDENTIFICADOR))
            .andExpect(jsonPath("$.isMaster").value(DEFAULT_IS_MASTER.booleanValue()))
            .andExpect(jsonPath("$.role").value(DEFAULT_ROLE))
            .andExpect(jsonPath("$.read").value(DEFAULT_READ.booleanValue()))
            .andExpect(jsonPath("$.write").value(DEFAULT_WRITE.booleanValue()))
            .andExpect(jsonPath("$.update").value(DEFAULT_UPDATE.booleanValue()));
    }

    @Test
    void getNonExistingUsuarioInstituicao() throws Exception {
        // Get the usuarioInstituicao
        restUsuarioInstituicaoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingUsuarioInstituicao() throws Exception {
        // Initialize the database
        insertedUsuarioInstituicao = usuarioInstituicaoRepository.save(usuarioInstituicao);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the usuarioInstituicao
        UsuarioInstituicao updatedUsuarioInstituicao = usuarioInstituicaoRepository.findById(usuarioInstituicao.getId()).orElseThrow();
        updatedUsuarioInstituicao
            .identificador(UPDATED_IDENTIFICADOR)
            .isMaster(UPDATED_IS_MASTER)
            .role(UPDATED_ROLE)
            .read(UPDATED_READ)
            .write(UPDATED_WRITE)
            .update(UPDATED_UPDATE);

        restUsuarioInstituicaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedUsuarioInstituicao.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedUsuarioInstituicao))
            )
            .andExpect(status().isOk());

        // Validate the UsuarioInstituicao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedUsuarioInstituicaoToMatchAllProperties(updatedUsuarioInstituicao);
    }

    @Test
    void putNonExistingUsuarioInstituicao() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        usuarioInstituicao.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUsuarioInstituicaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, usuarioInstituicao.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(usuarioInstituicao))
            )
            .andExpect(status().isBadRequest());

        // Validate the UsuarioInstituicao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchUsuarioInstituicao() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        usuarioInstituicao.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUsuarioInstituicaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(usuarioInstituicao))
            )
            .andExpect(status().isBadRequest());

        // Validate the UsuarioInstituicao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamUsuarioInstituicao() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        usuarioInstituicao.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUsuarioInstituicaoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(usuarioInstituicao)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UsuarioInstituicao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateUsuarioInstituicaoWithPatch() throws Exception {
        // Initialize the database
        insertedUsuarioInstituicao = usuarioInstituicaoRepository.save(usuarioInstituicao);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the usuarioInstituicao using partial update
        UsuarioInstituicao partialUpdatedUsuarioInstituicao = new UsuarioInstituicao();
        partialUpdatedUsuarioInstituicao.setId(usuarioInstituicao.getId());

        partialUpdatedUsuarioInstituicao.role(UPDATED_ROLE).read(UPDATED_READ);

        restUsuarioInstituicaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUsuarioInstituicao.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUsuarioInstituicao))
            )
            .andExpect(status().isOk());

        // Validate the UsuarioInstituicao in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUsuarioInstituicaoUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedUsuarioInstituicao, usuarioInstituicao),
            getPersistedUsuarioInstituicao(usuarioInstituicao)
        );
    }

    @Test
    void fullUpdateUsuarioInstituicaoWithPatch() throws Exception {
        // Initialize the database
        insertedUsuarioInstituicao = usuarioInstituicaoRepository.save(usuarioInstituicao);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the usuarioInstituicao using partial update
        UsuarioInstituicao partialUpdatedUsuarioInstituicao = new UsuarioInstituicao();
        partialUpdatedUsuarioInstituicao.setId(usuarioInstituicao.getId());

        partialUpdatedUsuarioInstituicao
            .identificador(UPDATED_IDENTIFICADOR)
            .isMaster(UPDATED_IS_MASTER)
            .role(UPDATED_ROLE)
            .read(UPDATED_READ)
            .write(UPDATED_WRITE)
            .update(UPDATED_UPDATE);

        restUsuarioInstituicaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUsuarioInstituicao.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUsuarioInstituicao))
            )
            .andExpect(status().isOk());

        // Validate the UsuarioInstituicao in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUsuarioInstituicaoUpdatableFieldsEquals(
            partialUpdatedUsuarioInstituicao,
            getPersistedUsuarioInstituicao(partialUpdatedUsuarioInstituicao)
        );
    }

    @Test
    void patchNonExistingUsuarioInstituicao() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        usuarioInstituicao.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUsuarioInstituicaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, usuarioInstituicao.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(usuarioInstituicao))
            )
            .andExpect(status().isBadRequest());

        // Validate the UsuarioInstituicao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchUsuarioInstituicao() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        usuarioInstituicao.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUsuarioInstituicaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(usuarioInstituicao))
            )
            .andExpect(status().isBadRequest());

        // Validate the UsuarioInstituicao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamUsuarioInstituicao() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        usuarioInstituicao.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUsuarioInstituicaoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(usuarioInstituicao)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UsuarioInstituicao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteUsuarioInstituicao() throws Exception {
        // Initialize the database
        insertedUsuarioInstituicao = usuarioInstituicaoRepository.save(usuarioInstituicao);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the usuarioInstituicao
        restUsuarioInstituicaoMockMvc
            .perform(delete(ENTITY_API_URL_ID, usuarioInstituicao.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return usuarioInstituicaoRepository.count();
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

    protected UsuarioInstituicao getPersistedUsuarioInstituicao(UsuarioInstituicao usuarioInstituicao) {
        return usuarioInstituicaoRepository.findById(usuarioInstituicao.getId()).orElseThrow();
    }

    protected void assertPersistedUsuarioInstituicaoToMatchAllProperties(UsuarioInstituicao expectedUsuarioInstituicao) {
        assertUsuarioInstituicaoAllPropertiesEquals(expectedUsuarioInstituicao, getPersistedUsuarioInstituicao(expectedUsuarioInstituicao));
    }

    protected void assertPersistedUsuarioInstituicaoToMatchUpdatableProperties(UsuarioInstituicao expectedUsuarioInstituicao) {
        assertUsuarioInstituicaoAllUpdatablePropertiesEquals(
            expectedUsuarioInstituicao,
            getPersistedUsuarioInstituicao(expectedUsuarioInstituicao)
        );
    }
}
