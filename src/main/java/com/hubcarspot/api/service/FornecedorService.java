package com.hubcarspot.api.service;

import com.hubcarspot.api.domain.Fornecedor;
import com.hubcarspot.api.domain.Instituicao;
import com.hubcarspot.api.repository.FornecedorRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link com.hubcarspot.api.domain.Fornecedor}.
 */
@Service
public class FornecedorService {

    private static final Logger LOG = LoggerFactory.getLogger(FornecedorService.class);

    private final FornecedorRepository fornecedorRepository;
    private final UsuarioInstituicaoService usuarioInstituicaoService;

    public FornecedorService(FornecedorRepository fornecedorRepository, UsuarioInstituicaoService usuarioInstituicaoService) {
        this.fornecedorRepository = fornecedorRepository;
        this.usuarioInstituicaoService = usuarioInstituicaoService;
    }

    /**
     * Save a fornecedor.
     *
     * @param fornecedor the entity to save.
     * @return the persisted entity.
     */
    public Fornecedor save(Fornecedor fornecedor) throws Exception {
        LOG.debug("Request to save Fornecedor : {}", fornecedor);
        fornecedor.setInstituicao(usuarioInstituicaoService.instituicaoDoUsuarioLogado());
        return fornecedorRepository.save(fornecedor);
    }

    /**
     * Update a fornecedor.
     *
     * @param fornecedor the entity to save.
     * @return the persisted entity.
     */
    public Fornecedor update(Fornecedor fornecedor) {
        LOG.debug("Request to update Fornecedor : {}", fornecedor);
        return fornecedorRepository.save(fornecedor);
    }

    /**
     * Partially update a fornecedor.
     *
     * @param fornecedor the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Fornecedor> partialUpdate(Fornecedor fornecedor) {
        LOG.debug("Request to partially update Fornecedor : {}", fornecedor);

        return fornecedorRepository
            .findById(fornecedor.getId())
            .map(existingFornecedor -> {
                if (fornecedor.getNome() != null) {
                    existingFornecedor.setNome(fornecedor.getNome());
                }
                if (fornecedor.getCnpj() != null) {
                    existingFornecedor.setCnpj(fornecedor.getCnpj());
                }
                if (fornecedor.getTelefone() != null) {
                    existingFornecedor.setTelefone(fornecedor.getTelefone());
                }
                if (fornecedor.getCep() != null) {
                    existingFornecedor.setCep(fornecedor.getCep());
                }
                if (fornecedor.getEndereco() != null) {
                    existingFornecedor.setEndereco(fornecedor.getEndereco());
                }
                if (fornecedor.getBairro() != null) {
                    existingFornecedor.setBairro(fornecedor.getBairro());
                }
                if (fornecedor.getCidade() != null) {
                    existingFornecedor.setCidade(fornecedor.getCidade());
                }
                if (fornecedor.getNumero() != null) {
                    existingFornecedor.setNumero(fornecedor.getNumero());
                }
                if (fornecedor.getUf() != null) {
                    existingFornecedor.setUf(fornecedor.getUf());
                }

                return existingFornecedor;
            })
            .map(fornecedorRepository::save);
    }

    /**
     * Get all the fornecedors.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Page<Fornecedor> findAll(Pageable pageable) throws Exception {
        LOG.debug("Request to get all Fornecedors");
        Instituicao instituicao = usuarioInstituicaoService.instituicaoDoUsuarioLogado();
        return fornecedorRepository.findByInstituicaoId(instituicao.getId(), pageable);
    }

    /**
     * Get all the fornecedors with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Fornecedor> findAllWithEagerRelationships(Pageable pageable) {
        return fornecedorRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one fornecedor by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<Fornecedor> findOne(String id) {
        LOG.debug("Request to get Fornecedor : {}", id);
        return fornecedorRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the fornecedor by id.
     *
     * @param id the id of the entity.
     */
    public void delete(String id) {
        LOG.debug("Request to delete Fornecedor : {}", id);
        fornecedorRepository.deleteById(id);
    }
}
