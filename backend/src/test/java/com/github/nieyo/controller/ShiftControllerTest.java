package com.github.nieyo.controller;

import com.github.nieyo.model.Shift;
import com.github.nieyo.model.User;
import com.github.nieyo.repository.ShiftRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ShiftControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ShiftRepository shiftRepository;

    Instant startTime = Instant.parse("2025-03-26T12:00:00Z");
    Instant endTime = Instant.parse("2025-03-26T14:00:00Z");
    List<User> participants = List.of();

    @BeforeEach
    void setUp() {
        shiftRepository.deleteAll();
    }

    // CREATE

    @Test
    void saveShift_shouldPersistShift() throws Exception {
        // Given
        String requestBody = """
                {
                    "startTime": "2025-03-26T12:00:00Z",
                    "endTime": "2025-03-26T14:00:00Z",
                    "participants": []
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
                .andExpect(jsonPath("$.participants").isArray())
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
                .andExpect(status().isBadRequest());

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
                .andExpect(status().isBadRequest());

        assert (shiftRepository.findAll()).isEmpty();
    }

    @Test
    void saveShift_shouldReturn400WhenBodyIsMissing() throws Exception {
        mvc.perform(post("/api/shift")  // No .content() or .contentType()
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    // GET ALL

    @Test
    void getShifts_whenEmpty_returnEmptyList() throws Exception {
        // WHEN
        mvc.perform(get("/api/shift")
                        .contentType(MediaType.APPLICATION_JSON))
                // THEN
                .andExpect(status().isOk())
                .andExpect(content().json("""
                          []
                        """));
    }

    @Test
    void getShifts_whenFound_returnShifts() throws Exception {
        Shift shift1 = new Shift("1", startTime, endTime, participants);
        Shift shift2 = new Shift("2", startTime, endTime, participants);

        shiftRepository.save(shift1);
        shiftRepository.save(shift2);

        // WHEN
        mvc.perform(get("/api/shift")
                        .contentType(MediaType.APPLICATION_JSON))
                // THEN
                .andExpect(status().isOk())
                .andExpect(content().json("""
                         [
                           {
                             "id": "1",
                             "startTime": "2025-03-26T12:00:00Z",
                             "endTime": "2025-03-26T14:00:00Z",
                             "participants": []
                           },
                           {
                             "id": "2",
                             "startTime": "2025-03-26T12:00:00Z",
                             "endTime": "2025-03-26T14:00:00Z",
                             "participants": []
                           }
                         ]
                        """));
    }

    // GET BY ID

    @Test
    void getShiftById_whenIdExists_returnShift() throws Exception {
        String existingId = "34";
        Shift expected = new Shift(existingId, startTime, endTime, participants);
        shiftRepository.save(expected);
        // WHEN
        mvc.perform(get("/api/shift/34")
                        .contentType(MediaType.APPLICATION_JSON))
                // THEN
                .andExpect(status().isOk())
                .andExpect(content().json("""
                          {
                            "id": "34",
                            "startTime": "2025-03-26T12:00:00Z",
                            "endTime": "2025-03-26T14:00:00Z",
                            "participants": []
                          }
                        """));
    }

    @Test
    void getShiftById_whenNotFound_returnException() throws Exception {
        // WHEN
        mvc.perform(get("/api/shift/345")
                        .contentType(MediaType.APPLICATION_JSON))
                // THEN
                .andExpect(status().isNotFound());
    }

    // UPDATE

    @Test
    @DirtiesContext
    void updateShift_whenFound_returnShift() throws Exception {
        // GIVEN
        Shift shift = new Shift("1", startTime, endTime, participants);
        shiftRepository.save(shift);
        // WHEN
        mvc.perform(put("/api/shift/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {
                                      "id": "1",
                                      "startTime": "2025-03-26T12:00:00Z",
                                      "endTime": "2025-03-26T14:00:00Z",
                                      "participants": [{"name": "Alan"}]
                                    }
                                """)
                )
                // THEN
                .andExpect(status().isOk())
                .andExpect(content().json("""
                          {
                            "id": "1",
                            "startTime": "2025-03-26T12:00:00Z",
                            "endTime": "2025-03-26T14:00:00Z",
                            "participants": [{"name": "Alan"}]
                          }
                        """)
                );
    }

    @Test
    void updateShift_whenNotFound_throwNoSuchElementException() throws Exception {
        // GIVEN
        String id = "doesNotExist";

        // WHEN
        mvc.perform(put("/api/shift/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "id": "doesNotExist",
                                "startTime": "2025-03-26T12:00:00Z",
                                "endTime": "2025-03-26T14:00:00Z",
                                "participants": [{"name": "Alan"}]
                                }
                                """)
                )
                // THEN
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMessage").value("shift not found with the id " + id));
    }

    @Test
    @DirtiesContext
    void updateShift_whenIdDoesNotMatch_throwIllegalArgumentException() throws Exception {
        // GIVEN
        Shift shift = new Shift("1", startTime, endTime, participants);
        shiftRepository.save(shift);
        // WHEN
        mvc.perform(put("/api/shift/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                """
                                  {
                                    "id": "2",
                                    "startTime": "2025-03-26T12:00:00Z",
                                    "endTime": "2025-03-26T14:00:00Z",
                                    "participants": [{"name": "Alan"}]
                                  }
                                """
                        )
                )
                // THEN
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").value("Ein Fehler ist aufgetreten: ID is not changeable"));
    }

    // DELETE

    @Test
    @DirtiesContext
    void deleteShift_whenFound_removesShift() throws Exception {
        // GIVEN
        Shift shiftToDelete = new Shift("1", startTime, endTime, participants);
        shiftRepository.save(shiftToDelete);

        // WHEN & THEN
        mvc.perform(delete("/api/shift/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void deleteShift_whenNotFound_returnNotFound() throws Exception {
        // WHEN & THEN
        mvc.perform(delete("/api/shift/does-not-exist")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

}