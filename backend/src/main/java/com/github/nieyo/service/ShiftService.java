package com.github.nieyo.service;

import com.github.nieyo.model.Shift;
import com.github.nieyo.repository.ShiftRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShiftService {

    private final ShiftRepository shiftRepository;

    public Shift saveShift(Shift shiftToSave) {

        validateShift(shiftToSave);
        return shiftRepository.save(shiftToSave);
    }

    private void validateShift(Shift shiftToValidate) {
        if (shiftToValidate == null) {
            throw new IllegalArgumentException("Shift cannot be null");
        }

        if (shiftToValidate.startTime() == null || shiftToValidate.endTime() == null) {
            throw new IllegalArgumentException("startTime and endTime are required");
        }

        if (shiftToValidate.startTime().isAfter(shiftToValidate.endTime())) {
            throw new IllegalArgumentException("Start must be before End");
        }
    }
}
