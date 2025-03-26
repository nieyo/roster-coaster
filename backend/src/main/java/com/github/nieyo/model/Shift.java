package com.github.nieyo.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.time.Instant;
import java.util.Map;

public record Shift(
        @Id ObjectId id,
        Instant startTime,
        Instant endTime,
        Map<ObjectId, User> participants
) {
}
