package com.qma.auth.controller;

import com.qma.auth.dto.*;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

// NOTE: @CrossOrigin intentionally omitted — CORS is handled by the API Gateway
@Tag(name = "Authentication", description = "Register, login and JWT auth — UC21 auth-service")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired private AuthService authService;

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
        @ApiResponse(responseCode = "200", description = "Registered successfully"),
        @ApiResponse(responseCode = "400", description = "Email/username already taken", content = @Content)
    })
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @Valid @org.springframework.web.bind.annotation.RequestBody RegisterRequest req) {
        AuthResponse res = authService.register(req);
        return res.isSuccess()
            ? ResponseEntity.ok(res)
            : ResponseEntity.badRequest().body(res);
    }

    @Operation(summary = "Login with email & password",
        description = "Returns a JWT token. Click **Authorize** at top and paste: `Bearer <token>`")
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
        @ApiResponse(responseCode = "200", description = "Login successful"),
        @ApiResponse(responseCode = "401", description = "Invalid credentials", content = @Content)
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @org.springframework.web.bind.annotation.RequestBody LoginRequest req) {
        AuthResponse res = authService.login(req);
        return res.isSuccess()
            ? ResponseEntity.ok(res)
            : ResponseEntity.status(401).body(res);
    }

    @Operation(
        summary = "Get current user profile",
        description = "Requires Bearer JWT token",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponse(responseCode = "200", description = "User profile returned")
    @GetMapping("/me")
    public ResponseEntity<AuthResponse> me(Authentication auth) {
        AuthResponse res = authService.getMe(auth.getName());
        return ResponseEntity.ok(res);
    }

    @Operation(summary = "Health check")
    @ApiResponse(responseCode = "200", description = "Service is UP")
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("{\"status\":\"auth-service UP\",\"port\":8083}");
    }
}
