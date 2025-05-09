package com.github.nieyo.service;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class IdServiceTest {

    IdService idService = new IdService();

    @Test
    void randomId_shouldGenerateValidUUID() {
        UUID uuid = idService.randomId();

        assertNotNull(uuid);

        String uuidStr = uuid.toString();
        assertTrue(uuidStr.matches("[a-f0-9]{8}-[a-f0-9]{4}-4[a-f0-9]{3}-[89ab][a-f0-9]{3}-[a-f0-9]{12}"));
    }
}