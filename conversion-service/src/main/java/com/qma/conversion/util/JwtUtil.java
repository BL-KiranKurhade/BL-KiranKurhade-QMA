package com.qma.conversion.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;

/**
 * Utility to extract the authenticated username from the JWT Bearer token
 * present in the Authorization header.
 *
 * Uses the same secret as auth-service — no network call needed.
 * The API Gateway already ensures only valid tokens reach this service.
 */
@Component
public class JwtUtil {

    private final Key signingKey;

    public JwtUtil(@Value("${app.jwt.secret}") String secret) {
        this.signingKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Extracts the username (JWT subject) from the Authorization header.
     *
     * @param request incoming HTTP request
     * @return username string, or "anonymous" if token is absent / invalid
     */
    public String extractUsername(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            return "anonymous";
        }
        try {
            String token = header.substring(7);
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(signingKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject();
        } catch (Exception e) {
            return "anonymous";
        }
    }
}
