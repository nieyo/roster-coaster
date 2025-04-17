package com.github.nieyo.model;

import lombok.With;

import java.time.Instant;

public record ShiftDuration(
        @With
        Instant start,
        @With
        Instant end
) {
}
