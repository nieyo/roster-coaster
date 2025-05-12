package com.github.nieyo.service;

import com.github.nieyo.dto.OrganisationDTO;
import com.github.nieyo.entity.Organisation;
import com.github.nieyo.mapper.OrganisationMapper;
import com.github.nieyo.repository.OrganisationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrganisationService {

    private final OrganisationRepository organisationRepository;

    public List<OrganisationDTO> findAll() {
        return organisationRepository.findAll()
                .stream()
                .map(OrganisationMapper::toDto)
                .toList();
    }

    public Optional<OrganisationDTO> findById(UUID id) {
        return organisationRepository.findById(id).map(OrganisationMapper::toDto);
    }

    public OrganisationDTO save(OrganisationDTO organisationDTO) {
        Organisation organisation = OrganisationMapper.fromDto(organisationDTO);
        Organisation saved = organisationRepository.save(organisation);
        return OrganisationMapper.toDto(saved);
    }

    public OrganisationDTO save(UUID id, OrganisationDTO organisationDTO) {
        if (!id.equals(organisationDTO.id())) {
            throw new IllegalArgumentException("ID is not changeable");
        }

        if (!organisationRepository.existsById(id)) {
            throw new NoSuchElementException(String.format("shift not found with the id %s", id));
        }

        Organisation organisation = OrganisationMapper.fromDto(organisationDTO);
        Organisation saved = organisationRepository.save(organisation);
        return OrganisationMapper.toDto(saved);
    }



    public void deleteById(UUID id) {
        organisationRepository.deleteById(id);
    }
}
