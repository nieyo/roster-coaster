package com.github.nieyo.repository;

import com.github.nieyo.model.Shift;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShiftRepository extends MongoRepository<Shift, ObjectId> {
}
