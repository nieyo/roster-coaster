package com.github.nieyo.model.shift;

import lombok.Builder;
import org.springframework.data.annotation.Id;

import java.util.List;

@Builder
public record Shift(
        @Id String id,
        ShiftDuration duration,
        List<ShiftSignup> signups,
        int minParticipants,
        int maxParticipants
) {}
