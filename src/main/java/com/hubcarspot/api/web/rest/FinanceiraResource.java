package com.hubcarspot.api.web.rest;

import com.hubcarspot.api.domain.Financeira;
import com.hubcarspot.api.repository.FinanceiraRepository;
import com.hubcarspot.api.service.FinanceiraService;
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
 * REST controller for managing {@link com.hubcarspot.api.domain.Financeira}.
 */
@RestController
@RequestMapping("/api/financeiras")
public class FinanceiraResource {

    private static final Logger LOG = LoggerFactory.getLogger(FinanceiraResource.class);

    private static final String ENTITY_NAME = "Financeira";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FinanceiraService financeiraService;

    private final FinanceiraRepository financeiraRepository;

    public FinanceiraResource(FinanceiraService financeiraService, FinanceiraRepository financeiraRepository) {
        this.financeiraService = financeiraService;
        this.financeiraRepository = financeiraRepository;
    }

    /**
     * {@code POST  /financeiras} : Create a new financeira.
     *
     * @param financeira the financeira to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new financeira, or with status {@code 400 (Bad Request)} if the financeira has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Financeira> createFinanceira(@Valid @RequestBody Financeira financeira) throws Exception {
        LOG.debug("REST request to save Financeira : {}", financeira);
        if (financeira.getId() != null) {
            throw new BadRequestAlertException("A new financeira cannot already have an ID", ENTITY_NAME, "idexists");
        }
        financeira = financeiraService.save(financeira);
        return ResponseEntity.created(new URI("/api/financeiras/" + financeira.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, financeira.getId()))
            .body(financeira);
    }

    /**
     * {@code PUT  /financeiras/:id} : Updates an existing financeira.
     *
     * @param id the id of the financeira to save.
     * @param financeira the financeira to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated financeira,
     * or with status {@code 400 (Bad Request)} if the financeira is not valid,
     * or with status {@code 500 (Internal Server Error)} if the financeira couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Financeira> updateFinanceira(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody Financeira financeira
    ) throws URISyntaxException {
        LOG.debug("REST request to update Financeira : {}, {}", id, financeira);
        if (financeira.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, financeira.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!financeiraRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        financeira = financeiraService.update(financeira);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, financeira.getId()))
            .body(financeira);
    }

    /**
     * {@code PATCH  /financeiras/:id} : Partial updates given fields of an existing financeira, field will ignore if it is null
     *
     * @param id the id of the financeira to save.
     * @param financeira the financeira to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated financeira,
     * or with status {@code 400 (Bad Request)} if the financeira is not valid,
     * or with status {@code 404 (Not Found)} if the financeira is not found,
     * or with status {@code 500 (Internal Server Error)} if the financeira couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Financeira> partialUpdateFinanceira(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody Financeira financeira
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Financeira partially : {}, {}", id, financeira);
        if (financeira.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, financeira.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!financeiraRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Financeira> result = financeiraService.partialUpdate(financeira);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, financeira.getId())
        );
    }

    /**
     * {@code GET  /financeiras} : get all the financeiras.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of financeiras in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Financeira>> getAllFinanceiras(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) throws Exception {
        LOG.debug("REST request to get a page of Financeiras");
        Page<Financeira> page = financeiraService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /financeiras/:id} : get the "id" financeira.
     *
     * @param id the id of the financeira to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the financeira, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Financeira> getFinanceira(@PathVariable("id") String id) {
        LOG.debug("REST request to get Financeira : {}", id);
        Optional<Financeira> financeira = financeiraService.findOne(id);
        return ResponseUtil.wrapOrNotFound(financeira);
    }

    /**
     * {@code DELETE  /financeiras/:id} : delete the "id" financeira.
     *
     * @param id the id of the financeira to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFinanceira(@PathVariable("id") String id) {
        LOG.debug("REST request to delete Financeira : {}", id);
        financeiraService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }
}
