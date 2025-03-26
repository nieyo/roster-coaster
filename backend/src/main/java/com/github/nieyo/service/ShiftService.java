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
        return shiftRepository.save(shiftToSave);
    }
}
