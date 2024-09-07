package com.hubcarspot.api.service;

import com.hubcarspot.api.domain.Marca;
import com.hubcarspot.api.repository.MarcaRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link com.hubcarspot.api.domain.Marca}.
 */
@Service
public class MarcaService {

    private static final Logger LOG = LoggerFactory.getLogger(MarcaService.class);

    private final MarcaRepository marcaRepository;

    public MarcaService(MarcaRepository marcaRepository) {
        this.marcaRepository = marcaRepository;
    }

    /**
     * Save a marca.
     *
     * @param marca the entity to save.
     * @return the persisted entity.
     */
    public Marca save(Marca marca) {
        LOG.debug("Request to save Marca : {}", marca);
        return marcaRepository.save(marca);
    }

    /**
     * Update a marca.
     *
     * @param marca the entity to save.
     * @return the persisted entity.
     */
    public Marca update(Marca marca) {
        LOG.debug("Request to update Marca : {}", marca);
        return marcaRepository.save(marca);
    }

    /**
     * Partially update a marca.
     *
     * @param marca the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Marca> partialUpdate(Marca marca) {
        LOG.debug("Request to partially update Marca : {}", marca);

        return marcaRepository
            .findById(marca.getId())
            .map(existingMarca -> {
                if (marca.getNome() != null) {
                    existingMarca.setNome(marca.getNome());
                }

                return existingMarca;
            })
            .map(marcaRepository::save);
    }

    /**
     * Get all the marcas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Page<Marca> findAll(Pageable pageable) {
        LOG.debug("Request to get all Marcas");
        return marcaRepository.findAll(pageable);
    }

    /**
     * Get one marca by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<Marca> findOne(String id) {
        LOG.debug("Request to get Marca : {}", id);
        return marcaRepository.findById(id);
    }

    /**
     * Delete the marca by id.
     *
     * @param id the id of the entity.
     */
    public void delete(String id) {
        LOG.debug("Request to delete Marca : {}", id);
        marcaRepository.deleteById(id);
    }
}
