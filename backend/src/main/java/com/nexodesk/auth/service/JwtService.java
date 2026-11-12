package com.nexodesk.auth.service;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.nexodesk.user.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private final SecretKey signingKey;
    private final long expirationMillis;

    public JwtService(
        @Value("${security.jwt.secret}") String secret,
        @Value("${security.jwt.expiration-minutes:60}") long expirationMinutes
    ) {
        if (secret.length() < 32) {
            throw new IllegalArgumentException("security.jwt.secret must contain at least 32 characters");
        }
        this.signingKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMillis = expirationMinutes * 60_000L;
    }

    public String generateToken(User user) {
        Date now = new Date();
        return Jwts.builder()
            .subject(user.getEmail())
            .claim("role", user.getRole().name())
            .claim("userId", user.getId().toString())
            .issuedAt(now)
            .expiration(new Date(now.getTime() + expirationMillis))
            .signWith(signingKey)
            .compact();
    }

    public String extractSubject(String token) {
        return parse(token).getSubject();
    }

    private Claims parse(String token) {
        return Jwts.parser().verifyWith(signingKey).build().parseSignedClaims(token).getPayload();
    }
}
