package com.hubcarspot.api.web.rest;

import com.hubcarspot.api.domain.StatusDocumento;
import com.hubcarspot.api.repository.StatusDocumentoRepository;
import com.hubcarspot.api.service.StatusDocumentoService;
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
 * REST controller for managing {@link com.hubcarspot.api.domain.StatusDocumento}.
 */
@RestController
@RequestMapping("/api/status-documentos")
public class StatusDocumentoResource {

    private static final Logger LOG = LoggerFactory.getLogger(StatusDocumentoResource.class);

    private static final String ENTITY_NAME = "statusDocumento";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StatusDocumentoService statusDocumentoService;

    private final StatusDocumentoRepository statusDocumentoRepository;

    public StatusDocumentoResource(StatusDocumentoService statusDocumentoService, StatusDocumentoRepository statusDocumentoRepository) {
        this.statusDocumentoService = statusDocumentoService;
        this.statusDocumentoRepository = statusDocumentoRepository;
    }

    /**
     * {@code POST  /status-documentos} : Create a new statusDocumento.
     *
     * @param statusDocumento the statusDocumento to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new statusDocumento, or with status {@code 400 (Bad Request)} if the statusDocumento has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<StatusDocumento> createStatusDocumento(@Valid @RequestBody StatusDocumento statusDocumento)
        throws URISyntaxException {
        LOG.debug("REST request to save StatusDocumento : {}", statusDocumento);
        if (statusDocumento.getId() != null) {
            throw new BadRequestAlertException("A new statusDocumento cannot already have an ID", ENTITY_NAME, "idexists");
        }
        statusDocumento = statusDocumentoService.save(statusDocumento);
        return ResponseEntity.created(new URI("/api/status-documentos/" + statusDocumento.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, statusDocumento.getId()))
            .body(statusDocumento);
    }

    /**
     * {@code PUT  /status-documentos/:id} : Updates an existing statusDocumento.
     *
     * @param id the id of the statusDocumento to save.
     * @param statusDocumento the statusDocumento to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated statusDocumento,
     * or with status {@code 400 (Bad Request)} if the statusDocumento is not valid,
     * or with status {@code 500 (Internal Server Error)} if the statusDocumento couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<StatusDocumento> updateStatusDocumento(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody StatusDocumento statusDocumento
    ) throws URISyntaxException {
        LOG.debug("REST request to update StatusDocumento : {}, {}", id, statusDocumento);
        if (statusDocumento.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, statusDocumento.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!statusDocumentoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        statusDocumento = statusDocumentoService.update(statusDocumento);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, statusDocumento.getId()))
            .body(statusDocumento);
    }

    /**
     * {@code PATCH  /status-documentos/:id} : Partial updates given fields of an existing statusDocumento, field will ignore if it is null
     *
     * @param id the id of the statusDocumento to save.
     * @param statusDocumento the statusDocumento to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated statusDocumento,
     * or with status {@code 400 (Bad Request)} if the statusDocumento is not valid,
     * or with status {@code 404 (Not Found)} if the statusDocumento is not found,
     * or with status {@code 500 (Internal Server Error)} if the statusDocumento couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<StatusDocumento> partialUpdateStatusDocumento(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody StatusDocumento statusDocumento
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update StatusDocumento partially : {}, {}", id, statusDocumento);
        if (statusDocumento.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, statusDocumento.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!statusDocumentoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<StatusDocumento> result = statusDocumentoService.partialUpdate(statusDocumento);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, statusDocumento.getId())
        );
    }

    /**
     * {@code GET  /status-documentos} : get all the statusDocumentos.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of statusDocumentos in body.
     */
    @GetMapping("")
    public List<StatusDocumento> getAllStatusDocumentos(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get all StatusDocumentos");
        return statusDocumentoService.findAll();
    }

    /**
     * {@code GET  /status-documentos/:id} : get the "id" statusDocumento.
     *
     * @param id the id of the statusDocumento to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the statusDocumento, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<StatusDocumento> getStatusDocumento(@PathVariable("id") String id) {
        LOG.debug("REST request to get StatusDocumento : {}", id);
        Optional<StatusDocumento> statusDocumento = statusDocumentoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(statusDocumento);
    }

    /**
     * {@code DELETE  /status-documentos/:id} : delete the "id" statusDocumento.
     *
     * @param id the id of the statusDocumento to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStatusDocumento(@PathVariable("id") String id) {
        LOG.debug("REST request to delete StatusDocumento : {}", id);
        statusDocumentoService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }
}
