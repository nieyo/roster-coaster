package com.github.nieyo.service;

import com.github.nieyo.model.shift.*;
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
    ShiftDurationDTO durationDTO = new ShiftDurationDTO(startTime.toString(), endTime.toString());
    List<ShiftSignup> signups = List.of();


    // CREATE
    @Test
    void saveShift_ShouldPersistNewEntity() {

        // GIVEN
        CreateShiftDTO inputShift = CreateShiftDTO.builder()
                .duration(durationDTO)
                .signups(List.of())
                .build();

        String expectedId = "generated-id";
        when(idService.randomId()).thenReturn(expectedId);

        Shift expected = Shift.builder()
                .id(expectedId)
                .duration(duration)
                .signups(signups)
                .build();

        when(shiftRepository.save(any(Shift.class))).thenReturn(expected);

        // WHEN
        Shift actual = shiftService.saveShift(inputShift);

        // THEN
        verify(idService).randomId();
        verify(shiftRepository).save(any(Shift.class));
        assertNotNull(actual);
        assertEquals(expected.id(), actual.id());
        assertEquals(expected.duration().start(), actual.duration().start());
        assertEquals(expected.duration().end(), actual.duration().end());
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
        CreateShiftDTO invalidShiftNoStart = CreateShiftDTO.builder()
                .duration(
                        ShiftDurationDTO.builder()
                                .start(null)
                                .end(endTime.toString())
                                .build()
                )
                .signups(List.of())
                .build();

        CreateShiftDTO invalidShiftNoEnd = CreateShiftDTO.builder()
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
        CreateShiftDTO invalidShift = CreateShiftDTO.builder()
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
        List<Shift> expected = List.of();
        when(shiftRepository.findAll()).thenReturn(expected);
        // WHEN
        List<Shift> actual = shiftService.getShifts();
        // THEN
        verify(shiftRepository).findAll();
        assertEquals(expected, actual);
    }

    @Test
    void getShifts_whenNotEmpty_returnShiftList() {
        // GIVEN
        List<Shift> expected = List.of(
                Shift.builder()
                        .id("1")
                        .duration(duration)
                        .signups(signups)
                        .build(),
                Shift.builder()
                        .id("2")
                        .duration(duration)
                        .signups(signups)
                        .build(),
                Shift.builder()
                        .id("3")
                        .duration(duration)
                        .signups(signups)
                        .build()
        );
        when(shiftRepository.findAll()).thenReturn(expected);
        // WHEN
        List<Shift> actual = shiftService.getShifts();
        // THEN
        verify(shiftRepository).findAll();
        assertEquals(expected, actual);
    }

    // GET BY ID
    @Test
    void getShiftById_whenIdExists_returnShift() {
        // GIVEN
        String existingId = "idToSearchFor";
        Optional<Shift> expected = Optional.of(
                Shift.builder()
                        .id(existingId)
                        .duration(duration)
                        .signups(signups)
                        .build()
        );
        when(shiftRepository.findById(existingId)).thenReturn(expected);

        // WHEN
        Optional<Shift> actual = shiftService.getShiftById(existingId);

        // THEN
        verify(shiftRepository).findById(existingId);
        assertEquals(expected, actual);
    }

    @Test
    void getShiftById_whenIdDoesNotExists_returnEmptyOptional() {
        // GIVEN
        String nonExistingId = "idToSearchFor";
        Optional<Shift> expected = Optional.empty();
        when(shiftRepository.findById(nonExistingId)).thenReturn(expected);

        // WHEN
        Optional<Shift> actual = shiftService.getShiftById(nonExistingId);

        // THEN
        verify(shiftRepository).findById(nonExistingId);
        assertEquals(expected, actual);
    }

    // UPDATE
    @Test
    void updateShift_whenFound_returnShift() {
        // GIVEN
        String targetId = "2";
        Shift expected = Shift.builder()
                .id("2")
                .duration(duration.withEnd(endTime.plusSeconds(3600)))
                .signups(signups)
                .build();
        when(shiftRepository.existsById(targetId)).thenReturn(true);
        when(shiftRepository.save(expected)).thenReturn(expected);

        // WHEN
        Shift actual = shiftService.updateShift(targetId, expected);

        // THEN
        verify(shiftRepository).existsById(targetId);
        verify(shiftRepository).save(expected);
        assertEquals(expected.id(), actual.id());
        assertEquals(expected.duration().start(), actual.duration().start());
        assertEquals(expected.duration().end(), actual.duration().end());
        assertEquals(expected.signups(), actual.signups());
    }

    @Test
    void updateShift_whenNotFound_throwNoSuchElementException() {
        String targetId = "3";
        Shift updatedShift = Shift.builder()
                .id(targetId)
                .duration(duration)
                .signups(signups)
                .build();
        when(shiftRepository.existsById(targetId)).thenReturn(false);

        // WHEN + THEN
        assertThrows(NoSuchElementException.class, () -> shiftService.updateShift(targetId, updatedShift));
        verify(shiftRepository, never()).save(any());
    }

    @Test
    void updateShift_whenIdDoesNotMatch_throwIllegalArgumentException() {
        // GIVEN
        String targetId = "3";
        Shift updatedShift = Shift.builder()
                .id("notMatchingId")
                .duration(duration)
                .signups(signups)
                .build();
        when(shiftRepository.existsById(targetId)).thenReturn(true);
        when(shiftRepository.save(updatedShift)).thenReturn(updatedShift);

        // WHEN + THEN
        assertThrows(IllegalArgumentException.class, () -> shiftService.updateShift(targetId, updatedShift));
        verify(shiftRepository, never()).save(any());
    }

    // DELETE
    @Test
    void deleteShift_whenFound_deleteShift() {
        // GIVEN
        when(shiftRepository.existsById("1")).thenReturn(true);

        // WHEN
        boolean deleted = shiftService.deleteShiftById("1");

        // THEN
        verify(shiftRepository).deleteById("1");
        assertTrue(deleted);
    }

    @Test
    void deleteShift_whenNotFound_returnFalse() {
        // GIVEN
        when(shiftRepository.existsById("1")).thenReturn(false);

        // WHEN
        boolean deleted = shiftService.deleteShiftById("1");

        // THEN
        verify(shiftRepository, never()).deleteById(anyString());
        assertFalse(deleted);
    }
}