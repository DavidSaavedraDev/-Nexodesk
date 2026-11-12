package com.nexodesk.auth.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.nexodesk.user.entity.Role;
import com.nexodesk.user.entity.User;

class JwtServiceTest {

    @Test
    void generatesTokenWithUserSubject() {
        var service = new JwtService("12345678901234567890123456789012", 60);
        User user = new User("Test", "User", "test@example.com", "hash", Role.EMPLOYEE);

        assertEquals(user.getEmail(), service.extractSubject(service.generateToken(user)));
    }
}
