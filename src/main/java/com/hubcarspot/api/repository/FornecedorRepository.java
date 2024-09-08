package com.hubcarspot.api.repository;

import com.hubcarspot.api.domain.Fornecedor;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Fornecedor entity.
 */
@Repository
public interface FornecedorRepository extends MongoRepository<Fornecedor, String> {
    @Query("{}")
    Page<Fornecedor> findAllWithEagerRelationships(Pageable pageable);

    @Query("{}")
    List<Fornecedor> findAllWithEagerRelationships();

    @Query("{'id': ?0}")
    Optional<Fornecedor> findOneWithEagerRelationships(String id);
}
