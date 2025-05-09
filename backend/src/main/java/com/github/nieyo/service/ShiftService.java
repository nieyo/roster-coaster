package com.github.nieyo.service;

import com.github.nieyo.model.shift.CreateShiftDTO;
import com.github.nieyo.model.shift.Shift;
import com.github.nieyo.model.shift.ShiftMapper;
import com.github.nieyo.repository.ShiftRepository;
import com.github.nieyo.validation.ShiftValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShiftService {

    private final ShiftRepository shiftRepository;
    private final IdService idService;
    private final ShiftValidator shiftValidator;

    public Shift saveShift(CreateShiftDTO createShiftDTO) {
        String id = idService.randomId();
        Shift shiftToSave = ShiftMapper.toShift(createShiftDTO, id);
        shiftValidator.validateShift(shiftToSave);
        return shiftRepository.save(shiftToSave);
    }

    public List<Shift> getShifts() {
        return shiftRepository.findAll();
    }

    public Optional<Shift> getShiftById(String id) {
        return shiftRepository.findById(id);
    }

    public Shift updateShift(String id, Shift shiftToUpdate) {
        if (!shiftRepository.existsById(id)) {
            throw new NoSuchElementException(String.format("shift not found with the id %s", id));
        }
        if (!id.equals(shiftToUpdate.id())) {
            throw new IllegalArgumentException("ID is not changeable");
        }
        shiftValidator.validateShift(shiftToUpdate);
        return shiftRepository.save(shiftToUpdate);
    }

    public boolean deleteShiftById(String id) {
        if (shiftRepository.existsById(id)) {
            shiftRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
