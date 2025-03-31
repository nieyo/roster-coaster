package com.github.nieyo.model;

import org.springframework.data.annotation.Id;

import java.time.Instant;
import java.util.List;

public record Shift(
        @Id String id,
        Instant startTime,
        Instant endTime,
        List<User> participants
) {
}
