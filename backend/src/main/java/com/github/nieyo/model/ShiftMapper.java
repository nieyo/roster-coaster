package com.github.nieyo.model;

import java.time.Instant;

public class ShiftMapper {
    public static ShiftDTO toDto(Shift shift) {
        return new ShiftDTO(
                shift.id(),
                shift.duration().stream().map(Instant::toString).toList(),
                shift.participants().stream()
                        .map(user -> new UserDTO(user.name()))
                        .toList()
        );
    }

    public static Shift toShift(ShiftDTO shiftDTO) {
        return new Shift(
                shiftDTO.id(),
                shiftDTO.duration().stream()
                        .map(Instant::parse)
                        .toList(),
                shiftDTO.participants().stream()
                        .map(userDTO -> new User(userDTO.name()))
                        .toList()
        );
    }
}
