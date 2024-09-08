package com.hubcarspot.api.repository;

import com.hubcarspot.api.domain.VendaVeiculo;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the VendaVeiculo entity.
 */
@Repository
public interface VendaVeiculoRepository extends MongoRepository<VendaVeiculo, String> {
    @Query("{}")
    Page<VendaVeiculo> findAllWithEagerRelationships(Pageable pageable);

    @Query("{}")
    List<VendaVeiculo> findAllWithEagerRelationships();

    @Query("{'id': ?0}")
    Optional<VendaVeiculo> findOneWithEagerRelationships(String id);
}
