package com.nexodesk.auth.dto;

import java.util.UUID;

import com.nexodesk.user.entity.Role;

public record AuthResponse(String token, UUID userId, String email, Role role) {
}
