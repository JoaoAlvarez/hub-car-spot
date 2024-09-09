package com.hubcarspot.api.web.rest;

import com.hubcarspot.api.domain.CompraVeiculo;
import com.hubcarspot.api.repository.CompraVeiculoRepository;
import com.hubcarspot.api.service.CompraVeiculoService;
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
 * REST controller for managing {@link com.hubcarspot.api.domain.CompraVeiculo}.
 */
@RestController
@RequestMapping("/api/compra-veiculos")
public class CompraVeiculoResource {

    private static final Logger LOG = LoggerFactory.getLogger(CompraVeiculoResource.class);

    private static final String ENTITY_NAME = "Compra de Veiculo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CompraVeiculoService compraVeiculoService;

    private final CompraVeiculoRepository compraVeiculoRepository;

    public CompraVeiculoResource(CompraVeiculoService compraVeiculoService, CompraVeiculoRepository compraVeiculoRepository) {
        this.compraVeiculoService = compraVeiculoService;
        this.compraVeiculoRepository = compraVeiculoRepository;
    }

    /**
     * {@code POST  /compra-veiculos} : Create a new compraVeiculo.
     *
     * @param compraVeiculo the compraVeiculo to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new compraVeiculo, or with status {@code 400 (Bad Request)} if the compraVeiculo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CompraVeiculo> createCompraVeiculo(@Valid @RequestBody CompraVeiculo compraVeiculo) throws Exception {
        LOG.debug("REST request to save CompraVeiculo : {}", compraVeiculo);
        if (compraVeiculo.getId() != null) {
            throw new BadRequestAlertException("A new compraVeiculo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        compraVeiculo = compraVeiculoService.save(compraVeiculo);
        return ResponseEntity.created(new URI("/api/compra-veiculos/" + compraVeiculo.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, compraVeiculo.getId()))
            .body(compraVeiculo);
    }

    /**
     * {@code PUT  /compra-veiculos/:id} : Updates an existing compraVeiculo.
     *
     * @param id the id of the compraVeiculo to save.
     * @param compraVeiculo the compraVeiculo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated compraVeiculo,
     * or with status {@code 400 (Bad Request)} if the compraVeiculo is not valid,
     * or with status {@code 500 (Internal Server Error)} if the compraVeiculo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CompraVeiculo> updateCompraVeiculo(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody CompraVeiculo compraVeiculo
    ) throws URISyntaxException {
        LOG.debug("REST request to update CompraVeiculo : {}, {}", id, compraVeiculo);
        if (compraVeiculo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, compraVeiculo.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!compraVeiculoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        compraVeiculo = compraVeiculoService.update(compraVeiculo);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, compraVeiculo.getId()))
            .body(compraVeiculo);
    }

    /**
     * {@code PATCH  /compra-veiculos/:id} : Partial updates given fields of an existing compraVeiculo, field will ignore if it is null
     *
     * @param id the id of the compraVeiculo to save.
     * @param compraVeiculo the compraVeiculo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated compraVeiculo,
     * or with status {@code 400 (Bad Request)} if the compraVeiculo is not valid,
     * or with status {@code 404 (Not Found)} if the compraVeiculo is not found,
     * or with status {@code 500 (Internal Server Error)} if the compraVeiculo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CompraVeiculo> partialUpdateCompraVeiculo(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody CompraVeiculo compraVeiculo
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CompraVeiculo partially : {}, {}", id, compraVeiculo);
        if (compraVeiculo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, compraVeiculo.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!compraVeiculoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CompraVeiculo> result = compraVeiculoService.partialUpdate(compraVeiculo);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, compraVeiculo.getId())
        );
    }

    /**
     * {@code GET  /compra-veiculos} : get all the compraVeiculos.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of compraVeiculos in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CompraVeiculo>> getAllCompraVeiculos(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) throws Exception {
        LOG.debug("REST request to get a page of CompraVeiculos");
        Page<CompraVeiculo> page = compraVeiculoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /compra-veiculos/:id} : get the "id" compraVeiculo.
     *
     * @param id the id of the compraVeiculo to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the compraVeiculo, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CompraVeiculo> getCompraVeiculo(@PathVariable("id") String id) {
        LOG.debug("REST request to get CompraVeiculo : {}", id);
        Optional<CompraVeiculo> compraVeiculo = compraVeiculoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(compraVeiculo);
    }

    /**
     * {@code DELETE  /compra-veiculos/:id} : delete the "id" compraVeiculo.
     *
     * @param id the id of the compraVeiculo to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompraVeiculo(@PathVariable("id") String id) {
        LOG.debug("REST request to delete CompraVeiculo : {}", id);
        compraVeiculoService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }
}
