package com.hubcarspot.api.service;

import com.hubcarspot.api.domain.TrocaVeiculo;
import com.hubcarspot.api.repository.TrocaVeiculoRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link com.hubcarspot.api.domain.TrocaVeiculo}.
 */
@Service
public class TrocaVeiculoService {

    private static final Logger LOG = LoggerFactory.getLogger(TrocaVeiculoService.class);

    private final TrocaVeiculoRepository trocaVeiculoRepository;

    public TrocaVeiculoService(TrocaVeiculoRepository trocaVeiculoRepository) {
        this.trocaVeiculoRepository = trocaVeiculoRepository;
    }

    /**
     * Save a trocaVeiculo.
     *
     * @param trocaVeiculo the entity to save.
     * @return the persisted entity.
     */
    public TrocaVeiculo save(TrocaVeiculo trocaVeiculo) {
        LOG.debug("Request to save TrocaVeiculo : {}", trocaVeiculo);
        return trocaVeiculoRepository.save(trocaVeiculo);
    }

    /**
     * Update a trocaVeiculo.
     *
     * @param trocaVeiculo the entity to save.
     * @return the persisted entity.
     */
    public TrocaVeiculo update(TrocaVeiculo trocaVeiculo) {
        LOG.debug("Request to update TrocaVeiculo : {}", trocaVeiculo);
        return trocaVeiculoRepository.save(trocaVeiculo);
    }

    /**
     * Partially update a trocaVeiculo.
     *
     * @param trocaVeiculo the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TrocaVeiculo> partialUpdate(TrocaVeiculo trocaVeiculo) {
        LOG.debug("Request to partially update TrocaVeiculo : {}", trocaVeiculo);

        return trocaVeiculoRepository
            .findById(trocaVeiculo.getId())
            .map(existingTrocaVeiculo -> {
                if (trocaVeiculo.getCarroEntradaId() != null) {
                    existingTrocaVeiculo.setCarroEntradaId(trocaVeiculo.getCarroEntradaId());
                }
                if (trocaVeiculo.getCarroSaidaId() != null) {
                    existingTrocaVeiculo.setCarroSaidaId(trocaVeiculo.getCarroSaidaId());
                }
                if (trocaVeiculo.getDataTroca() != null) {
                    existingTrocaVeiculo.setDataTroca(trocaVeiculo.getDataTroca());
                }
                if (trocaVeiculo.getCondicaoPagamento() != null) {
                    existingTrocaVeiculo.setCondicaoPagamento(trocaVeiculo.getCondicaoPagamento());
                }
                if (trocaVeiculo.getValorPago() != null) {
                    existingTrocaVeiculo.setValorPago(trocaVeiculo.getValorPago());
                }
                if (trocaVeiculo.getValorRecebido() != null) {
                    existingTrocaVeiculo.setValorRecebido(trocaVeiculo.getValorRecebido());
                }

                return existingTrocaVeiculo;
            })
            .map(trocaVeiculoRepository::save);
    }

    /**
     * Get all the trocaVeiculos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Page<TrocaVeiculo> findAll(Pageable pageable) {
        LOG.debug("Request to get all TrocaVeiculos");
        return trocaVeiculoRepository.findAll(pageable);
    }

    /**
     * Get all the trocaVeiculos with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<TrocaVeiculo> findAllWithEagerRelationships(Pageable pageable) {
        return trocaVeiculoRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one trocaVeiculo by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<TrocaVeiculo> findOne(String id) {
        LOG.debug("Request to get TrocaVeiculo : {}", id);
        return trocaVeiculoRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the trocaVeiculo by id.
     *
     * @param id the id of the entity.
     */
    public void delete(String id) {
        LOG.debug("Request to delete TrocaVeiculo : {}", id);
        trocaVeiculoRepository.deleteById(id);
    }
}
