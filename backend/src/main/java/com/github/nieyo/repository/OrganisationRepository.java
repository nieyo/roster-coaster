package com.github.nieyo.repository;

import com.github.nieyo.entity.Organisation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrganisationRepository extends MongoRepository<Organisation, UUID> {

}
