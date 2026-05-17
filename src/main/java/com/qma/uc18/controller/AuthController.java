package com.qma.uc18.controller;

import com.qma.uc18.dto.AuthResponse;
import com.qma.uc18.dto.LoginRequest;
import com.qma.uc18.dto.RegisterRequest;
import com.qma.uc18.model.User;
import com.qma.uc18.repository.UserRepository;
import com.qma.uc18.security.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Tag(name = "Authentication", description = "Register, login and Google OAuth2 endpoints")
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:8085"})
public class AuthController {

    @Autowired private AuthenticationManager authManager;
    @Autowired private JwtTokenProvider      tokenProvider;
    @Autowired private PasswordEncoder       passwordEncoder;
    @Autowired private UserRepository        userRepository;

    @Operation(
        summary = "Register a new user",
        description = "Creates a local account and returns a JWT token"
    )
    @RequestBody(
        required = true,
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = RegisterRequest.class),
            examples = @ExampleObject(
                name = "Sample Registration",
                value = "{\"name\":\"Love Vyas\",\"email\":\"love@qma.com\",\"password\":\"pass123\"}"
            )
        )
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Registered successfully",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = AuthResponse.class))),
        @ApiResponse(responseCode = "400", description = "Email already in use", content = @Content)
    })
    @PostMapping("/register")
    public ResponseEntity<?> register(
            @org.springframework.web.bind.annotation.RequestBody RegisterRequest req) {
        if (userRepository.existsByEmail(req.getEmail())) {
            return ResponseEntity.badRequest()
                .body(new ErrorResponse("Email already in use: " + req.getEmail()));
        }
        User user = new User(
            req.getEmail(),
            passwordEncoder.encode(req.getPassword()),
            req.getName() != null ? req.getName() : req.getEmail(),
            "local",
            Set.of("USER")
        );
        userRepository.save(user);
        String token = tokenProvider.generateTokenFromUsername(user.getEmail());
        return ResponseEntity.ok(new AuthResponse(token, user.getEmail(), user.getName()));
    }

    @Operation(
        summary = "Login with email & password",
        description = "Returns a JWT token. Copy it and click **Authorize** at the top of the page."
    )
    @RequestBody(
        required = true,
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = LoginRequest.class),
            examples = @ExampleObject(
                name = "Sample Login",
                value = "{\"email\":\"love@qma.com\",\"password\":\"pass123\"}"
            )
        )
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Login successful",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = AuthResponse.class))),
        @ApiResponse(responseCode = "401", description = "Invalid credentials", content = @Content)
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @org.springframework.web.bind.annotation.RequestBody LoginRequest req) {
        try {
            Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword()));
            String token = tokenProvider.generateToken(auth);
            User user = userRepository.findByEmail(req.getEmail()).orElseThrow();
            return ResponseEntity.ok(new AuthResponse(token, user.getEmail(), user.getName()));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401)
                .body(new ErrorResponse("Invalid email or password"));
        }
    }

    @Operation(
        summary = "Get current user profile",
        description = "Returns the authenticated user's details. Requires Bearer token.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User profile",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = AuthResponse.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    @GetMapping("/me")
    public ResponseEntity<?> me(Authentication authentication) {
        return userRepository.findByEmail(authentication.getName())
            .<ResponseEntity<?>>map(u ->
                ResponseEntity.ok(new AuthResponse(null, u.getEmail(), u.getName())))
            .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Health check", description = "Returns service UP status")
    @ApiResponse(responseCode = "200", description = "Service is UP")
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok(
            "{\"status\":\"UP\",\"service\":\"qma-security\",\"port\":8085}");
    }

    @Operation(summary = "OAuth2 success callback",
        description = "Called after successful Google OAuth2 redirect",
        security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/oauth2/success")
    public ResponseEntity<AuthResponse> oauth2Success(Authentication authentication) {
        String email = authentication.getName();
        String token = tokenProvider.generateTokenFromUsername(email);
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("OAuth2 user not found"));
        return ResponseEntity.ok(new AuthResponse(token, user.getEmail(), user.getName()));
    }

    @Operation(summary = "OAuth2 failure callback")
    @GetMapping("/oauth2/failure")
    public ResponseEntity<?> oauth2Failure() {
        return ResponseEntity.status(401)
            .body(new ErrorResponse("Google OAuth2 login failed. Please try again."));
    }
}
