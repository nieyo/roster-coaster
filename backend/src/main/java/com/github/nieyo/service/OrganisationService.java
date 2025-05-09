package com.github.nieyo.service;

import com.github.nieyo.model.organisation.Organisation;
import com.github.nieyo.repository.OrganisationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrganisationService {

    private final OrganisationRepository organisationRepository;

    public List<Organisation> findAll() {
        return organisationRepository.findAll();
    }

    public Optional<Organisation> findById(String id) {
        return organisationRepository.findById(id);
    }

    public Organisation save(Organisation organisation) {
        return organisationRepository.save(organisation);
    }

    public void deleteById(String id) {
        organisationRepository.deleteById(id);
    }

}
