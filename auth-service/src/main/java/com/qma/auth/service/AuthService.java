package com.qma.auth.service;

import com.qma.auth.dto.AuthResponse;
import com.qma.auth.dto.LoginRequest;
import com.qma.auth.dto.RegisterRequest;
import com.qma.auth.model.User;
import com.qma.auth.repository.UserRepository;
import com.qma.auth.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired private UserRepository   userRepository;
    @Autowired private PasswordEncoder  passwordEncoder;
    @Autowired private JwtTokenProvider jwtTokenProvider;

    // ── Register ─────────────────────────────────────────────────────────────────

    public AuthResponse register(RegisterRequest req) {
        if (userRepository.existsByEmail(req.getEmail()))
            return AuthResponse.err("Email already registered: " + req.getEmail());
        if (userRepository.existsByUsername(req.getUsername()))
            return AuthResponse.err("Username already taken: " + req.getUsername());

        User user = new User(
            req.getUsername(),
            req.getEmail(),
            passwordEncoder.encode(req.getPassword())
        );
        userRepository.save(user);

        String token = jwtTokenProvider.generateToken(user.getEmail());
        return AuthResponse.ok(token, toDto(user));
    }

    // ── Login ────────────────────────────────────────────────────────────────────

    public AuthResponse login(LoginRequest req) {
        User user = userRepository.findByEmail(req.getEmail()).orElse(null);
        if (user == null || !passwordEncoder.matches(req.getPassword(), user.getPassword()))
            return AuthResponse.err("Invalid email or password");

        String token = jwtTokenProvider.generateToken(user.getEmail());
        return AuthResponse.ok(token, toDto(user));
    }

    // ── Current user ─────────────────────────────────────────────────────────────

    public AuthResponse getMe(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) return AuthResponse.err("User not found");
        return AuthResponse.ok(null, toDto(user));
    }

    // ── Helper ───────────────────────────────────────────────────────────────────

    private AuthResponse.UserDto toDto(User u) {
        return new AuthResponse.UserDto(u.getId(), u.getUsername(), u.getEmail(), u.getRole().name());
    }
}
