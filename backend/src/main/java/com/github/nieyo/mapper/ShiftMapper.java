package com.github.nieyo.mapper;

import com.github.nieyo.dto.ShiftCreateDTO;
import com.github.nieyo.dto.ShiftDTO;
import com.github.nieyo.dto.ShiftDurationDTO;
import com.github.nieyo.entity.Shift;
import com.github.nieyo.entity.ShiftDuration;
import com.github.nieyo.entity.ShiftSignup;

import java.time.Instant;
import java.util.UUID;


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
                .signups(
                        shift.signups().stream()
                                .map(signup -> new ShiftSignup(signup.name(), signup.email(), signup.userId(), signup.signedUpAt()))
                                .toList()
                )
                .minParticipants(shift.minParticipants()) // Include if ShiftDTO has this field
                .maxParticipants(shift.maxParticipants())
                .build();
    }

    public static ShiftCreateDTO toCreateShiftDto(Shift shift) {
        return ShiftCreateDTO.builder()
                .duration(new ShiftDurationDTO(
                        shift.duration().start().toString(),
                        shift.duration().end().toString()
                ))
                .signups(
                        shift.signups().stream()
                                .map(signup -> new ShiftSignup(signup.name(), signup.email(), signup.userId(), signup.signedUpAt()))
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
                .signups(
                        dto.signups().stream()
                                .map(signup -> new ShiftSignup(signup.name(), signup.email(), signup.userId(), signup.signedUpAt()))
                                .toList()
                )
                .minParticipants(dto.minParticipants())
                .maxParticipants(dto.maxParticipants())
                .build();
    }


    public static Shift toShift(ShiftCreateDTO dto, UUID id) {
        return Shift.builder()
                .id(id)
                .duration(new ShiftDuration(
                        Instant.parse(dto.duration().start()),
                        Instant.parse(dto.duration().end())
                ))
                .signups(
                        dto.signups().stream()
                                .map(signup -> new ShiftSignup(signup.name(), signup.email(), signup.userId(), signup.signedUpAt()))
                                .toList()
                )
                .minParticipants(dto.minParticipants())
                .maxParticipants(dto.maxParticipants())
                .build();
    }
}