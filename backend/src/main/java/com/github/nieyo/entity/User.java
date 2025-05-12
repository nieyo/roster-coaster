package com.github.nieyo.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.UUID;

@Document(collection = "users")
public record User (
        @Id UUID id,
        String name,
        String email,
        String password,
        List<UUID> organizationIds,
        List<UUID> adminOrganizationIds
) {}
