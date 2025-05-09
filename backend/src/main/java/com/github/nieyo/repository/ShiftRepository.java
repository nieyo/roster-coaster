package com.github.nieyo.repository;

import com.github.nieyo.entity.Shift;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public interface ShiftRepository extends MongoRepository<Shift, UUID> {

    @Query("{ 'duration.start': { $lt: ?1 }, 'duration.end': { $gt: ?0 } }")
    List<Shift> findOverlappingShifts(Instant start, Instant end);

    @Query("{ '_id': { $ne: ?2 }, 'duration.start': { $lt: ?1 }, 'duration.end': { $gt: ?0 } }")
    List<Shift> findOverlappingShiftsExcludingSelf(Instant start, Instant end, UUID shiftId);

}