package com.github.nieyo.controller;

import com.github.nieyo.model.organisation.Organisation;
import com.github.nieyo.service.OrganisationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/organisations")
@RequiredArgsConstructor
public class OrganisationController {

    private final OrganisationService organisationService;

    @GetMapping
    public List<Organisation> getAll() {
        return organisationService.findAll();
    }

    @GetMapping("/{id}")
    public Organisation getById(@PathVariable String id) {
        return organisationService.findById(id).orElse(null);
    }

    @PostMapping
    public Organisation create(@RequestBody Organisation organisation) {
        return organisationService.save(organisation);
    }

    @PutMapping("/{id}")
    public Organisation update(@PathVariable String id, @RequestBody Organisation organisation) {
        // Optional: Prüfen, ob id und organisation.id übereinstimmen
        return organisationService.save(organisation);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        organisationService.deleteById(id);
    }
}
