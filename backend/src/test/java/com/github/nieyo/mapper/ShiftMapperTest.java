package com.github.nieyo.mapper;

import com.github.nieyo.dto.ShiftCreateDTO;
import com.github.nieyo.dto.ShiftDTO;
import com.github.nieyo.dto.ShiftDurationDTO;
import com.github.nieyo.entity.Shift;
import com.github.nieyo.entity.ShiftDuration;
import com.github.nieyo.entity.ShiftSignup;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ShiftMapperTest {

    private static final UUID SHIFT1_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final UUID SHIFT2_ID = UUID.fromString("22222222-2222-2222-2222-222222222222");
    private static final UUID SHIFT3_ID = UUID.fromString("33333333-3333-3333-3333-333333333333");


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
                .id(SHIFT1_ID)
                .duration(duration)
                .signups(signups)
                .build();


        ShiftDTO dto = ShiftMapper.toShiftDto(shift);

        assertEquals(SHIFT1_ID, dto.id());
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
                .id(SHIFT1_ID)
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
        UUID id = SHIFT1_ID;
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
        UUID id = SHIFT1_ID;
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
                .id(SHIFT2_ID)
                .duration(duration)
                .signups(List.of())
                .build();

        ShiftDTO dto = ShiftMapper.toShiftDto(shift);

        assertEquals(SHIFT2_ID, dto.id());
        assertEquals(0, dto.signups().size());
    }

    @Test
    void toShift_handlesEmptysignups() {
        UUID id = SHIFT3_ID;
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