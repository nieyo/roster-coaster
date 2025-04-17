package com.github.nieyo.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ShiftDTO(
        @NotEmpty(message = "Shift ID must not be empty")
        String id,

        @NotNull(message = "Duration is mandatory")
        @Valid
        ShiftDurationDTO duration,

        @NotNull(message = "Participants list must not be null")
        List<@Valid UserDTO> participants
) {}
