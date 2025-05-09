package com.github.nieyo.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.UUID;

@Document(collection = "organisations")
public record Organisation(
        @Id UUID id,
        String name,
        String adminUserId,
        List<UUID> eventIds
) {}
