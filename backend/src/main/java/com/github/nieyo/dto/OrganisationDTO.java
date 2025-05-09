package com.github.nieyo.dto;

import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record OrganisationDTO(
        UUID id,
        String name,
        String adminUserId,
        List<UUID> eventIds
) {
}
