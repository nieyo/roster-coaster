package com.github.nieyo.controller;

import com.github.nieyo.model.shift.CreateShiftDTO;
import com.github.nieyo.model.shift.Shift;
import com.github.nieyo.service.ShiftService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/api/shift")
@RequiredArgsConstructor
public class ShiftController {

    private final ShiftService shiftService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Shift saveShift(@Valid @RequestBody CreateShiftDTO shiftToSave) {
        return shiftService.saveShift(shiftToSave);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Shift> getShifts() {
        return shiftService.getShifts();
    }

    @GetMapping("{id}")
    public Shift getShiftById(@PathVariable String id)
    {
        Optional<Shift> shift = shiftService.getShiftById(id);
        if(shift.isPresent()) {
            return shift.get();
        }
        throw new NoSuchElementException("Shift with ID: "+ id + " not found");
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public Shift updateShift(@PathVariable String id, @Valid @RequestBody Shift shiftToUpdate) {
        return shiftService.updateShift(id, shiftToUpdate);
    }

    @DeleteMapping("{id}")
    public void deleteShiftById(@PathVariable String id) {
        boolean isDeleted = shiftService.deleteShiftById(id);
        if (!isDeleted)
        {
            throw new NoSuchElementException("Shift with ID: " + id + " not found");
        }
    }
}
