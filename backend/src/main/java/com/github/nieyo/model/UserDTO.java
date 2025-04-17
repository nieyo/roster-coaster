package com.github.nieyo.model;

import jakarta.validation.constraints.NotEmpty;

public record UserDTO(
        @NotEmpty(message = "User name must not be empty")
        String name
) {}