package com.hubcarspot.api.web.rest;

import com.hubcarspot.api.domain.UsuarioInstituicao;
import com.hubcarspot.api.repository.UsuarioInstituicaoRepository;
import com.hubcarspot.api.service.UsuarioInstituicaoService;
import com.hubcarspot.api.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.hubcarspot.api.domain.UsuarioInstituicao}.
 */
@RestController
@RequestMapping("/api/usuario-instituicaos")
public class UsuarioInstituicaoResource {

    private static final Logger LOG = LoggerFactory.getLogger(UsuarioInstituicaoResource.class);

    private static final String ENTITY_NAME = "usuarioInstituicao";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UsuarioInstituicaoService usuarioInstituicaoService;

    private final UsuarioInstituicaoRepository usuarioInstituicaoRepository;

    public UsuarioInstituicaoResource(
        UsuarioInstituicaoService usuarioInstituicaoService,
        UsuarioInstituicaoRepository usuarioInstituicaoRepository
    ) {
        this.usuarioInstituicaoService = usuarioInstituicaoService;
        this.usuarioInstituicaoRepository = usuarioInstituicaoRepository;
    }

    /**
     * {@code POST  /usuario-instituicaos} : Create a new usuarioInstituicao.
     *
     * @param usuarioInstituicao the usuarioInstituicao to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new usuarioInstituicao, or with status {@code 400 (Bad Request)} if the usuarioInstituicao has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<UsuarioInstituicao> createUsuarioInstituicao(@Valid @RequestBody UsuarioInstituicao usuarioInstituicao)
        throws URISyntaxException {
        LOG.debug("REST request to save UsuarioInstituicao : {}", usuarioInstituicao);
        if (usuarioInstituicao.getId() != null) {
            throw new BadRequestAlertException("A new usuarioInstituicao cannot already have an ID", ENTITY_NAME, "idexists");
        }
        usuarioInstituicao = usuarioInstituicaoService.save(usuarioInstituicao);
        return ResponseEntity.created(new URI("/api/usuario-instituicaos/" + usuarioInstituicao.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, usuarioInstituicao.getId()))
            .body(usuarioInstituicao);
    }

    /**
     * {@code PUT  /usuario-instituicaos/:id} : Updates an existing usuarioInstituicao.
     *
     * @param id the id of the usuarioInstituicao to save.
     * @param usuarioInstituicao the usuarioInstituicao to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated usuarioInstituicao,
     * or with status {@code 400 (Bad Request)} if the usuarioInstituicao is not valid,
     * or with status {@code 500 (Internal Server Error)} if the usuarioInstituicao couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioInstituicao> updateUsuarioInstituicao(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody UsuarioInstituicao usuarioInstituicao
    ) throws URISyntaxException {
        LOG.debug("REST request to update UsuarioInstituicao : {}, {}", id, usuarioInstituicao);
        if (usuarioInstituicao.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, usuarioInstituicao.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!usuarioInstituicaoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        usuarioInstituicao = usuarioInstituicaoService.update(usuarioInstituicao);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, usuarioInstituicao.getId()))
            .body(usuarioInstituicao);
    }

    /**
     * {@code PATCH  /usuario-instituicaos/:id} : Partial updates given fields of an existing usuarioInstituicao, field will ignore if it is null
     *
     * @param id the id of the usuarioInstituicao to save.
     * @param usuarioInstituicao the usuarioInstituicao to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated usuarioInstituicao,
     * or with status {@code 400 (Bad Request)} if the usuarioInstituicao is not valid,
     * or with status {@code 404 (Not Found)} if the usuarioInstituicao is not found,
     * or with status {@code 500 (Internal Server Error)} if the usuarioInstituicao couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UsuarioInstituicao> partialUpdateUsuarioInstituicao(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody UsuarioInstituicao usuarioInstituicao
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update UsuarioInstituicao partially : {}, {}", id, usuarioInstituicao);
        if (usuarioInstituicao.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, usuarioInstituicao.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!usuarioInstituicaoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UsuarioInstituicao> result = usuarioInstituicaoService.partialUpdate(usuarioInstituicao);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, usuarioInstituicao.getId())
        );
    }

    /**
     * {@code GET  /usuario-instituicaos} : get all the usuarioInstituicaos.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of usuarioInstituicaos in body.
     */
    @GetMapping("")
    public List<UsuarioInstituicao> getAllUsuarioInstituicaos(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get all UsuarioInstituicaos");
        return usuarioInstituicaoService.findAll();
    }

    /**
     * {@code GET  /usuario-instituicaos/:id} : get the "id" usuarioInstituicao.
     *
     * @param id the id of the usuarioInstituicao to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the usuarioInstituicao, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioInstituicao> getUsuarioInstituicao(@PathVariable("id") String id) {
        LOG.debug("REST request to get UsuarioInstituicao : {}", id);
        Optional<UsuarioInstituicao> usuarioInstituicao = usuarioInstituicaoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(usuarioInstituicao);
    }

    /**
     * {@code DELETE  /usuario-instituicaos/:id} : delete the "id" usuarioInstituicao.
     *
     * @param id the id of the usuarioInstituicao to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuarioInstituicao(@PathVariable("id") String id) {
        LOG.debug("REST request to delete UsuarioInstituicao : {}", id);
        usuarioInstituicaoService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }
}
