package com.github.nieyo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class BackendApplicationTest {

    @Test
    void contextLoads() {
        assertTrue(true, "spring startup failed");
    }

}