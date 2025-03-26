package com.github.nieyo.controller;

import com.github.nieyo.model.Shift;
import com.github.nieyo.repository.ShiftRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ShiftControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ShiftRepository shiftRepository;

    @BeforeEach
    void setUp() {
        shiftRepository.deleteAll();
    }

    @Test
    void saveShift_shouldPersistShift() throws Exception {
        // Given
        String requestBody = """
                {
                    "startTime": "2025-03-26T12:00:00Z",
                    "endTime": "2025-03-26T14:00:00Z",
                    "participants": {}
                }
                """;

        // When
        mvc.perform(post("/api/shift")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))

                // Then (HTTP layer)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.startTime").value("2025-03-26T12:00:00Z"))
                .andExpect(jsonPath("$.endTime").value("2025-03-26T14:00:00Z"))
                .andExpect(jsonPath("$.participants").isMap())
                .andExpect(jsonPath("$.participants").isEmpty());

        // Verify database state
        List<Shift> shifts = shiftRepository.findAll();
        assertEquals(1, shifts.size());
        assertEquals("2025-03-26T12:00:00Z", shifts.getFirst().startTime().toString());
    }

    @Test
    void saveShift_shouldRejectInvalidTimeRange() throws Exception {
        String invalidBody = """
                {
                    "startTime": "2025-03-26T14:00:00Z",
                    "endTime": "2025-03-26T12:00:00Z",
                    "participants": {}
                }
                """;

        mvc.perform(post("/api/shift")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").value("Ein Fehler ist aufgetreten: Start must be before End"));

        assertEquals(0, shiftRepository.findAll().size());
    }

    @Test
    void saveShift_shouldReturn400WhenStartTimeIsNull() throws Exception {
        testNullTimeScenario(
                """
                        {
                            "startTime": null,
                            "endTime": "2025-03-26T14:00:00Z",
                            "participants": {}
                        }
                        """
        );
    }

    @Test
    void saveShift_shouldReturn400WhenEndTimeIsNull() throws Exception {
        testNullTimeScenario(
                """
                        {
                            "startTime": "2025-03-26T12:00:00Z",
                            "endTime": null,
                            "participants": {}
                        }
                        """
        );
    }

    private void testNullTimeScenario(String requestBody) throws Exception {
        mvc.perform(post("/api/shift")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").value("Ein Fehler ist aufgetreten: startTime and endTime are required"));

        assert (shiftRepository.findAll()).isEmpty();
    }
}