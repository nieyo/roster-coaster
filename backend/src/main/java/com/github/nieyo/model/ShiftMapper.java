package com.github.nieyo.model;

import java.time.Instant;


public class ShiftMapper {

    private ShiftMapper() {
    }

    public static ShiftDTO toShiftDto(Shift shift) {
        return ShiftDTO.builder()
                .id(shift.id())
                .duration(new ShiftDurationDTO(
                        shift.duration().start().toString(),
                        shift.duration().end().toString()
                ))
                .participants(
                        shift.participants().stream()
                                .map(user -> new UserDTO(user.name()))
                                .toList()
                )
                .minParticipants(shift.minParticipants()) // Include if ShiftDTO has this field
                .maxParticipants(shift.maxParticipants())
                .build();
    }

    public static CreateShiftDTO toCreateShiftDto(Shift shift) {
        return CreateShiftDTO.builder()
                .duration(new ShiftDurationDTO(
                        shift.duration().start().toString(),
                        shift.duration().end().toString()
                ))
                .participants(
                        shift.participants().stream()
                                .map(user -> new UserDTO(user.name()))
                                .toList()
                )
                .minParticipants(shift.minParticipants())
                .maxParticipants(shift.maxParticipants())
                .build();
    }


    public static Shift toShift(ShiftDTO dto) {
        return Shift.builder()
                .id(dto.id())
                .duration(new ShiftDuration(
                        Instant.parse(dto.duration().start()),
                        Instant.parse(dto.duration().end())
                ))
                .participants(
                        dto.participants().stream()
                                .map(userDto -> new User(userDto.name()))
                                .toList()
                )
                .minParticipants(dto.minParticipants())
                .maxParticipants(dto.maxParticipants())
                .build();
    }


    public static Shift toShift(CreateShiftDTO dto, String id) {
        return Shift.builder()
                .id(id)
                .duration(new ShiftDuration(
                        Instant.parse(dto.duration().start()),
                        Instant.parse(dto.duration().end())
                ))
                .participants(
                        dto.participants().stream()
                                .map(userDto -> new User(userDto.name()))
                                .toList()
                )
                .minParticipants(dto.minParticipants())
                .maxParticipants(dto.maxParticipants())
                .build();
    }
}