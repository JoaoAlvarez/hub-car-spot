package com.hubcarspot.api.service;

import com.hubcarspot.api.domain.VendaVeiculo;
import com.hubcarspot.api.repository.VendaVeiculoRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link com.hubcarspot.api.domain.VendaVeiculo}.
 */
@Service
public class VendaVeiculoService {

    private static final Logger LOG = LoggerFactory.getLogger(VendaVeiculoService.class);

    private final VendaVeiculoRepository vendaVeiculoRepository;

    public VendaVeiculoService(VendaVeiculoRepository vendaVeiculoRepository) {
        this.vendaVeiculoRepository = vendaVeiculoRepository;
    }

    /**
     * Save a vendaVeiculo.
     *
     * @param vendaVeiculo the entity to save.
     * @return the persisted entity.
     */
    public VendaVeiculo save(VendaVeiculo vendaVeiculo) {
        LOG.debug("Request to save VendaVeiculo : {}", vendaVeiculo);
        return vendaVeiculoRepository.save(vendaVeiculo);
    }

    /**
     * Update a vendaVeiculo.
     *
     * @param vendaVeiculo the entity to save.
     * @return the persisted entity.
     */
    public VendaVeiculo update(VendaVeiculo vendaVeiculo) {
        LOG.debug("Request to update VendaVeiculo : {}", vendaVeiculo);
        return vendaVeiculoRepository.save(vendaVeiculo);
    }

    /**
     * Partially update a vendaVeiculo.
     *
     * @param vendaVeiculo the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<VendaVeiculo> partialUpdate(VendaVeiculo vendaVeiculo) {
        LOG.debug("Request to partially update VendaVeiculo : {}", vendaVeiculo);

        return vendaVeiculoRepository
            .findById(vendaVeiculo.getId())
            .map(existingVendaVeiculo -> {
                if (vendaVeiculo.getKmSaida() != null) {
                    existingVendaVeiculo.setKmSaida(vendaVeiculo.getKmSaida());
                }
                if (vendaVeiculo.getValorCompra() != null) {
                    existingVendaVeiculo.setValorCompra(vendaVeiculo.getValorCompra());
                }
                if (vendaVeiculo.getValorTabela() != null) {
                    existingVendaVeiculo.setValorTabela(vendaVeiculo.getValorTabela());
                }
                if (vendaVeiculo.getValorVenda() != null) {
                    existingVendaVeiculo.setValorVenda(vendaVeiculo.getValorVenda());
                }
                if (vendaVeiculo.getDataVenda() != null) {
                    existingVendaVeiculo.setDataVenda(vendaVeiculo.getDataVenda());
                }
                if (vendaVeiculo.getCondicaoRecebimento() != null) {
                    existingVendaVeiculo.setCondicaoRecebimento(vendaVeiculo.getCondicaoRecebimento());
                }
                if (vendaVeiculo.getValorEntrada() != null) {
                    existingVendaVeiculo.setValorEntrada(vendaVeiculo.getValorEntrada());
                }
                if (vendaVeiculo.getValorFinanciado() != null) {
                    existingVendaVeiculo.setValorFinanciado(vendaVeiculo.getValorFinanciado());
                }

                return existingVendaVeiculo;
            })
            .map(vendaVeiculoRepository::save);
    }

    /**
     * Get all the vendaVeiculos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Page<VendaVeiculo> findAll(Pageable pageable) {
        LOG.debug("Request to get all VendaVeiculos");
        return vendaVeiculoRepository.findAll(pageable);
    }

    /**
     * Get all the vendaVeiculos with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<VendaVeiculo> findAllWithEagerRelationships(Pageable pageable) {
        return vendaVeiculoRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one vendaVeiculo by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<VendaVeiculo> findOne(String id) {
        LOG.debug("Request to get VendaVeiculo : {}", id);
        return vendaVeiculoRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the vendaVeiculo by id.
     *
     * @param id the id of the entity.
     */
    public void delete(String id) {
        LOG.debug("Request to delete VendaVeiculo : {}", id);
        vendaVeiculoRepository.deleteById(id);
    }
}
