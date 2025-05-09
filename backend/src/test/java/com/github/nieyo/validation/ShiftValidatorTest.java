package com.github.nieyo.validation;

import com.github.nieyo.entity.Shift;
import com.github.nieyo.entity.ShiftDuration;
import com.github.nieyo.entity.ShiftSignup;
import com.github.nieyo.repository.ShiftRepository;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ShiftValidatorTest {

    ShiftRepository shiftRepository = mock(ShiftRepository.class);
    private final Clock clock = Clock.fixed(Instant.parse("2025-04-01T00:00:00Z"), ZoneOffset.UTC);

    ShiftValidator shiftValidator = new ShiftValidator(shiftRepository, clock);

    Instant now = clock.instant();
    Instant startTime = now.plus(Duration.ofMinutes(15));
    Instant endTime = startTime.plus(Duration.ofMinutes(30));
    ShiftDuration duration = new ShiftDuration(startTime, endTime);

    List<ShiftSignup> signups = List.of();

    @Test
    void validateShift_doesNotThrowException_whenShiftIsValid() {
        Shift validShift = Shift.builder()
                .id(null)
                .duration(duration)
                .signups(signups)
                .build();
        assertDoesNotThrow(() -> shiftValidator.validateShift(validShift));
    }

    @Test
    void validateShift_throwsException_whenShiftIsNull() {
        assertThrows(IllegalArgumentException.class, () -> shiftValidator.validateShift(null));
    }

    @Test
    void validateShift_throwsException_whenRequiredFieldIsNull() {
        Shift invalidShiftNoStart = Shift.builder()
                .id(null)
                .duration(duration.withStart(null))
                .signups(List.of())
                .build();
        Shift invalidShiftNoEnd = Shift.builder()
                .id(null)
                .duration(duration.withEnd(null))
                .signups(List.of())
                .build();

        assertThrows(NullPointerException.class, () -> shiftValidator.validateShift(invalidShiftNoStart));
        assertThrows(NullPointerException.class, () -> shiftValidator.validateShift(invalidShiftNoEnd));
    }

    @Test
    void validateShift_throwsException_whenEndTimeBeforeStartTime() {
        Shift invalidShift = Shift.builder()
                .id(null)
                .duration(duration.withStart(endTime).withEnd(startTime))
                .signups(List.of())
                .build();

        assertThrows(IllegalArgumentException.class, () -> shiftValidator.validateShift(invalidShift));
    }

    @Test
    void validateShift_throwsException_whenStartTimeIsInThePast() {
        Shift shiftInPast = Shift.builder()
                .id(null)
                .duration(duration.withStart(now.minus(Duration.ofDays(1))))
                .signups(List.of())
                .build();
        assertThrows(IllegalArgumentException.class, () -> shiftValidator.validateShift(shiftInPast));
    }

    @Test
    void validateShift_throwsException_whenDurationIsUnderMinimum() {
        Shift shiftWithShortDuration = Shift.builder()
                .id(null)
                .duration(duration.withEnd(endTime.minus(Duration.ofMinutes(25))))
                .signups(List.of())
                .build();

        assertThrows(IllegalArgumentException.class, () -> shiftValidator.validateShift(shiftWithShortDuration));
    }

    @Test
    void validateShift_throwsException_whenShiftOverlapsWithExistingShift() {
        Shift overlappingShift = Shift.builder()
                .id(null)
                .duration(duration)
                .signups(List.of())
                .build();

        when(shiftRepository.findOverlappingShifts(startTime, endTime))
                .thenReturn(List.of(
                        Shift.builder()
                                .id(UUID.fromString("11111111-1111-1111-1111-111111111111"))
                                .duration(duration)
                                .signups(List.of())
                                .build()
                ));

        assertThrows(IllegalArgumentException.class, () -> shiftValidator.validateShift(overlappingShift));
        verify(shiftRepository).findOverlappingShifts(startTime, endTime);
    }
}
