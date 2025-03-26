package com.github.nieyo.service;

import com.github.nieyo.model.Shift;
import com.github.nieyo.repository.ShiftRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ShiftServiceTest {

    ShiftRepository shiftRepository = mock(ShiftRepository.class);
    ShiftService shiftService = new ShiftService(shiftRepository);

    Instant startTime = Instant.parse("2025-03-26T12:00:00Z");
    Instant endTime = Instant.parse("2025-03-26T14:00:00Z");

    @Test
    void saveShift_ShouldPersistNewEntity() {
        // GIVEN
        Shift expected = new Shift(
                null, // ID ist zunächst null (wird erst generiert)
                startTime,
                endTime,
                Map.of()
        );

        ObjectId expectedId = new ObjectId();
        Shift savedShift = new Shift(
                expectedId, // Simuliere die generierte ID
                expected.startTime(),
                expected.endTime(),
                expected.participants()
        );

        when(shiftRepository.save(expected)).thenReturn(savedShift);

        // WHEN
        Shift actual = shiftService.saveShift(expected);

        // THEN
        verify(shiftRepository).save(expected);

        assertEquals(expectedId, actual.id());
        assertEquals(expected.startTime(), actual.startTime());
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