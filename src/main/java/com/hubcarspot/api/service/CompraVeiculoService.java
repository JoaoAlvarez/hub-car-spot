package com.hubcarspot.api.service;

import com.hubcarspot.api.domain.CompraVeiculo;
import com.hubcarspot.api.repository.CompraVeiculoRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link com.hubcarspot.api.domain.CompraVeiculo}.
 */
@Service
public class CompraVeiculoService {

    private static final Logger LOG = LoggerFactory.getLogger(CompraVeiculoService.class);

    private final CompraVeiculoRepository compraVeiculoRepository;

    public CompraVeiculoService(CompraVeiculoRepository compraVeiculoRepository) {
        this.compraVeiculoRepository = compraVeiculoRepository;
    }

    /**
     * Save a compraVeiculo.
     *
     * @param compraVeiculo the entity to save.
     * @return the persisted entity.
     */
    public CompraVeiculo save(CompraVeiculo compraVeiculo) {
        LOG.debug("Request to save CompraVeiculo : {}", compraVeiculo);
        return compraVeiculoRepository.save(compraVeiculo);
    }

    /**
     * Update a compraVeiculo.
     *
     * @param compraVeiculo the entity to save.
     * @return the persisted entity.
     */
    public CompraVeiculo update(CompraVeiculo compraVeiculo) {
        LOG.debug("Request to update CompraVeiculo : {}", compraVeiculo);
        return compraVeiculoRepository.save(compraVeiculo);
    }

    /**
     * Partially update a compraVeiculo.
     *
     * @param compraVeiculo the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CompraVeiculo> partialUpdate(CompraVeiculo compraVeiculo) {
        LOG.debug("Request to partially update CompraVeiculo : {}", compraVeiculo);

        return compraVeiculoRepository
            .findById(compraVeiculo.getId())
            .map(existingCompraVeiculo -> {
                if (compraVeiculo.getKmEntrada() != null) {
                    existingCompraVeiculo.setKmEntrada(compraVeiculo.getKmEntrada());
                }
                if (compraVeiculo.getValor() != null) {
                    existingCompraVeiculo.setValor(compraVeiculo.getValor());
                }
                if (compraVeiculo.getValorEstimado() != null) {
                    existingCompraVeiculo.setValorEstimado(compraVeiculo.getValorEstimado());
                }
                if (compraVeiculo.getEnderecoCrlv() != null) {
                    existingCompraVeiculo.setEnderecoCrlv(compraVeiculo.getEnderecoCrlv());
                }
                if (compraVeiculo.getCidadeCrlv() != null) {
                    existingCompraVeiculo.setCidadeCrlv(compraVeiculo.getCidadeCrlv());
                }
                if (compraVeiculo.getUfCrlv() != null) {
                    existingCompraVeiculo.setUfCrlv(compraVeiculo.getUfCrlv());
                }
                if (compraVeiculo.getCpfCrlv() != null) {
                    existingCompraVeiculo.setCpfCrlv(compraVeiculo.getCpfCrlv());
                }
                if (compraVeiculo.getDataCompra() != null) {
                    existingCompraVeiculo.setDataCompra(compraVeiculo.getDataCompra());
                }
                if (compraVeiculo.getCondicaoPagamento() != null) {
                    existingCompraVeiculo.setCondicaoPagamento(compraVeiculo.getCondicaoPagamento());
                }
                if (compraVeiculo.getValorPago() != null) {
                    existingCompraVeiculo.setValorPago(compraVeiculo.getValorPago());
                }

                return existingCompraVeiculo;
            })
            .map(compraVeiculoRepository::save);
    }

    /**
     * Get all the compraVeiculos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Page<CompraVeiculo> findAll(Pageable pageable) {
        LOG.debug("Request to get all CompraVeiculos");
        return compraVeiculoRepository.findAll(pageable);
    }

    /**
     * Get all the compraVeiculos with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<CompraVeiculo> findAllWithEagerRelationships(Pageable pageable) {
        return compraVeiculoRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one compraVeiculo by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<CompraVeiculo> findOne(String id) {
        LOG.debug("Request to get CompraVeiculo : {}", id);
        return compraVeiculoRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the compraVeiculo by id.
     *
     * @param id the id of the entity.
     */
    public void delete(String id) {
        LOG.debug("Request to delete CompraVeiculo : {}", id);
        compraVeiculoRepository.deleteById(id);
    }
}
