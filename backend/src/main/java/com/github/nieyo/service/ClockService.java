package com.github.nieyo.service;

import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class ClockService {
    public Instant now() { return Instant.now(); }
}
