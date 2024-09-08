package com.hubcarspot.api.repository;

import com.hubcarspot.api.domain.Marca;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Marca entity.
 */
@Repository
public interface MarcaRepository extends MongoRepository<Marca, String> {}
