package com.hubcarspot.api.repository;

import com.hubcarspot.api.domain.Veiculo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Veiculo entity.
 */
@Repository
public interface VeiculoRepository extends MongoRepository<Veiculo, String> {}
