package com.github.nieyo.service;

import com.github.nieyo.dto.ShiftCreateDTO;
import com.github.nieyo.dto.ShiftDTO;
import com.github.nieyo.dto.ShiftDurationDTO;
import com.github.nieyo.entity.Shift;
import com.github.nieyo.entity.ShiftDuration;
import com.github.nieyo.entity.ShiftSignup;
import com.github.nieyo.mapper.ShiftDurationMapper;
import com.github.nieyo.mapper.ShiftMapper;
import com.github.nieyo.repository.ShiftRepository;
import com.github.nieyo.validation.ShiftValidator;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ShiftServiceTest {

    ShiftRepository shiftRepository = mock(ShiftRepository.class);
    IdService idService = mock(IdService.class);
    private final Clock clock = Clock.fixed(Instant.parse("2025-04-01T00:00:00Z"), ZoneOffset.UTC);

    ShiftValidator shiftValidator = new ShiftValidator(shiftRepository, clock);
    ShiftService shiftService = new ShiftService(shiftRepository, idService, shiftValidator);

    Instant now = clock.instant();
    Instant startTime = now.plus(Duration.ofMinutes(15));
    Instant endTime = startTime.plus(Duration.ofMinutes(30));
    ShiftDuration duration = new ShiftDuration(startTime, endTime);
    ShiftDurationDTO durationDTO = ShiftDurationMapper.toDto(duration);
    List<ShiftSignup> signups = List.of();

    private static final UUID NOT_EXISTING_ID = UUID.fromString("00000000-0000-0000-0000-000000000000");
    private static final UUID ID_1 = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final UUID ID_2 = UUID.fromString("22222222-2222-2222-2222-222222222222");
    private static final UUID ID_3 = UUID.fromString("33333333-3333-3333-3333-333333333333");


    // CREATE
    @Test
    void saveShift_ShouldPersistNewEntity() {

        // GIVEN
        ShiftCreateDTO inputShift = ShiftCreateDTO.builder()
                .duration(durationDTO)
                .signups(List.of())
                .build();

        UUID expectedId = ID_1;
        when(idService.randomId()).thenReturn(expectedId);

        Shift expected = Shift.builder()
                .id(expectedId)
                .duration(duration)
                .signups(signups)
                .build();

        when(shiftRepository.save(any(Shift.class))).thenReturn(expected);

        // WHEN
        ShiftDTO actual = shiftService.saveShift(inputShift);

        // THEN
        verify(idService).randomId();
        verify(shiftRepository).save(any(Shift.class));
        assertNotNull(actual);
        assertEquals(expected.id(), actual.id());
        assertEquals(expected.duration().start().toString(), actual.duration().start());
        assertEquals(expected.duration().end().toString(), actual.duration().end());
        assertEquals(expected.signups(), actual.signups());
    }

    @Test
    void saveShift_shouldThrowException_whenShiftIsNull() {

        // WHEN & THEN
        assertThrows(NullPointerException.class, () -> shiftService.saveShift(null));

        // Verify repository was never called
        verify(shiftRepository, never()).save(any());
    }

    @Test
    void saveShift_ShouldThrowException_WhenRequiredFieldIsNull() {
        ShiftCreateDTO invalidShiftNoStart = ShiftCreateDTO.builder()
                .duration(
                        ShiftDurationDTO.builder()
                                .start(null)
                                .end(endTime.toString())
                                .build()
                )
                .signups(List.of())
                .build();

        ShiftCreateDTO invalidShiftNoEnd = ShiftCreateDTO.builder()
                .duration(
                        ShiftDurationDTO.builder()
                                .start(startTime.toString())
                                .end(null)
                                .build()
                )
                .signups(List.of())
                .build();

        assertThrows(NullPointerException.class, () -> shiftService.saveShift(invalidShiftNoStart));
        assertThrows(NullPointerException.class, () -> shiftService.saveShift(invalidShiftNoEnd));

        // Verify repository was never called
        verify(shiftRepository, never()).save(any());
    }

    @Test
    void saveShift_ShouldThrowException_WhenEndTimeBeforeStartTime() {
        ShiftCreateDTO invalidShift = ShiftCreateDTO.builder()
                .duration(
                        ShiftDurationDTO.builder()
                                .start(endTime.toString()) // wrong order: end as start
                                .end(startTime.toString()) // wrong order: start as end
                                .build()
                )
                .signups(List.of())
                .build();

        assertThrows(IllegalArgumentException.class, () -> shiftService.saveShift(invalidShift));

        // Verify repository was never called
        verify(shiftRepository, never()).save(any());
    }

    // GET ALL
    @Test
    void getShifts_whenEmpty_returnEmptyList() {
        // GIVEN
        when(shiftRepository.findAll()).thenReturn(List.of());

        // WHEN
        List<ShiftDTO> actual = shiftService.getShifts();

        // THEN
        verify(shiftRepository).findAll();
        assertTrue(actual.isEmpty());
    }


    @Test
    void getShifts_whenNotEmpty_returnShiftList() {
        // GIVEN
        List<Shift> shifts = List.of(
                Shift.builder()
                        .id(ID_1)
                        .duration(duration)
                        .signups(signups)
                        .build(),
                Shift.builder()
                        .id(ID_2)
                        .duration(duration)
                        .signups(signups)
                        .build(),
                Shift.builder()
                        .id(ID_3)
                        .duration(duration)
                        .signups(signups)
                        .build()
        );
        List<ShiftDTO> expected = shifts.stream()
                .map(ShiftMapper::toShiftDto)
                .toList();

        when(shiftRepository.findAll()).thenReturn(shifts);

        // WHEN
        List<ShiftDTO> actual = shiftService.getShifts();

        // THEN
        verify(shiftRepository).findAll();
        assertEquals(expected, actual);
    }

    // GET BY ID
    @Test
    void getShiftById_whenIdExists_returnShift() {
        // GIVEN
        UUID existingId = ID_1;
        Shift shift = Shift.builder()
                .id(existingId)
                .duration(duration)
                .signups(signups)
                .build();
        Optional<Shift> found = Optional.of(shift);
        ShiftDTO expectedDto = ShiftMapper.toShiftDto(shift);

        when(shiftRepository.findById(existingId)).thenReturn(found);

        // WHEN
        Optional<ShiftDTO> actual = shiftService.getShiftById(existingId);

        // THEN
        verify(shiftRepository).findById(existingId);
        assertTrue(actual.isPresent());
        assertEquals(expectedDto, actual.get());
    }


    @Test
    void getShiftById_whenIdDoesNotExists_returnEmptyOptional() {
        // GIVEN
        UUID nonExistingId = NOT_EXISTING_ID;
        when(shiftRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        // WHEN
        Optional<ShiftDTO> actual = shiftService.getShiftById(nonExistingId);

        // THEN
        verify(shiftRepository).findById(nonExistingId);
        assertTrue(actual.isEmpty());
    }


    // UPDATE
    @Test
    void updateShift_whenFound_returnShift() {
        // GIVEN
        UUID targetId = ID_2;
        Shift expected = Shift.builder()
                .id(ID_2)
                .duration(duration.withEnd(endTime.plusSeconds(3600)))
                .signups(signups)
                .build();
        when(shiftRepository.existsById(targetId)).thenReturn(true);
        when(shiftRepository.save(expected)).thenReturn(expected);

        // WHEN
        ShiftDTO actual = shiftService.updateShift(targetId, ShiftMapper.toShiftDto(expected));

        // THEN
        verify(shiftRepository).existsById(targetId);
        verify(shiftRepository).save(expected);
        assertEquals(expected.id(), actual.id());
        assertEquals(expected.duration().start().toString(), actual.duration().start());
        assertEquals(expected.duration().end().toString(), actual.duration().end());
        assertEquals(expected.signups(), actual.signups());
    }

    @Test
    void updateShift_whenNotFound_throwNoSuchElementException() {
        // GIVEN
        UUID targetId = ID_3;
        Shift updatedShift = Shift.builder()
                .id(targetId)
                .duration(duration)
                .signups(signups)
                .build();
        ShiftDTO updatedShiftDto = ShiftMapper.toShiftDto(updatedShift);
        when(shiftRepository.existsById(targetId)).thenReturn(false);

        // WHEN + THEN
        assertThrows(NoSuchElementException.class, () -> shiftService.updateShift(targetId, updatedShiftDto));
        verify(shiftRepository, never()).save(any());
    }


    @Test
    void updateShift_whenIdDoesNotMatch_throwIllegalArgumentException() {
        // GIVEN
        UUID targetId = ID_1;
        Shift updatedShift = Shift.builder()
                .id(ID_2)
                .duration(duration)
                .signups(signups)
                .build();
        when(shiftRepository.existsById(targetId)).thenReturn(true);
        when(shiftRepository.save(updatedShift)).thenReturn(updatedShift);

        // Prepare the DTO outside the lambda
        ShiftDTO updatedShiftDto = ShiftMapper.toShiftDto(updatedShift);

        // WHEN + THEN: Only one invocation in the lambda
        assertThrows(IllegalArgumentException.class, () -> shiftService.updateShift(targetId, updatedShiftDto));
        verify(shiftRepository, never()).save(any());
    }


    // DELETE
    @Test
    void deleteShift_whenFound_deleteShift() {
        // GIVEN
        when(shiftRepository.existsById(ID_1)).thenReturn(true);

        // WHEN
        boolean deleted = shiftService.deleteShiftById(ID_1);

        // THEN
        verify(shiftRepository).deleteById(ID_1);
        assertTrue(deleted);
    }

    @Test
    void deleteShift_whenNotFound_returnFalse() {
        // GIVEN
        when(shiftRepository.existsById(ID_1)).thenReturn(false);

        // WHEN
        boolean deleted = shiftService.deleteShiftById(ID_1);

        // THEN
        verify(shiftRepository, never()).deleteById(any(UUID.class));
        assertFalse(deleted);
    }
}