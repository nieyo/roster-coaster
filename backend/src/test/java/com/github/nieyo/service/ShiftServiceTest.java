package com.github.nieyo.service;

import com.github.nieyo.model.Shift;
import com.github.nieyo.repository.ShiftRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ShiftServiceTest {

    ShiftRepository shiftRepository = mock(ShiftRepository.class);
    ShiftService shiftService = new ShiftService(shiftRepository);

    @Test
    void save_ShouldPersistNewEntity() {
        // GIVEN
        Shift expected = new Shift(
                null, // ID ist zunächst null (wird erst generiert)
                Instant.parse("2025-03-26T12:00:00Z"),
                Instant.parse("2025-03-26T14:00:00Z"),
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
        Shift result = shiftService.saveShift(expected);

        // THEN
        verify(shiftRepository).save(expected);

        assertEquals(expectedId, result.id()); // ID wurde zugewiesen
        assertEquals(expected.startTime(), result.startTime()); // Daten bleiben konsistent
    }

}