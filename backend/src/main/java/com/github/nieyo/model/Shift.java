package com.github.nieyo.model;

import org.springframework.data.annotation.Id;

import java.util.List;

public record Shift(
        @Id String id,
        ShiftDuration duration,
        List<User> participants,
        int maxParticipants
) {
    public Shift(String id, ShiftDuration duration, List<User> participants) {
        this(id, duration, participants, 0);
    }
}
