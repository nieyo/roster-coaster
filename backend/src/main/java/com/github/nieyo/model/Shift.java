package com.github.nieyo.model;

import org.springframework.data.annotation.Id;

import java.time.Instant;
import java.util.List;

public record Shift(
        @Id String id,
        List<Instant> duration,
        List<User> participants
) {
}
