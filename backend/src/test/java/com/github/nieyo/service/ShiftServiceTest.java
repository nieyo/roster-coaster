package com.github.nieyo.service;

import com.github.nieyo.model.Shift;
import com.github.nieyo.model.User;
import com.github.nieyo.repository.ShiftRepository;
import com.github.nieyo.validation.ShiftValidator;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ShiftServiceTest {

    ShiftRepository shiftRepository = mock(ShiftRepository.class);
    IdService idService = mock(IdService.class);
    ShiftValidator shiftValidator = mock(ShiftValidator.class);
    ShiftService shiftService = new ShiftService(shiftRepository, idService, shiftValidator);

    Instant startTime = Instant.parse("2025-03-26T12:00:00Z");
    Instant endTime = Instant.parse("2025-03-26T14:00:00Z");
    List<User> participants = List.of();

    // CREATE
    @Test
    void saveShift_ShouldPersistNewEntity() {

        // GIVEN
        Shift inputShift = new Shift(null, startTime, endTime, List.of());

        String expectedId = "generated-id";
        when(idService.randomId()).thenReturn(expectedId);

        Shift expectedShift = new Shift(expectedId, startTime, endTime, participants);
        when(shiftRepository.save(any(Shift.class))).thenReturn(expectedShift);

        // WHEN
        Shift result = shiftService.saveShift(inputShift);

        // THEN
        verify(idService).randomId();
        verify(shiftRepository).save(any(Shift.class));
        assertNotNull(result);
        assertEquals(expectedId, result.id());
        assertEquals(startTime, result.startTime());
        assertEquals(endTime, result.endTime());
        assertEquals(participants, result.participants());
    }

    @Test
    void saveShift_ShouldThrowException_WhenShiftIsNull() {
        // WHEN & THEN
        assertThrows(IllegalArgumentException.class, () -> shiftService.saveShift(null));

        // Verify repository was never called
        verify(shiftRepository, never()).save(any());
    }

    @Test
    void saveShift_ShouldThrowException_WhenRequiredFieldIsNull() {
        Shift invalidShiftNoStart = new Shift(null, null, Instant.now(), List.of());
        Shift invalidShiftNoEnd = new Shift(null, startTime, null, List.of());

        assertThrows(IllegalArgumentException.class, () -> shiftService.saveShift(invalidShiftNoStart));
        assertThrows(IllegalArgumentException.class, () -> shiftService.saveShift(invalidShiftNoEnd));

        // Verify repository was never called
        verify(shiftRepository, never()).save(any());
    }

    @Test
    void saveShift_ShouldThrowException_WhenEndTimeBeforeStartTime() {
        Shift invalidShift = new Shift(
                null,
                endTime,
                startTime, // wrong order
                List.of()
        );

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
                new Shift("1", startTime, endTime, participants),
                new Shift("2", startTime, endTime, participants),
                new Shift("3", startTime, endTime, participants)
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
        Optional<Shift> expected = Optional.of(new Shift(existingId, startTime, endTime, participants));
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
        Shift expected = new Shift("2", startTime, endTime.plusSeconds(3600), participants);
        when(shiftRepository.existsById(targetId)).thenReturn(true);
        when(shiftRepository.save(expected)).thenReturn(expected);

        // WHEN
        Shift actual = shiftService.updateShift(targetId, expected);

        // THEN
        verify(shiftRepository).existsById(targetId);
        verify(shiftRepository).save(expected);
        assertEquals(expected.id(), actual.id());
        assertEquals(expected.startTime(), actual.startTime());
        assertEquals(expected.endTime(), actual.endTime());
        assertEquals(expected.participants(), actual.participants());
    }

    @Test
    void updateShift_whenNotFound_throwNoSuchElementException() {
        String targetId = "3";
        Shift updatedShift = new Shift(targetId, startTime, endTime, participants);
        when(shiftRepository.existsById(targetId)).thenReturn(false);

        // WHEN + THEN
        assertThrows(NoSuchElementException.class, () -> shiftService.updateShift(targetId, updatedShift));
        verify(shiftRepository, never()).save(any());
    }

    @Test
    void updateShift_whenIdDoesNotMatch_throwIllegalArgumentException() {
        // GIVEN
        String targetId = "3";
        Shift updatedShift = new Shift("notMatchingId", startTime, endTime, participants);
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