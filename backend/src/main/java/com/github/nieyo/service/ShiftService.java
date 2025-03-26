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

        if (shiftToSave == null){
            throw new IllegalArgumentException("Shift cannot be null");
        }

        if (shiftToSave.startTime() == null || shiftToSave.endTime() == null) {
            throw new IllegalArgumentException("startTime and endTime are required");
        }

        if (shiftToSave.startTime().isAfter(shiftToSave.endTime())) {
            throw new IllegalArgumentException("Start must be before End");
        }

        return shiftRepository.save(shiftToSave);
    }
}
