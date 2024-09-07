package com.hubcarspot.api.web.rest;

import com.hubcarspot.api.domain.TrocaVeiculo;
import com.hubcarspot.api.repository.TrocaVeiculoRepository;
import com.hubcarspot.api.service.TrocaVeiculoService;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.hubcarspot.api.domain.TrocaVeiculo}.
 */
@RestController
@RequestMapping("/api/troca-veiculos")
public class TrocaVeiculoResource {

    private static final Logger LOG = LoggerFactory.getLogger(TrocaVeiculoResource.class);

    private static final String ENTITY_NAME = "trocaVeiculo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TrocaVeiculoService trocaVeiculoService;

    private final TrocaVeiculoRepository trocaVeiculoRepository;

    public TrocaVeiculoResource(TrocaVeiculoService trocaVeiculoService, TrocaVeiculoRepository trocaVeiculoRepository) {
        this.trocaVeiculoService = trocaVeiculoService;
        this.trocaVeiculoRepository = trocaVeiculoRepository;
    }

    /**
     * {@code POST  /troca-veiculos} : Create a new trocaVeiculo.
     *
     * @param trocaVeiculo the trocaVeiculo to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new trocaVeiculo, or with status {@code 400 (Bad Request)} if the trocaVeiculo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TrocaVeiculo> createTrocaVeiculo(@Valid @RequestBody TrocaVeiculo trocaVeiculo) throws URISyntaxException {
        LOG.debug("REST request to save TrocaVeiculo : {}", trocaVeiculo);
        if (trocaVeiculo.getId() != null) {
            throw new BadRequestAlertException("A new trocaVeiculo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        trocaVeiculo = trocaVeiculoService.save(trocaVeiculo);
        return ResponseEntity.created(new URI("/api/troca-veiculos/" + trocaVeiculo.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, trocaVeiculo.getId()))
            .body(trocaVeiculo);
    }

    /**
     * {@code PUT  /troca-veiculos/:id} : Updates an existing trocaVeiculo.
     *
     * @param id the id of the trocaVeiculo to save.
     * @param trocaVeiculo the trocaVeiculo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated trocaVeiculo,
     * or with status {@code 400 (Bad Request)} if the trocaVeiculo is not valid,
     * or with status {@code 500 (Internal Server Error)} if the trocaVeiculo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TrocaVeiculo> updateTrocaVeiculo(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody TrocaVeiculo trocaVeiculo
    ) throws URISyntaxException {
        LOG.debug("REST request to update TrocaVeiculo : {}, {}", id, trocaVeiculo);
        if (trocaVeiculo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, trocaVeiculo.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!trocaVeiculoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        trocaVeiculo = trocaVeiculoService.update(trocaVeiculo);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, trocaVeiculo.getId()))
            .body(trocaVeiculo);
    }

    /**
     * {@code PATCH  /troca-veiculos/:id} : Partial updates given fields of an existing trocaVeiculo, field will ignore if it is null
     *
     * @param id the id of the trocaVeiculo to save.
     * @param trocaVeiculo the trocaVeiculo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated trocaVeiculo,
     * or with status {@code 400 (Bad Request)} if the trocaVeiculo is not valid,
     * or with status {@code 404 (Not Found)} if the trocaVeiculo is not found,
     * or with status {@code 500 (Internal Server Error)} if the trocaVeiculo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TrocaVeiculo> partialUpdateTrocaVeiculo(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody TrocaVeiculo trocaVeiculo
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update TrocaVeiculo partially : {}, {}", id, trocaVeiculo);
        if (trocaVeiculo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, trocaVeiculo.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!trocaVeiculoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TrocaVeiculo> result = trocaVeiculoService.partialUpdate(trocaVeiculo);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, trocaVeiculo.getId())
        );
    }

    /**
     * {@code GET  /troca-veiculos} : get all the trocaVeiculos.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of trocaVeiculos in body.
     */
    @GetMapping("")
    public ResponseEntity<List<TrocaVeiculo>> getAllTrocaVeiculos(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of TrocaVeiculos");
        Page<TrocaVeiculo> page;
        if (eagerload) {
            page = trocaVeiculoService.findAllWithEagerRelationships(pageable);
        } else {
            page = trocaVeiculoService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /troca-veiculos/:id} : get the "id" trocaVeiculo.
     *
     * @param id the id of the trocaVeiculo to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the trocaVeiculo, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TrocaVeiculo> getTrocaVeiculo(@PathVariable("id") String id) {
        LOG.debug("REST request to get TrocaVeiculo : {}", id);
        Optional<TrocaVeiculo> trocaVeiculo = trocaVeiculoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(trocaVeiculo);
    }

    /**
     * {@code DELETE  /troca-veiculos/:id} : delete the "id" trocaVeiculo.
     *
     * @param id the id of the trocaVeiculo to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrocaVeiculo(@PathVariable("id") String id) {
        LOG.debug("REST request to delete TrocaVeiculo : {}", id);
        trocaVeiculoService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }
}
