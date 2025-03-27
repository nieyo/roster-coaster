package com.github.nieyo.controller;

import com.github.nieyo.model.Shift;
import com.github.nieyo.service.ShiftService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/shift")
@RequiredArgsConstructor
public class ShiftController {

    private final ShiftService shiftService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Shift saveShift(@RequestBody Shift shiftToSave) {
        return shiftService.saveShift(shiftToSave);
    }

}
