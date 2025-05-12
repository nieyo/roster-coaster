package com.github.nieyo.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

@Builder
public record ShiftDurationDTO(
        @NotEmpty(message = "Shift start time must not be empty")
        String start, // ISO date string

        @NotEmpty(message = "Shift end time must not be empty")
        String end // ISO date string
) {}
