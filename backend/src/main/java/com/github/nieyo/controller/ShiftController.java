package com.github.nieyo.controller;

import com.github.nieyo.dto.ShiftCreateDTO;
import com.github.nieyo.dto.ShiftDTO;
import com.github.nieyo.service.ShiftService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/shift")
@RequiredArgsConstructor
public class ShiftController {

    private final ShiftService shiftService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ShiftDTO saveShift(@Valid @RequestBody ShiftCreateDTO shiftToSave) {
        return shiftService.saveShift(shiftToSave);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ShiftDTO> getShifts() {
        return shiftService.getShifts();
    }

    @GetMapping("{id}")
    public ShiftDTO getShiftById(@PathVariable UUID id)
    {
        Optional<ShiftDTO> shift = shiftService.getShiftById(id);
        if(shift.isPresent()) {
            return shift.get();
        }
        throw new NoSuchElementException("Shift with ID: "+ id + " not found");
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public ShiftDTO updateShift(@PathVariable UUID id, @Valid @RequestBody ShiftDTO shiftToUpdate) {
        return shiftService.updateShift(id, shiftToUpdate);
    }

    @DeleteMapping("{id}")
    public void deleteShiftById(@PathVariable UUID id) {
        boolean isDeleted = shiftService.deleteShiftById(id);
        if (!isDeleted)
        {
            throw new NoSuchElementException("Shift with ID: " + id + " not found");
        }
    }
}
