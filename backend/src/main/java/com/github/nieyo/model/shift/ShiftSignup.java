package com.github.nieyo.model.shift;

import java.time.Instant;

public record ShiftSignup(
        String name,
        String email,      // optional
        String userId,     // optional
        Instant signedUpAt
) {}
