package com.hubcarspot.api.repository;

import com.hubcarspot.api.domain.Instituicao;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Instituicao entity.
 */
@Repository
public interface InstituicaoRepository extends MongoRepository<Instituicao, String> {}
