package com.github.nieyo.controller;

import com.github.nieyo.config.FixedClockConfig;
import com.github.nieyo.entity.Organisation;
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
import java.util.UUID;

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

    private static final String ORG1_ID_STR   = "11111111-1111-1111-1111-111111111111";
    private static final String ORG2_ID_STR   = "22222222-2222-2222-2222-222222222222";
    private static final String EVENT1_ID_STR = "33333333-3333-3333-3333-333333333333";
    private static final String EVENT2_ID_STR = "44444444-4444-4444-4444-444444444444";

    private static final UUID ORG1_ID   = UUID.fromString(ORG1_ID_STR);
    private static final UUID ORG2_ID   = UUID.fromString(ORG2_ID_STR);
    private static final UUID EVENT1_ID = UUID.fromString(EVENT1_ID_STR);
    private static final UUID EVENT2_ID = UUID.fromString(EVENT2_ID_STR);



    @BeforeEach
    void setUp() {
        organisationRepository.deleteAll();
    }

    @Test
    void createAndGetOrganisation() throws Exception {
        String requestBody = """
                {
                  "id": "%s",
                  "name": "Test Org",
                  "adminUserId": "admin1",
                  "eventIds": ["%s", "%s"]
                }
                """.formatted(ORG1_ID, EVENT1_ID, EVENT2_ID);

        // Create
        mvc.perform(post("/api/organisations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Org"));

        // Get by ID
        mvc.perform(get("/api/organisations/" + ORG1_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Org"));

        // Get all
        mvc.perform(get("/api/organisations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void updateOrganisation() throws Exception {
        Organisation org = new Organisation(ORG2_ID, "Org Alt", "admin2", List.of());
        organisationRepository.save(org);

        String updated = """
                {
                  "id": "%s",
                  "name": "Org Neu",
                  "adminUserId": "admin2",
                  "eventIds": []
                }
                """.formatted(ORG2_ID);

        mvc.perform(put("/api/organisations/" + ORG2_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updated))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Org Neu"));
    }

    @Test
    void deleteOrganisation() throws Exception {
        Organisation org = new Organisation(ORG2_ID, "ToDelete", "admin2", List.of());
        organisationRepository.save(org);

        mvc.perform(delete("/api/organisations/" + ORG2_ID))
                .andExpect(status().isOk());

        assertEquals(Optional.empty(), organisationRepository.findById(ORG2_ID));
    }

    @Test
    void getById_whenNotFound_returns404() throws Exception {
        mvc.perform(get("/api/organisations/" + ORG1_ID))
                .andExpect(status().isNotFound());
    }

}