package com.github.nieyo.service;

import com.github.nieyo.model.organisation.Organisation;
import com.github.nieyo.repository.OrganisationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrganisationServiceTest {

    OrganisationRepository organisationRepository = mock(OrganisationRepository.class);
    OrganisationService organisationService = new OrganisationService(organisationRepository);

    private Organisation organisation1;
    private Organisation organisation2;

    @BeforeEach
    void setUp() {
        organisation1 = new Organisation("1", "Org One", "admin1", List.of("event1", "event2"));
        organisation2 = new Organisation("2", "Org Two", "admin2", List.of("event3"));
    }

    @Test
    void findAll_shouldReturnAllOrganisations() {
        when(organisationRepository.findAll()).thenReturn(List.of(organisation1, organisation2));

        List<Organisation> result = organisationService.findAll();

        assertEquals(2, result.size());
        assertTrue(result.contains(organisation1));
        assertTrue(result.contains(organisation2));
        verify(organisationRepository, times(1)).findAll();
    }

    @Test
    void findById_existingId_shouldReturnOrganisation() {
        when(organisationRepository.findById("1")).thenReturn(Optional.of(organisation1));

        Optional<Organisation> result = organisationService.findById("1");

        assertTrue(result.isPresent());
        assertEquals("Org One", result.get().name());
        verify(organisationRepository, times(1)).findById("1");
    }

    @Test
    void findById_nonExistingId_shouldReturnEmpty() {
        when(organisationRepository.findById("99")).thenReturn(Optional.empty());

        Optional<Organisation> result = organisationService.findById("99");

        assertFalse(result.isPresent());
        verify(organisationRepository, times(1)).findById("99");
    }

    @Test
    void save_shouldSaveAndReturnOrganisation() {
        when(organisationRepository.save(organisation1)).thenReturn(organisation1);

        Organisation saved = organisationService.save(organisation1);

        assertNotNull(saved);
        assertEquals("Org One", saved.name());
        verify(organisationRepository, times(1)).save(organisation1);
    }

    @Test
    void deleteById_shouldCallRepositoryDelete() {
        doNothing().when(organisationRepository).deleteById("1");

        organisationService.deleteById("1");

        verify(organisationRepository, times(1)).deleteById("1");
    }
}