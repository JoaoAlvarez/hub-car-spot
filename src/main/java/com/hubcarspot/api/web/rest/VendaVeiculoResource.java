package com.hubcarspot.api.web.rest;

import com.hubcarspot.api.domain.VendaVeiculo;
import com.hubcarspot.api.repository.VendaVeiculoRepository;
import com.hubcarspot.api.service.VendaVeiculoService;
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
 * REST controller for managing {@link com.hubcarspot.api.domain.VendaVeiculo}.
 */
@RestController
@RequestMapping("/api/venda-veiculos")
public class VendaVeiculoResource {

    private static final Logger LOG = LoggerFactory.getLogger(VendaVeiculoResource.class);

    private static final String ENTITY_NAME = "vendaVeiculo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VendaVeiculoService vendaVeiculoService;

    private final VendaVeiculoRepository vendaVeiculoRepository;

    public VendaVeiculoResource(VendaVeiculoService vendaVeiculoService, VendaVeiculoRepository vendaVeiculoRepository) {
        this.vendaVeiculoService = vendaVeiculoService;
        this.vendaVeiculoRepository = vendaVeiculoRepository;
    }

    /**
     * {@code POST  /venda-veiculos} : Create a new vendaVeiculo.
     *
     * @param vendaVeiculo the vendaVeiculo to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new vendaVeiculo, or with status {@code 400 (Bad Request)} if the vendaVeiculo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<VendaVeiculo> createVendaVeiculo(@Valid @RequestBody VendaVeiculo vendaVeiculo) throws URISyntaxException {
        LOG.debug("REST request to save VendaVeiculo : {}", vendaVeiculo);
        if (vendaVeiculo.getId() != null) {
            throw new BadRequestAlertException("A new vendaVeiculo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        vendaVeiculo = vendaVeiculoService.save(vendaVeiculo);
        return ResponseEntity.created(new URI("/api/venda-veiculos/" + vendaVeiculo.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, vendaVeiculo.getId()))
            .body(vendaVeiculo);
    }

    /**
     * {@code PUT  /venda-veiculos/:id} : Updates an existing vendaVeiculo.
     *
     * @param id the id of the vendaVeiculo to save.
     * @param vendaVeiculo the vendaVeiculo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vendaVeiculo,
     * or with status {@code 400 (Bad Request)} if the vendaVeiculo is not valid,
     * or with status {@code 500 (Internal Server Error)} if the vendaVeiculo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<VendaVeiculo> updateVendaVeiculo(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody VendaVeiculo vendaVeiculo
    ) throws URISyntaxException {
        LOG.debug("REST request to update VendaVeiculo : {}, {}", id, vendaVeiculo);
        if (vendaVeiculo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vendaVeiculo.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vendaVeiculoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        vendaVeiculo = vendaVeiculoService.update(vendaVeiculo);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, vendaVeiculo.getId()))
            .body(vendaVeiculo);
    }

    /**
     * {@code PATCH  /venda-veiculos/:id} : Partial updates given fields of an existing vendaVeiculo, field will ignore if it is null
     *
     * @param id the id of the vendaVeiculo to save.
     * @param vendaVeiculo the vendaVeiculo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vendaVeiculo,
     * or with status {@code 400 (Bad Request)} if the vendaVeiculo is not valid,
     * or with status {@code 404 (Not Found)} if the vendaVeiculo is not found,
     * or with status {@code 500 (Internal Server Error)} if the vendaVeiculo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<VendaVeiculo> partialUpdateVendaVeiculo(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody VendaVeiculo vendaVeiculo
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update VendaVeiculo partially : {}, {}", id, vendaVeiculo);
        if (vendaVeiculo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vendaVeiculo.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vendaVeiculoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<VendaVeiculo> result = vendaVeiculoService.partialUpdate(vendaVeiculo);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, vendaVeiculo.getId())
        );
    }

    /**
     * {@code GET  /venda-veiculos} : get all the vendaVeiculos.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of vendaVeiculos in body.
     */
    @GetMapping("")
    public ResponseEntity<List<VendaVeiculo>> getAllVendaVeiculos(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of VendaVeiculos");
        Page<VendaVeiculo> page;
        if (eagerload) {
            page = vendaVeiculoService.findAllWithEagerRelationships(pageable);
        } else {
            page = vendaVeiculoService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /venda-veiculos/:id} : get the "id" vendaVeiculo.
     *
     * @param id the id of the vendaVeiculo to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the vendaVeiculo, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<VendaVeiculo> getVendaVeiculo(@PathVariable("id") String id) {
        LOG.debug("REST request to get VendaVeiculo : {}", id);
        Optional<VendaVeiculo> vendaVeiculo = vendaVeiculoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(vendaVeiculo);
    }

    /**
     * {@code DELETE  /venda-veiculos/:id} : delete the "id" vendaVeiculo.
     *
     * @param id the id of the vendaVeiculo to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVendaVeiculo(@PathVariable("id") String id) {
        LOG.debug("REST request to delete VendaVeiculo : {}", id);
        vendaVeiculoService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }
}
