package com.qma.auth.service;

import com.qma.auth.dto.AuthResponse;
import com.qma.auth.dto.LoginRequest;
import com.qma.auth.dto.RegisterRequest;
import com.qma.auth.model.User;
import com.qma.auth.repository.UserRepository;
import com.qma.auth.security.JwtTokenProvider;
import com.qma.auth.entity.RefreshToken;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private static final Logger logger = LogManager.getLogger(AuthService.class);

    @Autowired private UserRepository   userRepository;
    @Autowired private PasswordEncoder  passwordEncoder;
    @Autowired private JwtTokenProvider jwtTokenProvider;

    @Autowired private RefreshTokenService refreshTokenService;

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
        String refreshToken = refreshTokenService.createRefreshToken(user.getId()).getToken();
        return AuthResponse.ok(token, refreshToken, toDto(user));
    }

    // ── Login ────────────────────────────────────────────────────────────────────

    public AuthResponse login(LoginRequest req) {
        User user = userRepository.findByEmail(req.getEmail()).orElse(null);
        if (user == null || !passwordEncoder.matches(req.getPassword(), user.getPassword()))
            return AuthResponse.err("Invalid email or password");

        String token = jwtTokenProvider.generateToken(user.getEmail());
        String refreshToken = refreshTokenService.createRefreshToken(user.getId()).getToken();
        return AuthResponse.ok(token, refreshToken, toDto(user));
    }

    // ── Current user ─────────────────────────────────────────────────────────────

    public AuthResponse getMe(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) return AuthResponse.err("User not found");
        return AuthResponse.ok(null, null, toDto(user));
    }

    // ── Tokens ───────────────────────────────────────────────────────────────────

    public com.qma.auth.dto.TokenRefreshResponse refreshToken(com.qma.auth.dto.TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();
        return refreshTokenService.findByToken(requestRefreshToken)
            .map(refreshTokenService::verifyExpiration)
            .map(RefreshToken::getUserId)
            .map(userId -> {
                User user = userRepository.findById(userId)
                    .orElseThrow(() -> new com.qma.auth.exception.TokenRefreshException(requestRefreshToken, "User not found"));
                String token = jwtTokenProvider.generateToken(user.getEmail());
                return new com.qma.auth.dto.TokenRefreshResponse(token, requestRefreshToken);
            })
            .orElseThrow(() -> new com.qma.auth.exception.TokenRefreshException(requestRefreshToken, "Refresh token is not in database!"));
    }

    public void logout(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user != null) {
            refreshTokenService.deleteByUserId(user.getId());
        }
    }

    // ── Helper ───────────────────────────────────────────────────────────────────

    private AuthResponse.UserDto toDto(User u) {
        return new AuthResponse.UserDto(u.getId(), u.getUsername(), u.getEmail(), u.getRole().name());
    }
}
