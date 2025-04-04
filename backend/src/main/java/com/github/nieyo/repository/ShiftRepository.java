package com.github.nieyo.repository;

import com.github.nieyo.model.Shift;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface ShiftRepository extends MongoRepository<Shift, String> {

    @Query("{ 'startTime': { $lt: ?0 }, 'endTime': { $gt: ?1 } }")
    List<Shift> findOverlappingShifts(Instant startTime, Instant endTime);

}
