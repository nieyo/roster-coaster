package com.github.nieyo.controller;

import com.github.nieyo.config.FixedClockConfig;
import com.github.nieyo.model.organisation.Organisation;
import com.github.nieyo.repository.OrganisationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = FixedClockConfig.class)
@AutoConfigureMockMvc(addFilters = false) // disable authentication
class OrganisationControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private OrganisationRepository organisationRepository;

    @BeforeEach
    void setUp() {
        organisationRepository.deleteAll();
    }

    @Test
    void createAndGetOrganisation() throws Exception {
        String requestBody = """
                {
                  "id": "1",
                  "name": "Test Org",
                  "adminUserId": "admin1",
                  "eventIds": ["event1", "event2"]
                }
                """;

        // Create
        mvc.perform(post("/api/organisations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Org"));

        // Get by ID
        mvc.perform(get("/api/organisations/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Org"));

        // Get all
        mvc.perform(get("/api/organisations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void updateOrganisation() throws Exception {
        Organisation org = new Organisation("2", "Org Alt", "admin2", List.of());
        organisationRepository.save(org);

        String updated = """
                {
                  "id": "2",
                  "name": "Org Neu",
                  "adminUserId": "admin2",
                  "eventIds": []
                }
                """;

        mvc.perform(put("/api/organisations/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updated))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Org Neu"));
    }

    @Test
    void deleteOrganisation() throws Exception {
        Organisation org = new Organisation("3", "ToDelete", "admin3", List.of());
        organisationRepository.save(org);

        mvc.perform(delete("/api/organisations/3"))
                .andExpect(status().isOk());

        assertEquals(Optional.empty(), organisationRepository.findById("3"));
    }

    @Test
    void getById_whenNotFound_returnsNull() throws Exception {
        mvc.perform(get("/api/organisations/doesnotexist"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }
}