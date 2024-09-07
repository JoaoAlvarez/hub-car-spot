package com.hubcarspot.api.service;

import com.hubcarspot.api.domain.Taxas;
import com.hubcarspot.api.repository.TaxasRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link com.hubcarspot.api.domain.Taxas}.
 */
@Service
public class TaxasService {

    private static final Logger LOG = LoggerFactory.getLogger(TaxasService.class);

    private final TaxasRepository taxasRepository;

    public TaxasService(TaxasRepository taxasRepository) {
        this.taxasRepository = taxasRepository;
    }

    /**
     * Save a taxas.
     *
     * @param taxas the entity to save.
     * @return the persisted entity.
     */
    public Taxas save(Taxas taxas) {
        LOG.debug("Request to save Taxas : {}", taxas);
        return taxasRepository.save(taxas);
    }

    /**
     * Update a taxas.
     *
     * @param taxas the entity to save.
     * @return the persisted entity.
     */
    public Taxas update(Taxas taxas) {
        LOG.debug("Request to update Taxas : {}", taxas);
        return taxasRepository.save(taxas);
    }

    /**
     * Partially update a taxas.
     *
     * @param taxas the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Taxas> partialUpdate(Taxas taxas) {
        LOG.debug("Request to partially update Taxas : {}", taxas);

        return taxasRepository
            .findById(taxas.getId())
            .map(existingTaxas -> {
                if (taxas.getNome() != null) {
                    existingTaxas.setNome(taxas.getNome());
                }
                if (taxas.getValor() != null) {
                    existingTaxas.setValor(taxas.getValor());
                }

                return existingTaxas;
            })
            .map(taxasRepository::save);
    }

    /**
     * Get all the taxas.
     *
     * @return the list of entities.
     */
    public List<Taxas> findAll() {
        LOG.debug("Request to get all Taxas");
        return taxasRepository.findAll();
    }

    /**
     * Get all the taxas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Taxas> findAllWithEagerRelationships(Pageable pageable) {
        return taxasRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one taxas by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<Taxas> findOne(String id) {
        LOG.debug("Request to get Taxas : {}", id);
        return taxasRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the taxas by id.
     *
     * @param id the id of the entity.
     */
    public void delete(String id) {
        LOG.debug("Request to delete Taxas : {}", id);
        taxasRepository.deleteById(id);
    }
}
