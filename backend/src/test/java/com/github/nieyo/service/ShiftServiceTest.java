package com.github.nieyo.service;

import com.github.nieyo.model.Shift;
import com.github.nieyo.model.User;
import com.github.nieyo.repository.ShiftRepository;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ShiftServiceTest {

    ShiftRepository shiftRepository = mock(ShiftRepository.class);
    IdService idService = mock(IdService.class);
    ShiftService shiftService = new ShiftService(shiftRepository, idService);


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

    // READ ALL
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

    @Test
    void getShiftById_whenIdExists_returnShift(){
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
    void getShiftById_whenIdDoesNotExists_returnEmptyOptional(){
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


}