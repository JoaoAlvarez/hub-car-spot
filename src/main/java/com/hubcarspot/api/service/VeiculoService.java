package com.hubcarspot.api.service;

import com.hubcarspot.api.domain.Veiculo;
import com.hubcarspot.api.repository.VeiculoRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link com.hubcarspot.api.domain.Veiculo}.
 */
@Service
public class VeiculoService {

    private static final Logger LOG = LoggerFactory.getLogger(VeiculoService.class);

    private final VeiculoRepository veiculoRepository;

    public VeiculoService(VeiculoRepository veiculoRepository) {
        this.veiculoRepository = veiculoRepository;
    }

    /**
     * Save a veiculo.
     *
     * @param veiculo the entity to save.
     * @return the persisted entity.
     */
    public Veiculo save(Veiculo veiculo) {
        LOG.debug("Request to save Veiculo : {}", veiculo);
        return veiculoRepository.save(veiculo);
    }

    /**
     * Update a veiculo.
     *
     * @param veiculo the entity to save.
     * @return the persisted entity.
     */
    public Veiculo update(Veiculo veiculo) {
        LOG.debug("Request to update Veiculo : {}", veiculo);
        return veiculoRepository.save(veiculo);
    }

    /**
     * Partially update a veiculo.
     *
     * @param veiculo the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Veiculo> partialUpdate(Veiculo veiculo) {
        LOG.debug("Request to partially update Veiculo : {}", veiculo);

        return veiculoRepository
            .findById(veiculo.getId())
            .map(existingVeiculo -> {
                if (veiculo.getCreatedAt() != null) {
                    existingVeiculo.setCreatedAt(veiculo.getCreatedAt());
                }
                if (veiculo.getEspecie() != null) {
                    existingVeiculo.setEspecie(veiculo.getEspecie());
                }
                if (veiculo.getPlaca() != null) {
                    existingVeiculo.setPlaca(veiculo.getPlaca());
                }
                if (veiculo.getMarca() != null) {
                    existingVeiculo.setMarca(veiculo.getMarca());
                }
                if (veiculo.getModelo() != null) {
                    existingVeiculo.setModelo(veiculo.getModelo());
                }
                if (veiculo.getAnoFabricacao() != null) {
                    existingVeiculo.setAnoFabricacao(veiculo.getAnoFabricacao());
                }
                if (veiculo.getAnoModelo() != null) {
                    existingVeiculo.setAnoModelo(veiculo.getAnoModelo());
                }
                if (veiculo.getCor() != null) {
                    existingVeiculo.setCor(veiculo.getCor());
                }
                if (veiculo.getCombustivel() != null) {
                    existingVeiculo.setCombustivel(veiculo.getCombustivel());
                }
                if (veiculo.getCambio() != null) {
                    existingVeiculo.setCambio(veiculo.getCambio());
                }
                if (veiculo.getStatus() != null) {
                    existingVeiculo.setStatus(veiculo.getStatus());
                }
                if (veiculo.getChassi() != null) {
                    existingVeiculo.setChassi(veiculo.getChassi());
                }
                if (veiculo.getRenavam() != null) {
                    existingVeiculo.setRenavam(veiculo.getRenavam());
                }
                if (veiculo.getNumeroMotor() != null) {
                    existingVeiculo.setNumeroMotor(veiculo.getNumeroMotor());
                }
                if (veiculo.getNumeroCambio() != null) {
                    existingVeiculo.setNumeroCambio(veiculo.getNumeroCambio());
                }
                if (veiculo.getQuilometraegem() != null) {
                    existingVeiculo.setQuilometraegem(veiculo.getQuilometraegem());
                }
                if (veiculo.getKmSaida() != null) {
                    existingVeiculo.setKmSaida(veiculo.getKmSaida());
                }
                if (veiculo.getCavalos() != null) {
                    existingVeiculo.setCavalos(veiculo.getCavalos());
                }
                if (veiculo.getMotorizacao() != null) {
                    existingVeiculo.setMotorizacao(veiculo.getMotorizacao());
                }
                if (veiculo.getAdicional() != null) {
                    existingVeiculo.setAdicional(veiculo.getAdicional());
                }
                if (veiculo.getDescritivoCurtoAcessorios() != null) {
                    existingVeiculo.setDescritivoCurtoAcessorios(veiculo.getDescritivoCurtoAcessorios());
                }

                return existingVeiculo;
            })
            .map(veiculoRepository::save);
    }

    /**
     * Get all the veiculos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Page<Veiculo> findAll(Pageable pageable) {
        LOG.debug("Request to get all Veiculos");
        return veiculoRepository.findAll(pageable);
    }

    /**
     * Get one veiculo by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<Veiculo> findOne(String id) {
        LOG.debug("Request to get Veiculo : {}", id);
        return veiculoRepository.findById(id);
    }

    /**
     * Delete the veiculo by id.
     *
     * @param id the id of the entity.
     */
    public void delete(String id) {
        LOG.debug("Request to delete Veiculo : {}", id);
        veiculoRepository.deleteById(id);
    }
}
