package com.hubcarspot.api.service;

import com.hubcarspot.api.domain.UsuarioInstituicao;
import com.hubcarspot.api.repository.UsuarioInstituicaoRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link com.hubcarspot.api.domain.UsuarioInstituicao}.
 */
@Service
public class UsuarioInstituicaoService {

    private static final Logger LOG = LoggerFactory.getLogger(UsuarioInstituicaoService.class);

    private final UsuarioInstituicaoRepository usuarioInstituicaoRepository;

    public UsuarioInstituicaoService(UsuarioInstituicaoRepository usuarioInstituicaoRepository) {
        this.usuarioInstituicaoRepository = usuarioInstituicaoRepository;
    }

    /**
     * Save a usuarioInstituicao.
     *
     * @param usuarioInstituicao the entity to save.
     * @return the persisted entity.
     */
    public UsuarioInstituicao save(UsuarioInstituicao usuarioInstituicao) {
        LOG.debug("Request to save UsuarioInstituicao : {}", usuarioInstituicao);
        return usuarioInstituicaoRepository.save(usuarioInstituicao);
    }

    /**
     * Update a usuarioInstituicao.
     *
     * @param usuarioInstituicao the entity to save.
     * @return the persisted entity.
     */
    public UsuarioInstituicao update(UsuarioInstituicao usuarioInstituicao) {
        LOG.debug("Request to update UsuarioInstituicao : {}", usuarioInstituicao);
        return usuarioInstituicaoRepository.save(usuarioInstituicao);
    }

    /**
     * Partially update a usuarioInstituicao.
     *
     * @param usuarioInstituicao the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<UsuarioInstituicao> partialUpdate(UsuarioInstituicao usuarioInstituicao) {
        LOG.debug("Request to partially update UsuarioInstituicao : {}", usuarioInstituicao);

        return usuarioInstituicaoRepository
            .findById(usuarioInstituicao.getId())
            .map(existingUsuarioInstituicao -> {
                if (usuarioInstituicao.getIdentificador() != null) {
                    existingUsuarioInstituicao.setIdentificador(usuarioInstituicao.getIdentificador());
                }
                if (usuarioInstituicao.getIsMaster() != null) {
                    existingUsuarioInstituicao.setIsMaster(usuarioInstituicao.getIsMaster());
                }
                if (usuarioInstituicao.getRole() != null) {
                    existingUsuarioInstituicao.setRole(usuarioInstituicao.getRole());
                }
                if (usuarioInstituicao.getRead() != null) {
                    existingUsuarioInstituicao.setRead(usuarioInstituicao.getRead());
                }
                if (usuarioInstituicao.getWrite() != null) {
                    existingUsuarioInstituicao.setWrite(usuarioInstituicao.getWrite());
                }
                if (usuarioInstituicao.getUpdate() != null) {
                    existingUsuarioInstituicao.setUpdate(usuarioInstituicao.getUpdate());
                }

                return existingUsuarioInstituicao;
            })
            .map(usuarioInstituicaoRepository::save);
    }

    /**
     * Get all the usuarioInstituicaos.
     *
     * @return the list of entities.
     */
    public List<UsuarioInstituicao> findAll() {
        LOG.debug("Request to get all UsuarioInstituicaos");
        return usuarioInstituicaoRepository.findAll();
    }

    /**
     * Get all the usuarioInstituicaos with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<UsuarioInstituicao> findAllWithEagerRelationships(Pageable pageable) {
        return usuarioInstituicaoRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one usuarioInstituicao by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<UsuarioInstituicao> findOne(String id) {
        LOG.debug("Request to get UsuarioInstituicao : {}", id);
        return usuarioInstituicaoRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the usuarioInstituicao by id.
     *
     * @param id the id of the entity.
     */
    public void delete(String id) {
        LOG.debug("Request to delete UsuarioInstituicao : {}", id);
        usuarioInstituicaoRepository.deleteById(id);
    }
}
