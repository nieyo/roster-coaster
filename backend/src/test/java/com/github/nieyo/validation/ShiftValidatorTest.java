package com.github.nieyo.validation;

import com.github.nieyo.model.Shift;
import com.github.nieyo.model.User;
import com.github.nieyo.repository.ShiftRepository;
import com.github.nieyo.service.ClockService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ShiftValidatorTest {

    ShiftRepository shiftRepository = mock(ShiftRepository.class);
    ClockService clockService = mock(ClockService.class);

    ShiftValidator shiftValidator = new ShiftValidator(shiftRepository, clockService);

    Instant now = Instant.parse("2025-03-25T00:00:00Z");
    Instant startTime = now.plus(Duration.ofMinutes(30));
    Instant endTime = startTime.plus(Duration.ofMinutes(30));
    List<User> participants = List.of();

    @BeforeEach
    void setUp() {
        when(clockService.now()).thenReturn(now);
    }

    @Test
    void validateShift_doesNotThrowException_whenShiftIsValid() {
        Shift validShift = new Shift(null, startTime, endTime, participants);
        assertDoesNotThrow(() -> shiftValidator.validateShift(validShift));
    }

    @Test
    void validateShift_throwsException_whenShiftIsNull() {
        assertThrows(IllegalArgumentException.class, () -> shiftValidator.validateShift(null));
    }

    @Test
    void validateShift_throwsException_whenRequiredFieldIsNull() {
        Shift invalidShiftNoStart = new Shift(null, null, endTime, List.of());
        Shift invalidShiftNoEnd = new Shift(null, startTime, null, List.of());

        assertThrows(IllegalArgumentException.class, () -> shiftValidator.validateShift(invalidShiftNoStart));
        assertThrows(IllegalArgumentException.class, () -> shiftValidator.validateShift(invalidShiftNoEnd));
    }

    @Test
    void validateShift_throwsException_whenEndTimeBeforeStartTime() {
        Shift invalidShift = new Shift(null, endTime, startTime, List.of());

        assertThrows(IllegalArgumentException.class, () -> shiftValidator.validateShift(invalidShift));
    }

    @Test
    void validateShift_throwsException_whenStartTimeIsInThePast() {
        Shift shiftInPast = new Shift(null, now.minus(Duration.ofDays(1)), endTime, List.of());
        assertThrows(IllegalArgumentException.class, () -> shiftValidator.validateShift(shiftInPast));
    }

    @Test
    void validateShift_throwsException_whenDurationIsUnderMinimum() {
        Shift shiftWithShortDuration = new Shift(
                null,
                startTime,
                endTime.minus(Duration.ofMinutes(25)),
                List.of()
        );

        assertThrows(IllegalArgumentException.class, () -> shiftValidator.validateShift(shiftWithShortDuration));
    }

    @Test
    void validateShift_throwsException_whenShiftOverlapsWithExistingShift() {
        Shift overlappingShift = new Shift(null, startTime, endTime, List.of());

        when(shiftRepository.findOverlappingShifts(startTime, endTime))
                .thenReturn(List.of(new Shift("existingId", startTime, endTime, List.of())));

        assertThrows(IllegalArgumentException.class, () -> shiftValidator.validateShift(overlappingShift));
        verify(shiftRepository).findOverlappingShifts(startTime, endTime);
    }
}