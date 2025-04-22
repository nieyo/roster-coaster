package com.github.nieyo.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateShiftDTO(
        @NotNull(message = "Duration is mandatory")
        @Valid
        ShiftDurationDTO duration,

        @NotNull(message = "Participants must not be null")
        List<@Valid UserDTO> participants,

        int maxParticipants
) {
        public CreateShiftDTO(@Valid ShiftDurationDTO duration, @Valid List<UserDTO> participants) {
                this(duration, participants, 0);
        }
}
