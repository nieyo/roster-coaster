package com.github.nieyo.repository;

import com.github.nieyo.model.organisation.Organisation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganisationRepository extends MongoRepository<Organisation, String> {

}
