package com.github.nieyo.exception;

import java.time.LocalDateTime;

public record ErrorMessage(
        String errorMessage,
        LocalDateTime timestamp
) {
    public ErrorMessage(String errorMessage) {
        this(errorMessage, LocalDateTime.now());
    }

    public ErrorMessage {
        timestamp = LocalDateTime.now();
    }
}
