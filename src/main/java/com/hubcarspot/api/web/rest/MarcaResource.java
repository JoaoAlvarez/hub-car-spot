package com.hubcarspot.api.web.rest;

import com.hubcarspot.api.domain.Marca;
import com.hubcarspot.api.repository.MarcaRepository;
import com.hubcarspot.api.service.MarcaService;
import com.hubcarspot.api.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.hubcarspot.api.domain.Marca}.
 */
@RestController
@RequestMapping("/api/marcas")
public class MarcaResource {

    private static final Logger LOG = LoggerFactory.getLogger(MarcaResource.class);

    private static final String ENTITY_NAME = "marca";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MarcaService marcaService;

    private final MarcaRepository marcaRepository;

    public MarcaResource(MarcaService marcaService, MarcaRepository marcaRepository) {
        this.marcaService = marcaService;
        this.marcaRepository = marcaRepository;
    }

    /**
     * {@code POST  /marcas} : Create a new marca.
     *
     * @param marca the marca to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new marca, or with status {@code 400 (Bad Request)} if the marca has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Marca> createMarca(@RequestBody Marca marca) throws URISyntaxException {
        LOG.debug("REST request to save Marca : {}", marca);
        if (marca.getId() != null) {
            throw new BadRequestAlertException("A new marca cannot already have an ID", ENTITY_NAME, "idexists");
        }
        marca = marcaService.save(marca);
        return ResponseEntity.created(new URI("/api/marcas/" + marca.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, marca.getId()))
            .body(marca);
    }

    /**
     * {@code PUT  /marcas/:id} : Updates an existing marca.
     *
     * @param id the id of the marca to save.
     * @param marca the marca to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated marca,
     * or with status {@code 400 (Bad Request)} if the marca is not valid,
     * or with status {@code 500 (Internal Server Error)} if the marca couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Marca> updateMarca(@PathVariable(value = "id", required = false) final String id, @RequestBody Marca marca)
        throws URISyntaxException {
        LOG.debug("REST request to update Marca : {}, {}", id, marca);
        if (marca.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, marca.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!marcaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        marca = marcaService.update(marca);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, marca.getId()))
            .body(marca);
    }

    /**
     * {@code PATCH  /marcas/:id} : Partial updates given fields of an existing marca, field will ignore if it is null
     *
     * @param id the id of the marca to save.
     * @param marca the marca to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated marca,
     * or with status {@code 400 (Bad Request)} if the marca is not valid,
     * or with status {@code 404 (Not Found)} if the marca is not found,
     * or with status {@code 500 (Internal Server Error)} if the marca couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Marca> partialUpdateMarca(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody Marca marca
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Marca partially : {}, {}", id, marca);
        if (marca.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, marca.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!marcaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Marca> result = marcaService.partialUpdate(marca);

        return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, marca.getId()));
    }

    /**
     * {@code GET  /marcas} : get all the marcas.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of marcas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Marca>> getAllMarcas(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Marcas");
        Page<Marca> page = marcaService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /marcas/:id} : get the "id" marca.
     *
     * @param id the id of the marca to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the marca, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Marca> getMarca(@PathVariable("id") String id) {
        LOG.debug("REST request to get Marca : {}", id);
        Optional<Marca> marca = marcaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(marca);
    }

    /**
     * {@code DELETE  /marcas/:id} : delete the "id" marca.
     *
     * @param id the id of the marca to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMarca(@PathVariable("id") String id) {
        LOG.debug("REST request to delete Marca : {}", id);
        marcaService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }
}
