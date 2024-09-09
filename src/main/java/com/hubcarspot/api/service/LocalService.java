package com.hubcarspot.api.service;

import com.hubcarspot.api.domain.Instituicao;
import com.hubcarspot.api.domain.Local;
import com.hubcarspot.api.repository.LocalRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link com.hubcarspot.api.domain.Local}.
 */
@Service
public class LocalService {

    private static final Logger LOG = LoggerFactory.getLogger(LocalService.class);

    private final LocalRepository localRepository;
    private final UsuarioInstituicaoService usuarioInstituicaoService;

    public LocalService(LocalRepository localRepository, UsuarioInstituicaoService usuarioInstituicaoService) {
        this.localRepository = localRepository;
        this.usuarioInstituicaoService = usuarioInstituicaoService;
    }

    /**
     * Save a local.
     *
     * @param local the entity to save.
     * @return the persisted entity.
     */
    public Local save(Local local) throws Exception {
        LOG.debug("Request to save Local : {}", local);
        local.setInstituicao(usuarioInstituicaoService.instituicaoDoUsuarioLogado());
        return localRepository.save(local);
    }

    /**
     * Update a local.
     *
     * @param local the entity to save.
     * @return the persisted entity.
     */
    public Local update(Local local) {
        LOG.debug("Request to update Local : {}", local);
        return localRepository.save(local);
    }

    /**
     * Partially update a local.
     *
     * @param local the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Local> partialUpdate(Local local) {
        LOG.debug("Request to partially update Local : {}", local);

        return localRepository
            .findById(local.getId())
            .map(existingLocal -> {
                if (local.getNome() != null) {
                    existingLocal.setNome(local.getNome());
                }

                return existingLocal;
            })
            .map(localRepository::save);
    }

    /**
     * Get all the locals.
     *
     * @return the list of entities.
     */
    public List<Local> findAll() throws Exception {
        LOG.debug("Request to get all Locals");
        Instituicao instituicao = usuarioInstituicaoService.instituicaoDoUsuarioLogado();
        return localRepository.findByInstituicaoId(instituicao.getId());
    }

    /**
     * Get all the locals with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Local> findAllWithEagerRelationships(Pageable pageable) {
        return localRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one local by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<Local> findOne(String id) {
        LOG.debug("Request to get Local : {}", id);
        return localRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the local by id.
     *
     * @param id the id of the entity.
     */
    public void delete(String id) {
        LOG.debug("Request to delete Local : {}", id);
        localRepository.deleteById(id);
    }
}
