package com.github.nieyo.dto;

import com.github.nieyo.entity.ShiftSignup;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record ShiftDTO(
        @NotNull(message = "Shift ID must not be empty")
        UUID id,

        @NotNull(message = "Duration is mandatory")
        @Valid
        ShiftDurationDTO duration,

        @NotNull(message = "Participants list must not be null")
        List<@Valid ShiftSignup> signups,

        int minParticipants,
        int maxParticipants
) {}
