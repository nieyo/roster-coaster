package com.github.nieyo.validation;

import com.github.nieyo.model.shift.Shift;
import com.github.nieyo.repository.ShiftRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ShiftValidator {

    private final ShiftRepository shiftRepository;
    private final Clock clock;

    public void validateShift(Shift shift) {
        validateNotNull(shift);
        validateRequiredFields(shift);
        validateTimeOrder(shift);
        validateNonPastShift(shift);
        validateMinimumDuration(shift);
        validateNoOverlap(shift);
    }

    private void validateNotNull(Shift shift) {
        if (shift == null) throw new IllegalArgumentException("Shift cannot be null");
    }

    private void validateRequiredFields(Shift shift) {
        if (shift.duration() == null)
            throw new IllegalArgumentException("startTime and endTime are required");
    }

    private void validateTimeOrder(Shift shift) {
        if (shift.duration().start().isAfter(shift.duration().end())) throw new IllegalArgumentException("Start must be before End");
    }

    private void validateNonPastShift(Shift shift) {
        Instant now = clock.instant();
        Instant buffer = now.plusSeconds(5);
        if (shift.duration().start().isBefore(buffer)) throw new IllegalArgumentException("Shift cannot be in the past");
    }

    private void validateMinimumDuration(Shift shift) {
        Duration minDuration = Duration.ofMinutes(15);
        Duration actualDuration = Duration.between(shift.duration().start(), shift.duration().end());
        if (actualDuration.compareTo(minDuration) < 0)
            throw new IllegalArgumentException("Shift must be at least " + minDuration.toMinutes() + " minutes long");
    }

    private void validateNoOverlap(Shift shift) {
        List<Shift> overlappingShifts;

        if (shift.id() == null || shift.id().isEmpty()) {
            overlappingShifts = shiftRepository.findOverlappingShifts(shift.duration().start(), shift.duration().end());
        } else {
            overlappingShifts = shiftRepository.findOverlappingShiftsExcludingSelf(shift.duration().start(), shift.duration().end(), shift.id());
        }

        if (!overlappingShifts.isEmpty()) {
            throw new IllegalArgumentException("Shift overlaps with existing shifts");
        }
    }

    // when events are implemented, check events are not in the past and shifts are inside event range
}
