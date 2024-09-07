package com.hubcarspot.api.service;

import com.hubcarspot.api.domain.StatusDocumento;
import com.hubcarspot.api.repository.StatusDocumentoRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link com.hubcarspot.api.domain.StatusDocumento}.
 */
@Service
public class StatusDocumentoService {

    private static final Logger LOG = LoggerFactory.getLogger(StatusDocumentoService.class);

    private final StatusDocumentoRepository statusDocumentoRepository;

    public StatusDocumentoService(StatusDocumentoRepository statusDocumentoRepository) {
        this.statusDocumentoRepository = statusDocumentoRepository;
    }

    /**
     * Save a statusDocumento.
     *
     * @param statusDocumento the entity to save.
     * @return the persisted entity.
     */
    public StatusDocumento save(StatusDocumento statusDocumento) {
        LOG.debug("Request to save StatusDocumento : {}", statusDocumento);
        return statusDocumentoRepository.save(statusDocumento);
    }

    /**
     * Update a statusDocumento.
     *
     * @param statusDocumento the entity to save.
     * @return the persisted entity.
     */
    public StatusDocumento update(StatusDocumento statusDocumento) {
        LOG.debug("Request to update StatusDocumento : {}", statusDocumento);
        return statusDocumentoRepository.save(statusDocumento);
    }

    /**
     * Partially update a statusDocumento.
     *
     * @param statusDocumento the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<StatusDocumento> partialUpdate(StatusDocumento statusDocumento) {
        LOG.debug("Request to partially update StatusDocumento : {}", statusDocumento);

        return statusDocumentoRepository
            .findById(statusDocumento.getId())
            .map(existingStatusDocumento -> {
                if (statusDocumento.getInstituicaoId() != null) {
                    existingStatusDocumento.setInstituicaoId(statusDocumento.getInstituicaoId());
                }
                if (statusDocumento.getNome() != null) {
                    existingStatusDocumento.setNome(statusDocumento.getNome());
                }

                return existingStatusDocumento;
            })
            .map(statusDocumentoRepository::save);
    }

    /**
     * Get all the statusDocumentos.
     *
     * @return the list of entities.
     */
    public List<StatusDocumento> findAll() {
        LOG.debug("Request to get all StatusDocumentos");
        return statusDocumentoRepository.findAll();
    }

    /**
     * Get all the statusDocumentos with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<StatusDocumento> findAllWithEagerRelationships(Pageable pageable) {
        return statusDocumentoRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one statusDocumento by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<StatusDocumento> findOne(String id) {
        LOG.debug("Request to get StatusDocumento : {}", id);
        return statusDocumentoRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the statusDocumento by id.
     *
     * @param id the id of the entity.
     */
    public void delete(String id) {
        LOG.debug("Request to delete StatusDocumento : {}", id);
        statusDocumentoRepository.deleteById(id);
    }
}
