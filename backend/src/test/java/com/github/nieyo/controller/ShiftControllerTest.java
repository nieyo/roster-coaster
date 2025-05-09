package com.github.nieyo.controller;

import com.github.nieyo.config.FixedClockConfig;
import com.github.nieyo.model.shift.Shift;
import com.github.nieyo.model.shift.ShiftDuration;
import com.github.nieyo.model.shift.ShiftDurationDTO;
import com.github.nieyo.model.shift.ShiftSignup;
import com.github.nieyo.repository.ShiftRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = FixedClockConfig.class)
@AutoConfigureMockMvc
class ShiftControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ShiftRepository shiftRepository;

    @Autowired
    private Clock clock;

    Instant now;
    ShiftDuration duration;
    ShiftDurationDTO durationDTO;
    List<ShiftSignup> signups = List.of();

    @BeforeEach
    void setUp() {
        now = clock.instant();
        duration = new ShiftDuration(now.plus(Duration.ofMinutes(15)), now.plus(Duration.ofMinutes(45)));
        durationDTO = new ShiftDurationDTO(now.plus(Duration.ofMinutes(15)).toString(), now.plus(Duration.ofMinutes(45)).toString());

        shiftRepository.deleteAll();
    }

    // CREATE

    @Test
    void saveShift_shouldPersistShift() throws Exception {
        // Given
        String requestBody = """
                {
                    "duration" : {
                        "start": "2025-04-01T00:15:00Z",
                        "end": "2025-04-01T00:45:00Z"
                    },
                    "signups": []
                }
                """;

        // When
        mvc.perform(post("/api/shift").contentType(MediaType.APPLICATION_JSON).content(requestBody))

                // Then (HTTP layer)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.duration.start").value(durationDTO.start()))
                .andExpect(jsonPath("$.duration.end").value(durationDTO.end()))
                .andExpect(jsonPath("$.signups").isArray())
                .andExpect(jsonPath("$.signups").isEmpty());

        // Verify database state
        List<Shift> shifts = shiftRepository.findAll();
        assertEquals(1, shifts.size());
        assertEquals(duration.start(), shifts.getFirst().duration().start());
        assertEquals(duration.end(), shifts.getFirst().duration().end());
        assertTrue(shifts.getFirst().signups().isEmpty());
    }

    @Test
    void saveShift_shouldRejectEndBeforeStart() throws Exception {
        String invalidBody = """
                {
                    "duration": {
                        "end": "2025-04-01T00:15:00Z",
                        "start": "2025-04-01T00:45:00Z"
                    },
                    "signups": []
                }
                """;

        mvc.perform(post("/api/shift").contentType(MediaType.APPLICATION_JSON).content(invalidBody))
                .andExpect(status().isBadRequest());

        assertEquals(0, shiftRepository.findAll().size());
    }

    @Test
    void saveShift_shouldReturn400WhenStartTimeIsNull() throws Exception {
        testNullTimeScenario("""
                {
                    "duration": {
                        "start": ,
                        "end": "2025-04-01T00:45:00Z"
                    },
                    "signups": []
                }
                """);
    }

    @Test
    void saveShift_shouldReturn400WhenEndTimeIsNull() throws Exception {
        testNullTimeScenario("""
                {
                    "duration": {
                        "start": "2025-04-01T00:15:00Z",
                        "end": ,
                    },
                    "signups": []
                }
                """);
    }

    private void testNullTimeScenario(String requestBody) throws Exception {
        mvc.perform(post("/api/shift").contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isBadRequest());

        assert (shiftRepository.findAll()).isEmpty();
    }

    @Test
    void saveShift_shouldReturn400WhenBodyIsMissing() throws Exception {
        mvc.perform(post("/api/shift")  // No .content() or .contentType()
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }

    // GET ALL

    @Test
    void getShifts_whenEmpty_returnEmptyList() throws Exception {
        // WHEN
        mvc.perform(get("/api/shift").contentType(MediaType.APPLICATION_JSON))
                // THEN
                .andExpect(status().isOk()).andExpect(content().json("""
                          []
                        """));
    }

    @Test
    void getShifts_whenFound_returnShifts() throws Exception {
        Shift shift1 = Shift.builder()
                .id("1")
                .duration(duration)
                .signups(signups)
                .build();

        Shift shift2 = Shift.builder()
                .id("2")
                .duration(duration)
                .signups(signups)
                .build();

        shiftRepository.save(shift1);
        shiftRepository.save(shift2);

        // WHEN
        mvc.perform(get("/api/shift").contentType(MediaType.APPLICATION_JSON))
                // THEN
                .andExpect(status().isOk()).andExpect(content().json("""
                        [
                            {
                                "id": "1",
                                "duration": {
                                    "start": "2025-04-01T00:15:00Z",
                                    "end": "2025-04-01T00:45:00Z"
                                },
                                "signups": []
                            },
                            {
                                "id": "2",
                                "duration": {
                                    "start": "2025-04-01T00:15:00Z",
                                    "end": "2025-04-01T00:45:00Z"
                                },
                                "signups": []
                            }
                        ]
                        """));
    }

    // GET BY ID

    @Test
    void getShiftById_whenIdExists_returnShift() throws Exception {
        String existingId = "34";
        Shift expected = Shift.builder()
                .id(existingId)
                .duration(duration)
                .signups(signups)
                .build();

        shiftRepository.save(expected);
        // WHEN
        mvc.perform(get("/api/shift/34").contentType(MediaType.APPLICATION_JSON))
                // THEN
                .andExpect(status().isOk()).andExpect(content().json("""
                        {
                            "id": "34",
                            "duration": {
                                "start": "2025-04-01T00:15:00Z",
                                "end": "2025-04-01T00:45:00Z"
                            },
                            "signups": []
                        }
                        """));
    }

    @Test
    void getShiftById_whenNotFound_returnException() throws Exception {
        // WHEN
        mvc.perform(get("/api/shift/345").contentType(MediaType.APPLICATION_JSON))
                // THEN
                .andExpect(status().isNotFound());
    }

    // UPDATE

    @Test
    @DirtiesContext
    void updateShift_whenFound_returnShift() throws Exception {
        // GIVEN
        Shift shift = Shift.builder()
                .id("1")
                .duration(duration)
                .signups(signups)
                .build();

        shiftRepository.save(shift);
        // WHEN
        mvc.perform(put("/api/shift/1").contentType(MediaType.APPLICATION_JSON).content("""
                        {
                            "id": "1",
                            "duration": {
                                "start": "2025-04-01T00:15:00Z",
                                "end": "2025-04-01T00:45:00Z"
                            },
                            "signups": []
                        }
                        """))
                // THEN
                .andExpect(status().isOk()).andExpect(content().json("""
                        {
                            "id": "1",
                            "duration": {
                                "start": "2025-04-01T00:15:00Z",
                                "end": "2025-04-01T00:45:00Z"
                            },
                            "signups": []
                        }
                        """));
    }

    @Test
    void updateShift_whenNotFound_throwNoSuchElementException() throws Exception {
        // GIVEN
        String id = "doesNotExist";

        // WHEN
        mvc.perform(put("/api/shift/" + id).contentType(MediaType.APPLICATION_JSON).content("""
                        {
                        "id": "doesNotExist",
                        "duration": {
                            "start": "2025-04-01T00:15:00Z",
                            "end": "2025-04-01T00:45:00Z"
                        },
                        "signups": [{"name": "Alan"}]
                        }
                        """))
                // THEN
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMessage").value("shift not found with the id " + id));
    }

    @Test
    @DirtiesContext
    void updateShift_whenIdDoesNotMatch_throwIllegalArgumentException() throws Exception {
        // GIVEN
        Shift shift = Shift.builder()
                .id("1")
                .duration(duration)
                .signups(signups)
                .build();

        shiftRepository.save(shift);
        // WHEN
        mvc.perform(put("/api/shift/1").contentType(MediaType.APPLICATION_JSON).content("""
                          {
                            "id": "2",
                            "duration": {
                                "start": "2025-04-01T00:15:00Z",
                                "end": "2025-04-01T00:45:00Z"
                            },
                            "signups": [{"name": "Alan"}]
                          }
                        """))
                // THEN
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").value("Ein Fehler ist aufgetreten: ID is not changeable"));
    }

    // DELETE

    @Test
    @DirtiesContext
    void deleteShift_whenFound_removesShift() throws Exception {
        // GIVEN
        Shift shiftToDelete = Shift.builder()
                .id("1")
                .duration(duration)
                .signups(signups)
                .build();

        shiftRepository.save(shiftToDelete);

        // WHEN & THEN
        mvc.perform(delete("/api/shift/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void deleteShift_whenNotFound_returnNotFound() throws Exception {
        // WHEN & THEN
        mvc.perform(delete("/api/shift/does-not-exist").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

}