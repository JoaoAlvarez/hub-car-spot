package com.hubcarspot.api.service;

import com.hubcarspot.api.domain.Instituicao;
import com.hubcarspot.api.repository.InstituicaoRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link com.hubcarspot.api.domain.Instituicao}.
 */
@Service
public class InstituicaoService {

    private static final Logger LOG = LoggerFactory.getLogger(InstituicaoService.class);

    private final InstituicaoRepository instituicaoRepository;

    public InstituicaoService(InstituicaoRepository instituicaoRepository) {
        this.instituicaoRepository = instituicaoRepository;
    }

    /**
     * Save a instituicao.
     *
     * @param instituicao the entity to save.
     * @return the persisted entity.
     */
    public Instituicao save(Instituicao instituicao) {
        LOG.debug("Request to save Instituicao : {}", instituicao);
        return instituicaoRepository.save(instituicao);
    }

    /**
     * Update a instituicao.
     *
     * @param instituicao the entity to save.
     * @return the persisted entity.
     */
    public Instituicao update(Instituicao instituicao) {
        LOG.debug("Request to update Instituicao : {}", instituicao);
        return instituicaoRepository.save(instituicao);
    }

    /**
     * Partially update a instituicao.
     *
     * @param instituicao the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Instituicao> partialUpdate(Instituicao instituicao) {
        LOG.debug("Request to partially update Instituicao : {}", instituicao);

        return instituicaoRepository
            .findById(instituicao.getId())
            .map(existingInstituicao -> {
                if (instituicao.getNome() != null) {
                    existingInstituicao.setNome(instituicao.getNome());
                }
                if (instituicao.getTelefone() != null) {
                    existingInstituicao.setTelefone(instituicao.getTelefone());
                }
                if (instituicao.getCnpj() != null) {
                    existingInstituicao.setCnpj(instituicao.getCnpj());
                }
                if (instituicao.getCep() != null) {
                    existingInstituicao.setCep(instituicao.getCep());
                }
                if (instituicao.getEndereco() != null) {
                    existingInstituicao.setEndereco(instituicao.getEndereco());
                }
                if (instituicao.getBairro() != null) {
                    existingInstituicao.setBairro(instituicao.getBairro());
                }
                if (instituicao.getCidade() != null) {
                    existingInstituicao.setCidade(instituicao.getCidade());
                }
                if (instituicao.getNumero() != null) {
                    existingInstituicao.setNumero(instituicao.getNumero());
                }
                if (instituicao.getUf() != null) {
                    existingInstituicao.setUf(instituicao.getUf());
                }
                if (instituicao.getComplemento() != null) {
                    existingInstituicao.setComplemento(instituicao.getComplemento());
                }

                return existingInstituicao;
            })
            .map(instituicaoRepository::save);
    }

    /**
     * Get all the instituicaos.
     *
     * @return the list of entities.
     */
    public List<Instituicao> findAll() {
        LOG.debug("Request to get all Instituicaos");
        return instituicaoRepository.findAll();
    }

    /**
     * Get one instituicao by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<Instituicao> findOne(String id) {
        LOG.debug("Request to get Instituicao : {}", id);
        return instituicaoRepository.findById(id);
    }

    /**
     * Delete the instituicao by id.
     *
     * @param id the id of the entity.
     */
    public void delete(String id) {
        LOG.debug("Request to delete Instituicao : {}", id);
        instituicaoRepository.deleteById(id);
    }
}
