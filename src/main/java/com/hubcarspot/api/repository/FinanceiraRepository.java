package com.hubcarspot.api.repository;

import com.hubcarspot.api.domain.Financeira;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Financeira entity.
 */
@Repository
public interface FinanceiraRepository extends MongoRepository<Financeira, String> {
    @Query("{}")
    Page<Financeira> findAllWithEagerRelationships(Pageable pageable);

    @Query("{}")
    List<Financeira> findAllWithEagerRelationships();

    @Query("{'id': ?0}")
    Optional<Financeira> findOneWithEagerRelationships(String id);
}
