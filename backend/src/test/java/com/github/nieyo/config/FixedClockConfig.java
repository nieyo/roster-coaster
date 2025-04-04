package com.github.nieyo.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

@TestConfiguration
public class FixedClockConfig {
    @Primary
    @Bean
    public Clock fixedClock() {
        return Clock.fixed(
                Instant.parse("2025-04-01T00:00:00Z"),
                ZoneId.of("UTC")
        );
    }
}