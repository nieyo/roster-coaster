package com.github.nieyo.entity;

import java.time.Instant;

public record ShiftSignup(
        String name,
        String email,      // optional
        String userId,     // optional
        Instant signedUpAt
) {}
