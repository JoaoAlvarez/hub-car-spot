package com.hubcarspot.api.repository;

import com.hubcarspot.api.domain.StatusDocumento;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the StatusDocumento entity.
 */
@Repository
public interface StatusDocumentoRepository extends MongoRepository<StatusDocumento, String> {
    @Query("{}")
    Page<StatusDocumento> findAllWithEagerRelationships(Pageable pageable);

    @Query("{}")
    List<StatusDocumento> findAllWithEagerRelationships();

    @Query("{'id': ?0}")
    Optional<StatusDocumento> findOneWithEagerRelationships(String id);
}
