package com.github.nieyo.model;

import com.github.nieyo.model.shift.*;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ShiftMapperTest {

    @Test
    void toShiftDto_mapsCorrectly() {
        Instant start = Instant.parse("2025-04-18T08:00:00Z");
        Instant end = Instant.parse("2025-04-18T16:00:00Z");
        ShiftDuration duration = new ShiftDuration(start, end);
        List<ShiftSignup> signups = List.of(
                new ShiftSignup("Alice", null, null, Instant.now()),
                new ShiftSignup("Bob", null, null, Instant.now())
        );
        Shift shift = Shift.builder()
                .id("shift-1")
                .duration(duration)
                .signups(signups)
                .build();


        ShiftDTO dto = ShiftMapper.toShiftDto(shift);

        assertEquals("shift-1", dto.id());
        assertEquals(start.toString(), dto.duration().start());
        assertEquals(end.toString(), dto.duration().end());
        assertEquals(2, dto.signups().size());
        assertEquals("Alice", dto.signups().get(0).name());
        assertEquals("Bob", dto.signups().get(1).name());
    }

    @Test
    void toCreateShiftDto_mapsCorrectly() {
        Instant start = Instant.parse("2025-04-18T08:00:00Z");
        Instant end = Instant.parse("2025-04-18T16:00:00Z");
        ShiftDuration duration = new ShiftDuration(start, end);
        List<ShiftSignup> signups = List.of(new ShiftSignup("Alice", null, null, Instant.now()));
        Shift shift = Shift.builder()
                .id("any-id")
                .duration(duration)
                .signups(signups)
                .build();

        ShiftCreateDTO dto = ShiftMapper.toCreateShiftDto(shift);

        assertNotNull(dto.duration());
        assertEquals(start.toString(), dto.duration().start());
        assertEquals(end.toString(), dto.duration().end());
        assertEquals(1, dto.signups().size());
        assertEquals("Alice", dto.signups().getFirst().name());
    }

    @Test
    void toShift_fromShiftDTO_mapsCorrectly() {
        String id = "shift-1";
        String start = "2025-04-18T08:00:00Z";
        String end = "2025-04-18T16:00:00Z";
        ShiftDurationDTO durationDTO = new ShiftDurationDTO(start, end);
        List<ShiftSignup> signups = List.of(
                new ShiftSignup("Alice", null, null, Instant.now()),
                new ShiftSignup("Bob", null, null, Instant.now())
        );
        ShiftDTO dto = ShiftDTO.builder()
                .id(id)
                .duration(durationDTO)
                .signups(signups)
                .build();

        Shift shift = ShiftMapper.toShift(dto);

        assertEquals(id, shift.id());
        assertEquals(Instant.parse(start), shift.duration().start());
        assertEquals(Instant.parse(end), shift.duration().end());
        assertEquals(2, shift.signups().size());
        assertEquals("Alice", shift.signups().get(0).name());
        assertEquals("Bob", shift.signups().get(1).name());
    }

    @Test
    void toShift_fromCreateShiftDTO_mapsCorrectly() {
        String id = "shift-1";
        String start = "2025-04-18T08:00:00Z";
        String end = "2025-04-18T16:00:00Z";
        ShiftDurationDTO durationDTO = new ShiftDurationDTO(start, end);
        List<ShiftSignup> signups = List.of(new ShiftSignup("Alice", null, null, Instant.now()));
        ShiftCreateDTO dto = ShiftCreateDTO.builder()
                .duration(durationDTO)
                .signups(signups)
                .build();

        Shift shift = ShiftMapper.toShift(dto, id);

        assertEquals(id, shift.id());
        assertEquals(Instant.parse(start), shift.duration().start());
        assertEquals(Instant.parse(end), shift.duration().end());
        assertEquals(1, shift.signups().size());
        assertEquals("Alice", shift.signups().getFirst().name());
    }

    @Test
    void toShiftDto_handlesEmptysignups() {
        Instant start = Instant.parse("2025-04-18T08:00:00Z");
        Instant end = Instant.parse("2025-04-18T16:00:00Z");
        ShiftDuration duration = new ShiftDuration(start, end);
        Shift shift = Shift.builder()
                .id("shift-2")
                .duration(duration)
                .signups(List.of())
                .build();

        ShiftDTO dto = ShiftMapper.toShiftDto(shift);

        assertEquals("shift-2", dto.id());
        assertEquals(0, dto.signups().size());
    }

    @Test
    void toShift_handlesEmptysignups() {
        String id = "shift-3";
        String start = "2025-04-18T08:00:00Z";
        String end = "2025-04-18T16:00:00Z";
        ShiftDurationDTO durationDTO = new ShiftDurationDTO(start, end);
        ShiftDTO dto = ShiftDTO.builder()
                .id(id)
                .duration(durationDTO)
                .signups(List.of())
                .build();

        Shift shift = ShiftMapper.toShift(dto);

        assertEquals(id, shift.id());
        assertEquals(0, shift.signups().size());
    }
}