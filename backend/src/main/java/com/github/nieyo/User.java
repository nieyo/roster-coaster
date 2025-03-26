package com.github.nieyo;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

public record User(
        @Id ObjectId id,
        String name,
        String email,
        boolean isAnonymous
) {
}
