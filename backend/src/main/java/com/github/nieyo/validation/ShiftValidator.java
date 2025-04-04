package com.github.nieyo.validation;

import com.github.nieyo.model.Shift;
import com.github.nieyo.repository.ShiftRepository;
import com.github.nieyo.service.ClockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ShiftValidator {

    private final ShiftRepository shiftRepository;
    private final ClockService clockService;

    public void validateShift(Shift shift) {
       validateNotNull(shift);
       validateRequiredFields(shift);
       validateTimeOrder(shift);
       validateNonPastShift(shift);
       validateMinimumDuration(shift);
       validateNoOverlap(shift);
    }

    private void validateNotNull(Shift shift) {
        if (shift == null)
            throw new IllegalArgumentException("Shift cannot be null");
    }

    private void validateRequiredFields(Shift shift) {
        if (shift.startTime() == null || shift.endTime() == null)
            throw new IllegalArgumentException("startTime and endTime are required");
    }

    private void validateTimeOrder(Shift shift) {
        if (shift.startTime().isAfter(shift.endTime()))
            throw new IllegalArgumentException("Start must be before End");
    }

    private void validateNonPastShift(Shift shift) {
        Instant now = clockService.now();
        Instant buffer = now.plusSeconds(5);
        if (shift.startTime().isBefore(buffer))
            throw new IllegalArgumentException("Shift cannot be in the past");
    }

    private void validateMinimumDuration(Shift shift) {
        Duration minDuration = Duration.ofMinutes(15);
        Duration actualDuration = Duration.between(shift.startTime(), shift.endTime());
        if (actualDuration.compareTo(minDuration) < 0)
            throw new IllegalArgumentException("Shift must be at least " + minDuration.toMinutes() + " minutes long");
    }

    private void validateNoOverlap(Shift shift) {
        List<Shift> overlappingShifts = shiftRepository.findOverlappingShifts(shift.startTime(), shift.endTime());
        if (!overlappingShifts.isEmpty())
            throw new IllegalArgumentException("Shift overlaps with existing shifts");
    }

    // when events are implemented, check events are not in the past and shifts are inside event range
}
