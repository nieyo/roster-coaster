package com.github.nieyo.model.user;

import org.springframework.data.annotation.Id;

import java.util.List;

public record User(
        @Id String id,
        String name,
        String email,
        List<String> organizationIds,
        List<String> adminOrganizationIds
) {}
