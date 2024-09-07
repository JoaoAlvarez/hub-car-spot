package com.hubcarspot.api.service;

import com.hubcarspot.api.domain.Financeira;
import com.hubcarspot.api.repository.FinanceiraRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link com.hubcarspot.api.domain.Financeira}.
 */
@Service
public class FinanceiraService {

    private static final Logger LOG = LoggerFactory.getLogger(FinanceiraService.class);

    private final FinanceiraRepository financeiraRepository;

    public FinanceiraService(FinanceiraRepository financeiraRepository) {
        this.financeiraRepository = financeiraRepository;
    }

    /**
     * Save a financeira.
     *
     * @param financeira the entity to save.
     * @return the persisted entity.
     */
    public Financeira save(Financeira financeira) {
        LOG.debug("Request to save Financeira : {}", financeira);
        return financeiraRepository.save(financeira);
    }

    /**
     * Update a financeira.
     *
     * @param financeira the entity to save.
     * @return the persisted entity.
     */
    public Financeira update(Financeira financeira) {
        LOG.debug("Request to update Financeira : {}", financeira);
        return financeiraRepository.save(financeira);
    }

    /**
     * Partially update a financeira.
     *
     * @param financeira the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Financeira> partialUpdate(Financeira financeira) {
        LOG.debug("Request to partially update Financeira : {}", financeira);

        return financeiraRepository
            .findById(financeira.getId())
            .map(existingFinanceira -> {
                if (financeira.getNome() != null) {
                    existingFinanceira.setNome(financeira.getNome());
                }
                if (financeira.getTelefone() != null) {
                    existingFinanceira.setTelefone(financeira.getTelefone());
                }
                if (financeira.getCnpj() != null) {
                    existingFinanceira.setCnpj(financeira.getCnpj());
                }
                if (financeira.getCep() != null) {
                    existingFinanceira.setCep(financeira.getCep());
                }
                if (financeira.getEndereco() != null) {
                    existingFinanceira.setEndereco(financeira.getEndereco());
                }
                if (financeira.getBairro() != null) {
                    existingFinanceira.setBairro(financeira.getBairro());
                }
                if (financeira.getCidade() != null) {
                    existingFinanceira.setCidade(financeira.getCidade());
                }
                if (financeira.getNumero() != null) {
                    existingFinanceira.setNumero(financeira.getNumero());
                }
                if (financeira.getUf() != null) {
                    existingFinanceira.setUf(financeira.getUf());
                }

                return existingFinanceira;
            })
            .map(financeiraRepository::save);
    }

    /**
     * Get all the financeiras.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Page<Financeira> findAll(Pageable pageable) {
        LOG.debug("Request to get all Financeiras");
        return financeiraRepository.findAll(pageable);
    }

    /**
     * Get all the financeiras with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Financeira> findAllWithEagerRelationships(Pageable pageable) {
        return financeiraRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one financeira by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<Financeira> findOne(String id) {
        LOG.debug("Request to get Financeira : {}", id);
        return financeiraRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the financeira by id.
     *
     * @param id the id of the entity.
     */
    public void delete(String id) {
        LOG.debug("Request to delete Financeira : {}", id);
        financeiraRepository.deleteById(id);
    }
}
