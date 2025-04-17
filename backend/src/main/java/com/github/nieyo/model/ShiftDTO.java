package com.github.nieyo.model;

import java.util.List;

public record ShiftDTO(
        String id,
        List<String> duration,
        List<UserDTO> participants
) {
}
