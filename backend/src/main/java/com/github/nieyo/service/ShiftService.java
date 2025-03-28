package com.github.nieyo.service;

import com.github.nieyo.model.Shift;
import com.github.nieyo.repository.ShiftRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShiftService {

    private final ShiftRepository shiftRepository;
    private final IdService idService;

    public Shift saveShift(Shift shiftToSave) {

        validateShift(shiftToSave);
        String id = idService.randomId();
        shiftToSave = new Shift(id, shiftToSave.startTime(), shiftToSave.endTime(), shiftToSave.participants());
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

    public List<Shift> getShifts() {
        return shiftRepository.findAll();
    }
}
