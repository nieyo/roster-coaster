package com.github.nieyo.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.UUID;

@Document(collection = "events")
public record Event(
        @Id UUID id,
        String title,
        UUID ownerOrganizationId,
        List<UUID> organizerOrganizationIds,
        List<UUID> taskIds
) {}
