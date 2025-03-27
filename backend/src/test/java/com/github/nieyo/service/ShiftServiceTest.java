package com.github.nieyo.service;

import com.github.nieyo.model.Shift;
import com.github.nieyo.model.User;
import com.github.nieyo.repository.ShiftRepository;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ShiftServiceTest {

    ShiftRepository shiftRepository = mock(ShiftRepository.class);
    IdService idService = mock(IdService.class);
    ShiftService shiftService = new ShiftService(shiftRepository, idService);


    Instant startTime = Instant.parse("2025-03-26T12:00:00Z");
    Instant endTime = Instant.parse("2025-03-26T14:00:00Z");
    Map<String, User> participants = Map.of();


    @Test
    void saveShift_ShouldPersistNewEntity() {

        // GIVEN
        Shift inputShift = new Shift(null, startTime, endTime, Map.of());

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
        Shift invalidShiftNoStart = new Shift(null, null, Instant.now(), Map.of());
        Shift invalidShiftNoEnd = new Shift(null, startTime, null, Map.of());

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
                Map.of()
        );

        assertThrows(IllegalArgumentException.class, () -> shiftService.saveShift(invalidShift));

        // Verify repository was never called
        verify(shiftRepository, never()).save(any());
    }

}