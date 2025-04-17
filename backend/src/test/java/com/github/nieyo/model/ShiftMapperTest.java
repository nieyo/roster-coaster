package com.github.nieyo.model;

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
        List<User> participants = List.of(new User("Alice"), new User("Bob"));
        Shift shift = new Shift("shift-1", duration, participants);

        ShiftDTO dto = ShiftMapper.toShiftDto(shift);

        assertEquals("shift-1", dto.id());
        assertEquals(start.toString(), dto.duration().start());
        assertEquals(end.toString(), dto.duration().end());
        assertEquals(2, dto.participants().size());
        assertEquals("Alice", dto.participants().get(0).name());
        assertEquals("Bob", dto.participants().get(1).name());
    }

    @Test
    void toCreateShiftDto_mapsCorrectly() {
        Instant start = Instant.parse("2025-04-18T08:00:00Z");
        Instant end = Instant.parse("2025-04-18T16:00:00Z");
        ShiftDuration duration = new ShiftDuration(start, end);
        List<User> participants = List.of(new User("Alice"));
        Shift shift = new Shift("any-id", duration, participants);

        CreateShiftDTO dto = ShiftMapper.toCreateShiftDto(shift);

        assertNotNull(dto.duration());
        assertEquals(start.toString(), dto.duration().start());
        assertEquals(end.toString(), dto.duration().end());
        assertEquals(1, dto.participants().size());
        assertEquals("Alice", dto.participants().getFirst().name());
    }

    @Test
    void toShift_fromShiftDTO_mapsCorrectly() {
        String id = "shift-1";
        String start = "2025-04-18T08:00:00Z";
        String end = "2025-04-18T16:00:00Z";
        ShiftDurationDTO durationDTO = new ShiftDurationDTO(start, end);
        List<UserDTO> participants = List.of(new UserDTO("Alice"), new UserDTO("Bob"));
        ShiftDTO dto = new ShiftDTO(id, durationDTO, participants);

        Shift shift = ShiftMapper.toShift(dto);

        assertEquals(id, shift.id());
        assertEquals(Instant.parse(start), shift.duration().start());
        assertEquals(Instant.parse(end), shift.duration().end());
        assertEquals(2, shift.participants().size());
        assertEquals("Alice", shift.participants().get(0).name());
        assertEquals("Bob", shift.participants().get(1).name());
    }

    @Test
    void toShift_fromCreateShiftDTO_mapsCorrectly() {
        String id = "shift-1";
        String start = "2025-04-18T08:00:00Z";
        String end = "2025-04-18T16:00:00Z";
        ShiftDurationDTO durationDTO = new ShiftDurationDTO(start, end);
        List<UserDTO> participants = List.of(new UserDTO("Alice"));
        CreateShiftDTO dto = new CreateShiftDTO(durationDTO, participants);

        Shift shift = ShiftMapper.toShift(dto, id);

        assertEquals(id, shift.id());
        assertEquals(Instant.parse(start), shift.duration().start());
        assertEquals(Instant.parse(end), shift.duration().end());
        assertEquals(1, shift.participants().size());
        assertEquals("Alice", shift.participants().getFirst().name());
    }

    @Test
    void toShiftDto_handlesEmptyParticipants() {
        Instant start = Instant.parse("2025-04-18T08:00:00Z");
        Instant end = Instant.parse("2025-04-18T16:00:00Z");
        ShiftDuration duration = new ShiftDuration(start, end);
        Shift shift = new Shift("shift-2", duration, List.of());

        ShiftDTO dto = ShiftMapper.toShiftDto(shift);

        assertEquals("shift-2", dto.id());
        assertEquals(0, dto.participants().size());
    }

    @Test
    void toShift_handlesEmptyParticipants() {
        String id = "shift-3";
        String start = "2025-04-18T08:00:00Z";
        String end = "2025-04-18T16:00:00Z";
        ShiftDurationDTO durationDTO = new ShiftDurationDTO(start, end);
        ShiftDTO dto = new ShiftDTO(id, durationDTO, List.of());

        Shift shift = ShiftMapper.toShift(dto);

        assertEquals(id, shift.id());
        assertEquals(0, shift.participants().size());
    }
}