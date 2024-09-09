package com.hubcarspot.api.repository;

import com.hubcarspot.api.domain.Local;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Local entity.
 */
@Repository
public interface LocalRepository extends MongoRepository<Local, String> {
    @Query("{}")
    Page<Local> findAllWithEagerRelationships(Pageable pageable);

    @Query("{}")
    List<Local> findAllWithEagerRelationships();

    @Query("{'id': ?0}")
    Optional<Local> findOneWithEagerRelationships(String id);

    List<Local> findByInstituicaoId(String id);
}
