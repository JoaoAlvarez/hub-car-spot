package com.hubcarspot.api.web.rest;

import com.hubcarspot.api.domain.Veiculo;
import com.hubcarspot.api.repository.VeiculoRepository;
import com.hubcarspot.api.service.VeiculoService;
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
 * REST controller for managing {@link com.hubcarspot.api.domain.Veiculo}.
 */
@RestController
@RequestMapping("/api/veiculos")
public class VeiculoResource {

    private static final Logger LOG = LoggerFactory.getLogger(VeiculoResource.class);

    private static final String ENTITY_NAME = "veiculo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VeiculoService veiculoService;

    private final VeiculoRepository veiculoRepository;

    public VeiculoResource(VeiculoService veiculoService, VeiculoRepository veiculoRepository) {
        this.veiculoService = veiculoService;
        this.veiculoRepository = veiculoRepository;
    }

    /**
     * {@code POST  /veiculos} : Create a new veiculo.
     *
     * @param veiculo the veiculo to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new veiculo, or with status {@code 400 (Bad Request)} if the veiculo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Veiculo> createVeiculo(@Valid @RequestBody Veiculo veiculo) throws URISyntaxException {
        LOG.debug("REST request to save Veiculo : {}", veiculo);
        if (veiculo.getId() != null) {
            throw new BadRequestAlertException("A new veiculo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        veiculo = veiculoService.save(veiculo);
        return ResponseEntity.created(new URI("/api/veiculos/" + veiculo.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, veiculo.getId()))
            .body(veiculo);
    }

    /**
     * {@code PUT  /veiculos/:id} : Updates an existing veiculo.
     *
     * @param id the id of the veiculo to save.
     * @param veiculo the veiculo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated veiculo,
     * or with status {@code 400 (Bad Request)} if the veiculo is not valid,
     * or with status {@code 500 (Internal Server Error)} if the veiculo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Veiculo> updateVeiculo(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody Veiculo veiculo
    ) throws URISyntaxException {
        LOG.debug("REST request to update Veiculo : {}, {}", id, veiculo);
        if (veiculo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, veiculo.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!veiculoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        veiculo = veiculoService.update(veiculo);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, veiculo.getId()))
            .body(veiculo);
    }

    /**
     * {@code PATCH  /veiculos/:id} : Partial updates given fields of an existing veiculo, field will ignore if it is null
     *
     * @param id the id of the veiculo to save.
     * @param veiculo the veiculo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated veiculo,
     * or with status {@code 400 (Bad Request)} if the veiculo is not valid,
     * or with status {@code 404 (Not Found)} if the veiculo is not found,
     * or with status {@code 500 (Internal Server Error)} if the veiculo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Veiculo> partialUpdateVeiculo(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody Veiculo veiculo
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Veiculo partially : {}, {}", id, veiculo);
        if (veiculo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, veiculo.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!veiculoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Veiculo> result = veiculoService.partialUpdate(veiculo);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, veiculo.getId())
        );
    }

    /**
     * {@code GET  /veiculos} : get all the veiculos.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of veiculos in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Veiculo>> getAllVeiculos(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Veiculos");
        Page<Veiculo> page = veiculoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /veiculos/:id} : get the "id" veiculo.
     *
     * @param id the id of the veiculo to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the veiculo, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Veiculo> getVeiculo(@PathVariable("id") String id) {
        LOG.debug("REST request to get Veiculo : {}", id);
        Optional<Veiculo> veiculo = veiculoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(veiculo);
    }

    /**
     * {@code DELETE  /veiculos/:id} : delete the "id" veiculo.
     *
     * @param id the id of the veiculo to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVeiculo(@PathVariable("id") String id) {
        LOG.debug("REST request to delete Veiculo : {}", id);
        veiculoService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }
}
