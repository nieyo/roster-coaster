package com.github.nieyo.service;

import com.github.nieyo.dto.OrganisationDTO;
import com.github.nieyo.entity.Organisation;
import com.github.nieyo.mapper.OrganisationMapper;
import com.github.nieyo.repository.OrganisationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrganisationServiceTest {

    OrganisationRepository organisationRepository = mock(OrganisationRepository.class);
    OrganisationService organisationService = new OrganisationService(organisationRepository);

    private Organisation organisation1;
    private Organisation organisation2;

    private static final UUID ORG1_ID = UUID.fromString("100e4567-e89b-12d3-a456-426614174000");
    private static final UUID ORG2_ID = UUID.fromString("200e4567-e89b-12d3-a456-426614174000");

    private static final UUID EVENT1_ID = UUID.fromString("123e4567-e89b-12d3-a456-426614174001");
    private static final UUID EVENT2_ID = UUID.fromString("123e4567-e89b-12d3-a456-426614174002");
    private static final UUID EVENT3_ID = UUID.fromString("123e4567-e89b-12d3-a456-426614174003");


    @BeforeEach
    void setUp() {
        organisation1 = new Organisation(ORG1_ID, "Org One", "admin1", List.of(EVENT1_ID, EVENT2_ID));
        organisation2 = new Organisation(ORG2_ID, "Org Two", "admin2", List.of(EVENT3_ID));
    }

    @Test
    void findAll_shouldReturnAllOrganisations() {
        // GIVEN
        when(organisationRepository.findAll()).thenReturn(List.of(organisation1, organisation2));

        OrganisationDTO dto1 = OrganisationMapper.toDto(organisation1);
        OrganisationDTO dto2 = OrganisationMapper.toDto(organisation2);

        // WHEN
        List<OrganisationDTO> result = organisationService.findAll();

        // THEN
        assertEquals(2, result.size());
        assertTrue(result.contains(dto1));
        assertTrue(result.contains(dto2));
        verify(organisationRepository, times(1)).findAll();
    }


    @Test
    void findById_existingId_shouldReturnOrganisation() {
        when(organisationRepository.findById(ORG1_ID)).thenReturn(Optional.of(organisation1));

        Optional<OrganisationDTO> result = organisationService.findById(ORG1_ID);

        assertTrue(result.isPresent());
        assertEquals("Org One", result.get().name());
        verify(organisationRepository, times(1)).findById(ORG1_ID);
    }

    @Test
    void findById_nonExistingId_shouldReturnEmpty() {
        when(organisationRepository.findById(ORG1_ID)).thenReturn(Optional.empty());

        Optional<OrganisationDTO> result = organisationService.findById(ORG1_ID);

        assertFalse(result.isPresent());
        verify(organisationRepository, times(1)).findById(ORG1_ID);
    }

    @Test
    void save_shouldSaveAndReturnOrganisation() {
        // GIVEN
        OrganisationDTO dto = OrganisationMapper.toDto(organisation1);
        when(organisationRepository.save(any(Organisation.class))).thenReturn(organisation1);

        // WHEN
        OrganisationDTO saved = organisationService.save(dto);

        // THEN
        assertNotNull(saved);
        assertEquals("Org One", saved.name());
        assertEquals(organisation1.id(), saved.id());
        verify(organisationRepository, times(1)).save(any(Organisation.class));
    }

    @Test
    void deleteById_shouldCallRepositoryDelete() {
        doNothing().when(organisationRepository).deleteById(ORG1_ID);

        organisationService.deleteById(ORG1_ID);

        verify(organisationRepository, times(1)).deleteById(ORG1_ID);
    }
}