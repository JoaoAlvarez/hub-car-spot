package com.hubcarspot.api.service;

import com.hubcarspot.api.domain.Combustivel;
import com.hubcarspot.api.repository.CombustivelRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link com.hubcarspot.api.domain.Combustivel}.
 */
@Service
public class CombustivelService {

    private static final Logger LOG = LoggerFactory.getLogger(CombustivelService.class);

    private final CombustivelRepository combustivelRepository;

    public CombustivelService(CombustivelRepository combustivelRepository) {
        this.combustivelRepository = combustivelRepository;
    }

    /**
     * Save a combustivel.
     *
     * @param combustivel the entity to save.
     * @return the persisted entity.
     */
    public Combustivel save(Combustivel combustivel) {
        LOG.debug("Request to save Combustivel : {}", combustivel);
        return combustivelRepository.save(combustivel);
    }

    /**
     * Update a combustivel.
     *
     * @param combustivel the entity to save.
     * @return the persisted entity.
     */
    public Combustivel update(Combustivel combustivel) {
        LOG.debug("Request to update Combustivel : {}", combustivel);
        return combustivelRepository.save(combustivel);
    }

    /**
     * Partially update a combustivel.
     *
     * @param combustivel the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Combustivel> partialUpdate(Combustivel combustivel) {
        LOG.debug("Request to partially update Combustivel : {}", combustivel);

        return combustivelRepository
            .findById(combustivel.getId())
            .map(existingCombustivel -> {
                if (combustivel.getNome() != null) {
                    existingCombustivel.setNome(combustivel.getNome());
                }

                return existingCombustivel;
            })
            .map(combustivelRepository::save);
    }

    /**
     * Get all the combustivels.
     *
     * @return the list of entities.
     */
    public List<Combustivel> findAll() {
        LOG.debug("Request to get all Combustivels");
        return combustivelRepository.findAll();
    }

    /**
     * Get one combustivel by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<Combustivel> findOne(String id) {
        LOG.debug("Request to get Combustivel : {}", id);
        return combustivelRepository.findById(id);
    }

    /**
     * Delete the combustivel by id.
     *
     * @param id the id of the entity.
     */
    public void delete(String id) {
        LOG.debug("Request to delete Combustivel : {}", id);
        combustivelRepository.deleteById(id);
    }
}
