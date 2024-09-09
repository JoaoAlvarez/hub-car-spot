package com.hubcarspot.api.web.rest;

import com.hubcarspot.api.domain.Combustivel;
import com.hubcarspot.api.repository.CombustivelRepository;
import com.hubcarspot.api.service.CombustivelService;
import com.hubcarspot.api.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.hubcarspot.api.domain.Combustivel}.
 */
@RestController
@RequestMapping("/api/combustivels")
public class CombustivelResource {

    private static final Logger LOG = LoggerFactory.getLogger(CombustivelResource.class);

    private static final String ENTITY_NAME = "Combustivel";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CombustivelService combustivelService;

    private final CombustivelRepository combustivelRepository;

    public CombustivelResource(CombustivelService combustivelService, CombustivelRepository combustivelRepository) {
        this.combustivelService = combustivelService;
        this.combustivelRepository = combustivelRepository;
    }

    /**
     * {@code POST  /combustivels} : Create a new combustivel.
     *
     * @param combustivel the combustivel to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new combustivel, or with status {@code 400 (Bad Request)} if the combustivel has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Combustivel> createCombustivel(@RequestBody Combustivel combustivel) throws URISyntaxException {
        LOG.debug("REST request to save Combustivel : {}", combustivel);
        if (combustivel.getId() != null) {
            throw new BadRequestAlertException("A new combustivel cannot already have an ID", ENTITY_NAME, "idexists");
        }
        combustivel = combustivelService.save(combustivel);
        return ResponseEntity.created(new URI("/api/combustivels/" + combustivel.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, combustivel.getId()))
            .body(combustivel);
    }

    /**
     * {@code PUT  /combustivels/:id} : Updates an existing combustivel.
     *
     * @param id the id of the combustivel to save.
     * @param combustivel the combustivel to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated combustivel,
     * or with status {@code 400 (Bad Request)} if the combustivel is not valid,
     * or with status {@code 500 (Internal Server Error)} if the combustivel couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Combustivel> updateCombustivel(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody Combustivel combustivel
    ) throws URISyntaxException {
        LOG.debug("REST request to update Combustivel : {}, {}", id, combustivel);
        if (combustivel.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, combustivel.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!combustivelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        combustivel = combustivelService.update(combustivel);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, combustivel.getId()))
            .body(combustivel);
    }

    /**
     * {@code PATCH  /combustivels/:id} : Partial updates given fields of an existing combustivel, field will ignore if it is null
     *
     * @param id the id of the combustivel to save.
     * @param combustivel the combustivel to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated combustivel,
     * or with status {@code 400 (Bad Request)} if the combustivel is not valid,
     * or with status {@code 404 (Not Found)} if the combustivel is not found,
     * or with status {@code 500 (Internal Server Error)} if the combustivel couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Combustivel> partialUpdateCombustivel(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody Combustivel combustivel
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Combustivel partially : {}, {}", id, combustivel);
        if (combustivel.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, combustivel.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!combustivelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Combustivel> result = combustivelService.partialUpdate(combustivel);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, combustivel.getId())
        );
    }

    /**
     * {@code GET  /combustivels} : get all the combustivels.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of combustivels in body.
     */
    @GetMapping("")
    public List<Combustivel> getAllCombustivels() {
        LOG.debug("REST request to get all Combustivels");
        return combustivelService.findAll();
    }

    /**
     * {@code GET  /combustivels/:id} : get the "id" combustivel.
     *
     * @param id the id of the combustivel to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the combustivel, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Combustivel> getCombustivel(@PathVariable("id") String id) {
        LOG.debug("REST request to get Combustivel : {}", id);
        Optional<Combustivel> combustivel = combustivelService.findOne(id);
        return ResponseUtil.wrapOrNotFound(combustivel);
    }

    /**
     * {@code DELETE  /combustivels/:id} : delete the "id" combustivel.
     *
     * @param id the id of the combustivel to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCombustivel(@PathVariable("id") String id) {
        LOG.debug("REST request to delete Combustivel : {}", id);
        combustivelService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }
}
