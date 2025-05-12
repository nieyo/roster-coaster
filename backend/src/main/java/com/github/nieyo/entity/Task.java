package com.github.nieyo.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.UUID;

@Document(collection = "tasks")
public record Task(
        @Id UUID id,
        String eventId,
        String title,
        String description,
        List<UUID> responsibleOrganizationIds,
        List<UUID> shiftIds
) {}
