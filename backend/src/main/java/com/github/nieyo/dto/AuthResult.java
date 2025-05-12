package com.github.nieyo.dto;

import com.github.nieyo.entity.User;

public record AuthResult(User user, String token) {}
