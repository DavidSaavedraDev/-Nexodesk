package com.nexodesk.config;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

class SecurityConfigTest {

    @Test
    void passwordEncoderUsesBcrypt() {
        var encoder = new SecurityConfig().passwordEncoder();

        assertTrue(encoder instanceof BCryptPasswordEncoder);
        String encoded = encoder.encode("test-password");
        assertTrue(encoded.startsWith("$2"));
        assertTrue(encoder.matches("test-password", encoded));
    }
}
