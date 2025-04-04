package com.github.nieyo.service;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class IdService {
    public String randomId() {
        return UUID.randomUUID().toString();
    }
}