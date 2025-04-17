package com.github.nieyo.model;

import java.util.List;

public record ShiftDTO(
        String id,
        String startTime,
        String endTime,
        List<UserDTO> participants
) {
}
