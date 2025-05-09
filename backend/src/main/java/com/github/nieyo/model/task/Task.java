package com.github.nieyo.model.task;

import org.springframework.data.annotation.Id;

import java.util.List;

public record Task(
        @Id String id,
        String eventId,
        String title,
        String description,
        List<String> responsibleOrganizationIds,
        List<String> shiftIds
) {}
