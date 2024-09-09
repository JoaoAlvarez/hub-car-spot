package com.hubcarspot.api.repository;

import com.hubcarspot.api.domain.TrocaVeiculo;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the TrocaVeiculo entity.
 */
@Repository
public interface TrocaVeiculoRepository extends MongoRepository<TrocaVeiculo, String> {
    @Query("{}")
    Page<TrocaVeiculo> findAllWithEagerRelationships(Pageable pageable);

    @Query("{}")
    List<TrocaVeiculo> findAllWithEagerRelationships();

    @Query("{'id': ?0}")
    Optional<TrocaVeiculo> findOneWithEagerRelationships(String id);

    Page<TrocaVeiculo> findByInstituicaoId(String id, Pageable pageable);
}
