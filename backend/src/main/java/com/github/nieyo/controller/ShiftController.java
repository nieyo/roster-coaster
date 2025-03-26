package com.github.nieyo.controller;

import com.github.nieyo.model.Shift;
import com.github.nieyo.service.ShiftService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/shift")
@RequiredArgsConstructor
public class ShiftController {

    private final ShiftService shiftService;

    @PostMapping
    public Shift saveShift(@RequestBody Shift shiftToSave) {
        return shiftService.saveShift(shiftToSave);
    }

}
