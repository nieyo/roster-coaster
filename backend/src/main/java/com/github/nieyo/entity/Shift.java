package com.github.nieyo.entity;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.UUID;

@Builder
@Document(collection = "shifts")
public record Shift(
        @Id UUID id,
        ShiftDuration duration,
        List<ShiftSignup> signups,
        int minParticipants,
        int maxParticipants
) {}
