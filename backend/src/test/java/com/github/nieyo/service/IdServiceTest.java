package com.github.nieyo.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IdServiceTest {

    IdService idService = new IdService();

    @Test
    void randomId_shouldGenerateValidUUID() {
        String uuid = idService.randomId();

        // Überprüfe UUID-Format (8-4-4-4-12 Hex-Zeichen)
        assertTrue(uuid.matches("[a-f0-9]{8}-[a-f0-9]{4}-4[a-f0-9]{3}-[89ab][a-f0-9]{3}-[a-f0-9]{12}"));
        assertFalse(uuid.isEmpty());
    }
}