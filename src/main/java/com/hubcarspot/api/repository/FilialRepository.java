package com.hubcarspot.api.repository;

import com.hubcarspot.api.domain.Filial;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Filial entity.
 */
@Repository
public interface FilialRepository extends MongoRepository<Filial, String> {
    @Query("{}")
    Page<Filial> findAllWithEagerRelationships(Pageable pageable);

    @Query("{}")
    List<Filial> findAllWithEagerRelationships();

    @Query("{'id': ?0}")
    Optional<Filial> findOneWithEagerRelationships(String id);

    List<Filial> findByInstituicaoId(String id);
}
