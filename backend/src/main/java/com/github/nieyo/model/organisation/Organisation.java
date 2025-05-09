package com.github.nieyo.model.organisation;

import org.springframework.data.annotation.Id;

import java.util.List;

public record Organisation(
        @Id String id,
        String name,
        String adminUserId,
        List<String> eventIds
) {}
