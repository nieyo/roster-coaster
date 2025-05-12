package com.github.nieyo.mapper;

import com.github.nieyo.dto.ShiftDurationDTO;
import com.github.nieyo.entity.ShiftDuration;

import java.time.Instant;

public class ShiftDurationMapper {

    private ShiftDurationMapper(){
    }

    /**
     * Converts a domain ShiftDuration to a DTO.
     */
    public static ShiftDurationDTO toDto(ShiftDuration duration) {
        if (duration == null) {
            return null;
        }

        String start = optionalInstantToString(duration.start());
        String end = optionalInstantToString(duration.end());

        return new ShiftDurationDTO(start, end);
    }

    /**
     * Converts a DTO to a domain ShiftDuration.
     */
    public static ShiftDuration fromDto(ShiftDurationDTO dto) {
        if (dto == null) {
            return null;
        }

        Instant start = optionalStringToInstant(dto.start());
        Instant end = optionalStringToInstant(dto.end());

        return new ShiftDuration(start, end);
    }

    // --- Helper methods ---

    private static String optionalInstantToString(Instant instant) {
        return instant == null ? null : instant.toString();
    }

    private static Instant optionalStringToInstant(String str) {
        return (str == null || str.isBlank()) ? null : Instant.parse(str);
    }
}



