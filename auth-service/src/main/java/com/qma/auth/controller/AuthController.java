package com.qma.auth.controller;

import com.qma.auth.dto.AuthResponse;
import com.qma.auth.dto.LoginRequest;
import com.qma.auth.dto.RegisterRequest;
import com.qma.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * Authentication controller — JWT-based register / login / me.
 * CORS is handled exclusively by the API Gateway; no @CrossOrigin here.
 */
@Tag(name = "Authentication", description = "JWT register, login, and profile — UC21 auth-service")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LogManager.getLogger(AuthController.class);

    @Autowired private AuthService authService;

    // ── Register ─────────────────────────────────────────────────────────────────

    @Operation(summary = "Register a new user")
    @RequestBody(
        required = true,
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = RegisterRequest.class),
            examples = @ExampleObject(
                value = "{\"username\":\"love\",\"email\":\"love@qma.com\",\"password\":\"pass123\"}"
            )
        )
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Registered — JWT returned"),
        @ApiResponse(responseCode = "400", description = "Email/username already taken", content = @Content)
    })
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @Valid @org.springframework.web.bind.annotation.RequestBody RegisterRequest req) {
        logger.info("Received register request for email: {}", req.getEmail());
        AuthResponse res = authService.register(req);
        if (res.isSuccess()) {
            logger.info("Successfully registered user: {}", req.getEmail());
            return ResponseEntity.ok(res);
        } else {
            logger.warn("Registration failed for email {}: {}", req.getEmail(), res.getMessage());
            return ResponseEntity.badRequest().body(res);
        }
    }

    // ── Login ────────────────────────────────────────────────────────────────────

    @Operation(
        summary  = "Login with email & password",
        description = "Returns a signed JWT. Click **Authorize** above and paste: `Bearer <token>`"
    )
    @RequestBody(
        required = true,
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = LoginRequest.class),
            examples = @ExampleObject(
                value = "{\"email\":\"love@qma.com\",\"password\":\"pass123\"}"
            )
        )
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Login successful — JWT returned"),
        @ApiResponse(responseCode = "401", description = "Invalid credentials",       content = @Content)
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @org.springframework.web.bind.annotation.RequestBody LoginRequest req) {
        logger.info("Received login request for email: {}", req.getEmail());
        AuthResponse res = authService.login(req);
        if (res.isSuccess()) {
            logger.info("Successfully logged in user: {}", req.getEmail());
            return ResponseEntity.ok(res);
        } else {
            logger.warn("Login failed for email {}: {}", req.getEmail(), res.getMessage());
            return ResponseEntity.status(401).body(res);
        }
    }

    // ── Current user (protected) ──────────────────────────────────────────────────

    @Operation(
        summary      = "Get current user profile",
        description  = "Requires a valid Bearer JWT in the Authorization header.",
        security     = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponse(responseCode = "200",  description = "Profile returned")
    @ApiResponse(responseCode = "401",  description = "Missing or invalid JWT", content = @Content)
    @GetMapping("/me")
    public ResponseEntity<AuthResponse> me(Authentication auth) {
        AuthResponse res = authService.getMe(auth.getName());
        return ResponseEntity.ok(res);
    }

    // ── Refresh Token ────────────────────────────────────────────────────────────

    @Operation(summary = "Refresh access token")
    @ApiResponse(responseCode = "200", description = "New access token returned")
    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshtoken(@Valid @org.springframework.web.bind.annotation.RequestBody com.qma.auth.dto.TokenRefreshRequest request) {
        logger.info("Received refresh token request");
        return ResponseEntity.ok(authService.refreshToken(request));
    }

    // ── Logout ───────────────────────────────────────────────────────────────────

    @Operation(summary = "Logout user and delete refresh token", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200", description = "Logged out successfully")
    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(Authentication authentication) {
        authService.logout(authentication.getName());
        return ResponseEntity.ok(new com.qma.auth.dto.ApiResponse<>(true, "Log out successful!", null));
    }

    // ── Health check ─────────────────────────────────────────────────────────────

    @Operation(summary = "Health check")
    @ApiResponse(responseCode = "200", description = "auth-service is UP")
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("{\"status\":\"auth-service UP\",\"port\":8083}");
    }
}
