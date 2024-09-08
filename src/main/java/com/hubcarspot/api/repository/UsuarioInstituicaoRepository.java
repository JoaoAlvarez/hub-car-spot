package com.hubcarspot.api.repository;

import com.hubcarspot.api.domain.Instituicao;
import com.hubcarspot.api.domain.UsuarioInstituicao;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the UsuarioInstituicao entity.
 */
@Repository
public interface UsuarioInstituicaoRepository extends MongoRepository<UsuarioInstituicao, String> {
    @Query("{}")
    Page<UsuarioInstituicao> findAllWithEagerRelationships(Pageable pageable);

    @Query("{}")
    List<UsuarioInstituicao> findAllWithEagerRelationships();

    @Query("{'id': ?0}")
    Optional<UsuarioInstituicao> findOneWithEagerRelationships(String id);

    Optional<List<UsuarioInstituicao>> findByIdentificador(String currentUserLogin);

    List<UsuarioInstituicao> findByInstituicaoId(Instituicao instituicao);
}
