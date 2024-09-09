package com.hubcarspot.api.service;

import com.hubcarspot.api.domain.Filial;
import com.hubcarspot.api.domain.Instituicao;
import com.hubcarspot.api.repository.FilialRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link com.hubcarspot.api.domain.Filial}.
 */
@Service
public class FilialService {

    private static final Logger LOG = LoggerFactory.getLogger(FilialService.class);

    private final FilialRepository filialRepository;
    private final UsuarioInstituicaoService usuarioInstituicaoService;

    public FilialService(FilialRepository filialRepository, UsuarioInstituicaoService usuarioInstituicaoService) {
        this.filialRepository = filialRepository;
        this.usuarioInstituicaoService = usuarioInstituicaoService;
    }

    /**
     * Save a filial.
     *
     * @param filial the entity to save.
     * @return the persisted entity.
     */
    public Filial save(Filial filial) throws Exception {
        LOG.debug("Request to save Filial : {}", filial);
        filial.setInstituicao(usuarioInstituicaoService.instituicaoDoUsuarioLogado());
        return filialRepository.save(filial);
    }

    /**
     * Update a filial.
     *
     * @param filial the entity to save.
     * @return the persisted entity.
     */
    public Filial update(Filial filial) {
        LOG.debug("Request to update Filial : {}", filial);
        return filialRepository.save(filial);
    }

    /**
     * Partially update a filial.
     *
     * @param filial the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Filial> partialUpdate(Filial filial) {
        LOG.debug("Request to partially update Filial : {}", filial);

        return filialRepository
            .findById(filial.getId())
            .map(existingFilial -> {
                if (filial.getNome() != null) {
                    existingFilial.setNome(filial.getNome());
                }
                if (filial.getTelefone() != null) {
                    existingFilial.setTelefone(filial.getTelefone());
                }
                if (filial.getCnpj() != null) {
                    existingFilial.setCnpj(filial.getCnpj());
                }
                if (filial.getCep() != null) {
                    existingFilial.setCep(filial.getCep());
                }
                if (filial.getEndereco() != null) {
                    existingFilial.setEndereco(filial.getEndereco());
                }
                if (filial.getBairro() != null) {
                    existingFilial.setBairro(filial.getBairro());
                }
                if (filial.getCidade() != null) {
                    existingFilial.setCidade(filial.getCidade());
                }
                if (filial.getNumero() != null) {
                    existingFilial.setNumero(filial.getNumero());
                }
                if (filial.getUf() != null) {
                    existingFilial.setUf(filial.getUf());
                }

                return existingFilial;
            })
            .map(filialRepository::save);
    }

    /**
     * Get all the filials.
     *
     * @return the list of entities.
     */
    public List<Filial> findAll() throws Exception {
        LOG.debug("Request to get all Filials");
        Instituicao instituicao = usuarioInstituicaoService.instituicaoDoUsuarioLogado();
        return filialRepository.findByInstituicaoId(instituicao.getId());
    }

    /**
     * Get all the filials with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Filial> findAllWithEagerRelationships(Pageable pageable) {
        return filialRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one filial by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<Filial> findOne(String id) {
        LOG.debug("Request to get Filial : {}", id);
        return filialRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the filial by id.
     *
     * @param id the id of the entity.
     */
    public void delete(String id) {
        LOG.debug("Request to delete Filial : {}", id);
        filialRepository.deleteById(id);
    }
}
