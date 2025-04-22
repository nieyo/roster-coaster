package com.github.nieyo.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.List;

@Builder
public record CreateShiftDTO(
        @NotNull(message = "Duration is mandatory")
        @Valid
        ShiftDurationDTO duration,

        @NotNull(message = "Participants must not be null")
        List<@Valid UserDTO> participants,
        int minParticipants,
        int maxParticipants
) {}
