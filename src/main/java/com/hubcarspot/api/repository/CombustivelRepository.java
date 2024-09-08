package com.hubcarspot.api.repository;

import com.hubcarspot.api.domain.Combustivel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Combustivel entity.
 */
@Repository
public interface CombustivelRepository extends MongoRepository<Combustivel, String> {}
