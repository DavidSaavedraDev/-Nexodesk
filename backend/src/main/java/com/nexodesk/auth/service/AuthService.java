package com.nexodesk.auth.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nexodesk.auth.dto.AuthResponse;
import com.nexodesk.auth.dto.LoginRequest;
import com.nexodesk.auth.dto.RegisterRequest;
import com.nexodesk.user.entity.Role;
import com.nexodesk.user.entity.User;
import com.nexodesk.user.repository.UserRepository;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(
        UserRepository userRepository,
        PasswordEncoder passwordEncoder,
        AuthenticationManager authenticationManager,
        JwtService jwtService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmailIgnoreCase(request.email())) {
            throw new IllegalArgumentException("Email is already registered");
        }
        User user = userRepository.save(new User(
            request.firstName(), request.lastName(), request.email().toLowerCase(),
            passwordEncoder.encode(request.password()), Role.EMPLOYEE
        ));
        return response(user);
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );
        User user = userRepository.findByEmailIgnoreCase(request.email())
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return response(user);
    }

    private AuthResponse response(User user) {
        return new AuthResponse(jwtService.generateToken(user), user.getId(), user.getEmail(), user.getRole());
    }
}
