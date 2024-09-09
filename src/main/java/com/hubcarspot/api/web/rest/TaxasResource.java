package com.hubcarspot.api.web.rest;

import com.hubcarspot.api.domain.Taxas;
import com.hubcarspot.api.repository.TaxasRepository;
import com.hubcarspot.api.service.TaxasService;
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
 * REST controller for managing {@link com.hubcarspot.api.domain.Taxas}.
 */
@RestController
@RequestMapping("/api/taxas")
public class TaxasResource {

    private static final Logger LOG = LoggerFactory.getLogger(TaxasResource.class);

    private static final String ENTITY_NAME = "Taxas";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TaxasService taxasService;

    private final TaxasRepository taxasRepository;

    public TaxasResource(TaxasService taxasService, TaxasRepository taxasRepository) {
        this.taxasService = taxasService;
        this.taxasRepository = taxasRepository;
    }

    /**
     * {@code POST  /taxas} : Create a new taxas.
     *
     * @param taxas the taxas to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new taxas, or with status {@code 400 (Bad Request)} if the taxas has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Taxas> createTaxas(@RequestBody Taxas taxas) throws Exception {
        LOG.debug("REST request to save Taxas : {}", taxas);
        if (taxas.getId() != null) {
            throw new BadRequestAlertException("A new taxas cannot already have an ID", ENTITY_NAME, "idexists");
        }
        taxas = taxasService.save(taxas);
        return ResponseEntity.created(new URI("/api/taxas/" + taxas.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, taxas.getId()))
            .body(taxas);
    }

    /**
     * {@code PUT  /taxas/:id} : Updates an existing taxas.
     *
     * @param id the id of the taxas to save.
     * @param taxas the taxas to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated taxas,
     * or with status {@code 400 (Bad Request)} if the taxas is not valid,
     * or with status {@code 500 (Internal Server Error)} if the taxas couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Taxas> updateTaxas(@PathVariable(value = "id", required = false) final String id, @RequestBody Taxas taxas)
        throws URISyntaxException {
        LOG.debug("REST request to update Taxas : {}, {}", id, taxas);
        if (taxas.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, taxas.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!taxasRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        taxas = taxasService.update(taxas);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, taxas.getId()))
            .body(taxas);
    }

    /**
     * {@code PATCH  /taxas/:id} : Partial updates given fields of an existing taxas, field will ignore if it is null
     *
     * @param id the id of the taxas to save.
     * @param taxas the taxas to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated taxas,
     * or with status {@code 400 (Bad Request)} if the taxas is not valid,
     * or with status {@code 404 (Not Found)} if the taxas is not found,
     * or with status {@code 500 (Internal Server Error)} if the taxas couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Taxas> partialUpdateTaxas(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody Taxas taxas
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Taxas partially : {}, {}", id, taxas);
        if (taxas.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, taxas.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!taxasRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Taxas> result = taxasService.partialUpdate(taxas);

        return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, taxas.getId()));
    }

    /**
     * {@code GET  /taxas} : get all the taxas.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of taxas in body.
     */
    @GetMapping("")
    public List<Taxas> getAllTaxas(@RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload)
        throws Exception {
        LOG.debug("REST request to get all Taxas");
        return taxasService.findAll();
    }

    /**
     * {@code GET  /taxas/:id} : get the "id" taxas.
     *
     * @param id the id of the taxas to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the taxas, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Taxas> getTaxas(@PathVariable("id") String id) {
        LOG.debug("REST request to get Taxas : {}", id);
        Optional<Taxas> taxas = taxasService.findOne(id);
        return ResponseUtil.wrapOrNotFound(taxas);
    }

    /**
     * {@code DELETE  /taxas/:id} : delete the "id" taxas.
     *
     * @param id the id of the taxas to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTaxas(@PathVariable("id") String id) {
        LOG.debug("REST request to delete Taxas : {}", id);
        taxasService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }
}
