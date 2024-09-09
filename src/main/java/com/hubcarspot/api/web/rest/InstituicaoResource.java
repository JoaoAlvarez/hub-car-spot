package com.hubcarspot.api.web.rest;

import com.hubcarspot.api.domain.Instituicao;
import com.hubcarspot.api.repository.InstituicaoRepository;
import com.hubcarspot.api.service.InstituicaoService;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.hubcarspot.api.domain.Instituicao}.
 */
@RestController
@RequestMapping("/api/instituicaos")
public class InstituicaoResource {

    private static final Logger LOG = LoggerFactory.getLogger(InstituicaoResource.class);

    private static final String ENTITY_NAME = "Instituicao";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InstituicaoService instituicaoService;

    private final InstituicaoRepository instituicaoRepository;

    public InstituicaoResource(InstituicaoService instituicaoService, InstituicaoRepository instituicaoRepository) {
        this.instituicaoService = instituicaoService;
        this.instituicaoRepository = instituicaoRepository;
    }

    /**
     * {@code POST  /instituicaos} : Create a new instituicao.
     *
     * @param instituicao the instituicao to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new instituicao, or with status {@code 400 (Bad Request)} if the instituicao has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PostMapping("")
    public ResponseEntity<Instituicao> createInstituicao(@Valid @RequestBody Instituicao instituicao) throws URISyntaxException {
        LOG.debug("REST request to save Instituicao : {}", instituicao);
        if (instituicao.getId() != null) {
            throw new BadRequestAlertException("A new instituicao cannot already have an ID", ENTITY_NAME, "idexists");
        }
        instituicao = instituicaoService.save(instituicao);
        return ResponseEntity.created(new URI("/api/instituicaos/" + instituicao.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, instituicao.getId()))
            .body(instituicao);
    }

    /**
     * {@code PUT  /instituicaos/:id} : Updates an existing instituicao.
     *
     * @param id the id of the instituicao to save.
     * @param instituicao the instituicao to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated instituicao,
     * or with status {@code 400 (Bad Request)} if the instituicao is not valid,
     * or with status {@code 500 (Internal Server Error)} if the instituicao couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Instituicao> updateInstituicao(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody Instituicao instituicao
    ) throws URISyntaxException {
        LOG.debug("REST request to update Instituicao : {}, {}", id, instituicao);
        if (instituicao.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, instituicao.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!instituicaoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        instituicao = instituicaoService.update(instituicao);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, instituicao.getId()))
            .body(instituicao);
    }

    /**
     * {@code PATCH  /instituicaos/:id} : Partial updates given fields of an existing instituicao, field will ignore if it is null
     *
     * @param id the id of the instituicao to save.
     * @param instituicao the instituicao to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated instituicao,
     * or with status {@code 400 (Bad Request)} if the instituicao is not valid,
     * or with status {@code 404 (Not Found)} if the instituicao is not found,
     * or with status {@code 500 (Internal Server Error)} if the instituicao couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Instituicao> partialUpdateInstituicao(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody Instituicao instituicao
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Instituicao partially : {}, {}", id, instituicao);
        if (instituicao.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, instituicao.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!instituicaoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Instituicao> result = instituicaoService.partialUpdate(instituicao);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, instituicao.getId())
        );
    }

    /**
     * {@code GET  /instituicaos} : get all the instituicaos.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of instituicaos in body.
     */
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @GetMapping("")
    public List<Instituicao> getAllInstituicaos() {
        LOG.debug("REST request to get all Instituicaos");
        return instituicaoService.findAll();
    }

    /**
     * {@code GET  /instituicaos/:id} : get the "id" instituicao.
     *
     * @param id the id of the instituicao to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the instituicao, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Instituicao> getInstituicao(@PathVariable("id") String id) {
        LOG.debug("REST request to get Instituicao : {}", id);
        Optional<Instituicao> instituicao = instituicaoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(instituicao);
    }

    /**
     * {@code DELETE  /instituicaos/:id} : delete the "id" instituicao.
     *
     * @param id the id of the instituicao to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInstituicao(@PathVariable("id") String id) {
        LOG.debug("REST request to delete Instituicao : {}", id);
        instituicaoService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }
}
