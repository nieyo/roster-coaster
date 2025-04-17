package com.github.nieyo.model;

import java.time.Instant;


public class ShiftMapper {

    private ShiftMapper() {}

    public static ShiftDTO toShiftDto(Shift shift) {
        return new ShiftDTO(
                shift.id(),
                new ShiftDurationDTO(
                        shift.duration().start().toString(),
                        shift.duration().end().toString()
                ),
                shift.participants().stream()
                        .map(user -> new UserDTO(user.name()))
                        .toList()
        );
    }

    public static CreateShiftDTO toCreateShiftDto(Shift shift) {
        return new CreateShiftDTO(
                new ShiftDurationDTO(
                        shift.duration().start().toString(),
                        shift.duration().end().toString()
                ),
                shift.participants().stream()
                        .map(user -> new UserDTO(user.name()))
                        .toList()
        );
    }

    public static Shift toShift(ShiftDTO dto) {
        return new Shift(
                dto.id(),
                new ShiftDuration(
                        Instant.parse(dto.duration().start()),
                        Instant.parse(dto.duration().end())
                ),
                dto.participants().stream()
                        .map(userDto -> new User(userDto.name()))
                        .toList()
        );
    }

    public static Shift toShift(CreateShiftDTO dto, String id) {
        return new Shift(
                id,
                new ShiftDuration(
                        Instant.parse(dto.duration().start()),
                        Instant.parse(dto.duration().end())
                ),
                dto.participants().stream()
                        .map(userDto -> new User(userDto.name()))
                        .toList()
        );
    }
}