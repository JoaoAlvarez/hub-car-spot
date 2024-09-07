package com.hubcarspot.api.web.rest;

import com.hubcarspot.api.domain.Filial;
import com.hubcarspot.api.repository.FilialRepository;
import com.hubcarspot.api.service.FilialService;
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
 * REST controller for managing {@link com.hubcarspot.api.domain.Filial}.
 */
@RestController
@RequestMapping("/api/filials")
public class FilialResource {

    private static final Logger LOG = LoggerFactory.getLogger(FilialResource.class);

    private static final String ENTITY_NAME = "filial";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FilialService filialService;

    private final FilialRepository filialRepository;

    public FilialResource(FilialService filialService, FilialRepository filialRepository) {
        this.filialService = filialService;
        this.filialRepository = filialRepository;
    }

    /**
     * {@code POST  /filials} : Create a new filial.
     *
     * @param filial the filial to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new filial, or with status {@code 400 (Bad Request)} if the filial has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Filial> createFilial(@Valid @RequestBody Filial filial) throws URISyntaxException {
        LOG.debug("REST request to save Filial : {}", filial);
        if (filial.getId() != null) {
            throw new BadRequestAlertException("A new filial cannot already have an ID", ENTITY_NAME, "idexists");
        }
        filial = filialService.save(filial);
        return ResponseEntity.created(new URI("/api/filials/" + filial.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, filial.getId()))
            .body(filial);
    }

    /**
     * {@code PUT  /filials/:id} : Updates an existing filial.
     *
     * @param id the id of the filial to save.
     * @param filial the filial to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated filial,
     * or with status {@code 400 (Bad Request)} if the filial is not valid,
     * or with status {@code 500 (Internal Server Error)} if the filial couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Filial> updateFilial(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody Filial filial
    ) throws URISyntaxException {
        LOG.debug("REST request to update Filial : {}, {}", id, filial);
        if (filial.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, filial.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!filialRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        filial = filialService.update(filial);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, filial.getId()))
            .body(filial);
    }

    /**
     * {@code PATCH  /filials/:id} : Partial updates given fields of an existing filial, field will ignore if it is null
     *
     * @param id the id of the filial to save.
     * @param filial the filial to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated filial,
     * or with status {@code 400 (Bad Request)} if the filial is not valid,
     * or with status {@code 404 (Not Found)} if the filial is not found,
     * or with status {@code 500 (Internal Server Error)} if the filial couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Filial> partialUpdateFilial(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody Filial filial
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Filial partially : {}, {}", id, filial);
        if (filial.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, filial.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!filialRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Filial> result = filialService.partialUpdate(filial);

        return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, filial.getId()));
    }

    /**
     * {@code GET  /filials} : get all the filials.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of filials in body.
     */
    @GetMapping("")
    public List<Filial> getAllFilials(@RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload) {
        LOG.debug("REST request to get all Filials");
        return filialService.findAll();
    }

    /**
     * {@code GET  /filials/:id} : get the "id" filial.
     *
     * @param id the id of the filial to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the filial, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Filial> getFilial(@PathVariable("id") String id) {
        LOG.debug("REST request to get Filial : {}", id);
        Optional<Filial> filial = filialService.findOne(id);
        return ResponseUtil.wrapOrNotFound(filial);
    }

    /**
     * {@code DELETE  /filials/:id} : delete the "id" filial.
     *
     * @param id the id of the filial to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFilial(@PathVariable("id") String id) {
        LOG.debug("REST request to delete Filial : {}", id);
        filialService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }
}
