package com.github.nieyo.model;

import lombok.Builder;
import org.springframework.data.annotation.Id;

import java.util.List;

@Builder
public record Shift(
        @Id String id,
        ShiftDuration duration,
        List<User> participants,
        int minParticipants,
        int maxParticipants
) {}
