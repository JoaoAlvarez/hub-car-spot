package com.hubcarspot.api.repository;

import com.hubcarspot.api.domain.Taxas;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Taxas entity.
 */
@Repository
public interface TaxasRepository extends MongoRepository<Taxas, String> {
    @Query("{}")
    Page<Taxas> findAllWithEagerRelationships(Pageable pageable);

    @Query("{}")
    List<Taxas> findAllWithEagerRelationships();

    @Query("{'id': ?0}")
    Optional<Taxas> findOneWithEagerRelationships(String id);

    List<Taxas> findByInstituicaoId(String id);
}
