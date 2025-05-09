package com.github.nieyo.controller;

import com.github.nieyo.config.FixedClockConfig;
import com.github.nieyo.entity.Shift;
import com.github.nieyo.entity.ShiftDuration;
import com.github.nieyo.dto.ShiftDurationDTO;
import com.github.nieyo.entity.ShiftSignup;
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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = FixedClockConfig.class)
@AutoConfigureMockMvc(addFilters = false) // disable authentication
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

    private static final UUID ID_0 = UUID.fromString("00000000-0000-0000-0000-000000000000");
    private static final UUID ID_1 = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final UUID ID_2 = UUID.fromString("22222222-2222-2222-2222-222222222222");
    private static final UUID ID_3 = UUID.fromString("33333333-3333-3333-3333-333333333333");

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
                .id(ID_1)
                .duration(duration)
                .signups(signups)
                .build();

        Shift shift2 = Shift.builder()
                .id(ID_2)
                .duration(duration)
                .signups(signups)
                .build();

        shiftRepository.save(shift1);
        shiftRepository.save(shift2);

        // WHEN
        mvc.perform(get("/api/shift").contentType(MediaType.APPLICATION_JSON))
                // THEN
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        [
                            {
                                "id": "11111111-1111-1111-1111-111111111111",
                                "duration": {
                                    "start": "2025-04-01T00:15:00Z",
                                    "end": "2025-04-01T00:45:00Z"
                                },
                                "signups": []
                            },
                            {
                                "id": "22222222-2222-2222-2222-222222222222",
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
        Shift expected = Shift.builder()
                .id(ID_3)
                .duration(duration)
                .signups(signups)
                .build();

        shiftRepository.save(expected);
        // WHEN
        mvc.perform(get("/api/shift/" + ID_3).contentType(MediaType.APPLICATION_JSON))
                // THEN
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                            "id": "%s",
                            "duration": {
                                "start": "2025-04-01T00:15:00Z",
                                "end": "2025-04-01T00:45:00Z"
                            },
                            "signups": []
                        }
                        """.formatted(ID_3)));

    }

    @Test
    void getShiftById_whenNotFound_return404() throws Exception {
        mvc.perform(get("/api/shift/" + ID_0).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


    // UPDATE

    @Test
    @DirtiesContext
    void updateShift_whenFound_returnShift() throws Exception {
        // GIVEN
        Shift shift = Shift.builder()
                .id(ID_1)
                .duration(duration)
                .signups(signups)
                .build();

        shiftRepository.save(shift);
        // WHEN
        mvc.perform(put("/api/shift/" + ID_1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "id": "%s",
                                    "duration": {
                                        "start": "2025-04-01T00:15:00Z",
                                        "end": "2025-04-01T00:45:00Z"
                                    },
                                    "signups": []
                                }
                                """.formatted(ID_1)))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                            "id": "%s",
                            "duration": {
                                "start": "2025-04-01T00:15:00Z",
                                "end": "2025-04-01T00:45:00Z"
                            },
                            "signups": []
                        }
                        """.formatted(ID_1)));

    }

    @Test
    void updateShift_whenNotFound_throwNoSuchElementException() throws Exception {
        // GIVEN
        UUID id = ID_0;

        // WHEN
        mvc.perform(put("/api/shift/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "id": "%s",
                                    "duration": {
                                        "start": "2025-04-01T00:15:00Z",
                                        "end": "2025-04-01T00:45:00Z"
                                    },
                                    "signups": [{"name": "Alan"}]
                                }
                                """.formatted(id)))
                // THEN
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMessage").value("shift not found with the id " + id));
    }


    @Test
    @DirtiesContext
    void updateShift_whenIdDoesNotMatch_throwIllegalArgumentException() throws Exception {
        // GIVEN
        Shift shift = Shift.builder()
                .id(ID_1)
                .duration(duration)
                .signups(signups)
                .build();

        shiftRepository.save(shift);

        String urlId = ID_1.toString();
        String bodyId = ID_2.toString();

        // WHEN
        mvc.perform(put("/api/shift/" + urlId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                      {
                        "id": "%s",
                        "duration": {
                            "start": "2025-04-01T00:15:00Z",
                            "end": "2025-04-01T00:45:00Z"
                        },
                        "signups": [{"name": "Alan"}]
                      }
                    """.formatted(bodyId)))
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
                .id(ID_1)
                .duration(duration)
                .signups(signups)
                .build();

        shiftRepository.save(shiftToDelete);

        // WHEN & THEN
        mvc.perform(delete("/api/shift/" + ID_1).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void deleteShift_whenNotFound_returnNotFound() throws Exception {
        mvc.perform(delete("/api/shift/" + ID_0).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}