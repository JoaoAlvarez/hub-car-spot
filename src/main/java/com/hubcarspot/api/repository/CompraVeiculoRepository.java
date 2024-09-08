package com.hubcarspot.api.repository;

import com.hubcarspot.api.domain.CompraVeiculo;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the CompraVeiculo entity.
 */
@Repository
public interface CompraVeiculoRepository extends MongoRepository<CompraVeiculo, String> {
    @Query("{}")
    Page<CompraVeiculo> findAllWithEagerRelationships(Pageable pageable);

    @Query("{}")
    List<CompraVeiculo> findAllWithEagerRelationships();

    @Query("{'id': ?0}")
    Optional<CompraVeiculo> findOneWithEagerRelationships(String id);
}
