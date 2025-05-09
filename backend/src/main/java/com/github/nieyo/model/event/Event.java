package com.github.nieyo.model.event;

import org.springframework.data.annotation.Id;

import java.util.List;

public record Event(
        @Id String id,
        String title,
        String ownerOrganizationId,
        List<String> organizerOrganizationIds,
        List<String> taskIds
) {}
