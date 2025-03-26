package com.github.nieyo.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.util.Optional;

public record User(
        @Id ObjectId id,
        String name,
        Optional<String> email,
        boolean isAnonymous
) {
}
