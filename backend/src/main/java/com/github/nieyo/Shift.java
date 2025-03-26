package com.github.nieyo;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.time.Instant;
import java.util.List;

public record Shift(
        @Id ObjectId id,
        Instant startTime,
        Instant endTime,
        List<User> participants
) {
}
