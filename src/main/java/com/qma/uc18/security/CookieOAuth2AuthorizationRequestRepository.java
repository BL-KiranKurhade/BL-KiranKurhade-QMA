package com.qma.uc18.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;

/**
 * Stores the OAuth2 authorization request in a browser cookie instead of the HTTP session.
 *
 * WHY: The default HttpSessionOAuth2AuthorizationRequestRepository fails when the user
 * navigates away (e.g. clicks a Swagger link) between the initial /oauth2/authorization/google
 * redirect and Google's callback — the session changes and the stored state is lost.
 *
 * With cookie-based storage the state survives any navigation because the cookie is sent
 * automatically by the browser on the callback to /login/oauth2/code/google.
 */
@Component
public class CookieOAuth2AuthorizationRequestRepository
        implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    private static final String COOKIE_NAME   = "oauth2_auth_request";
    private static final int    COOKIE_EXPIRY  = 180; // 3 minutes — enough for any login flow

    // ── Save: called when /oauth2/authorization/google is hit ──────────────────
    @Override
    public void saveAuthorizationRequest(
            OAuth2AuthorizationRequest authorizationRequest,
            HttpServletRequest request,
            HttpServletResponse response) {

        if (authorizationRequest == null) {
            deleteCookie(request, response);
            return;
        }
        String serialized = serialize(authorizationRequest);
        Cookie cookie = new Cookie(COOKIE_NAME, serialized);
        cookie.setPath("/");
        cookie.setHttpOnly(true);          // not accessible from JS
        cookie.setMaxAge(COOKIE_EXPIRY);
        // SameSite=Lax allows the cookie to be sent on top-level cross-site GET
        // (needed for Google's redirect back to /login/oauth2/code/google)
        response.addHeader("Set-Cookie",
            COOKIE_NAME + "=" + serialized +
            "; Path=/; HttpOnly; Max-Age=" + COOKIE_EXPIRY + "; SameSite=Lax");
    }

    // ── Load: called on every request to check saved state ────────────────────
    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        return getCookieValue(request)
                .map(this::deserialize)
                .orElse(null);
    }

    // ── Remove: called after successful/failed callback (one-time use) ─────────
    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(
            HttpServletRequest request,
            HttpServletResponse response) {
        OAuth2AuthorizationRequest authRequest = loadAuthorizationRequest(request);
        deleteCookie(request, response);
        return authRequest;
    }

    // ── Helpers ────────────────────────────────────────────────────────────────
    private Optional<String> getCookieValue(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return Optional.empty();
        return Arrays.stream(cookies)
                .filter(c -> COOKIE_NAME.equals(c.getName()))
                .map(Cookie::getValue)
                .findFirst();
    }

    private void deleteCookie(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return;
        Arrays.stream(cookies)
              .filter(c -> COOKIE_NAME.equals(c.getName()))
              .forEach(c -> {
                  Cookie del = new Cookie(COOKIE_NAME, "");
                  del.setPath("/");
                  del.setMaxAge(0);
                  response.addCookie(del);
              });
    }

    /** Java serialization — OAuth2AuthorizationRequest implements Serializable */
    private String serialize(OAuth2AuthorizationRequest req) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(req);
            return Base64.getUrlEncoder().encodeToString(bos.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Failed to serialize OAuth2AuthorizationRequest", e);
        }
    }

    private OAuth2AuthorizationRequest deserialize(String value) {
        try {
            byte[] bytes = Base64.getUrlDecoder().decode(value);
            try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
                 ObjectInputStream  ois = new ObjectInputStream(bis)) {
                return (OAuth2AuthorizationRequest) ois.readObject();
            }
        } catch (IOException | ClassNotFoundException e) {
            return null; // invalid/expired cookie — treat as no saved request
        }
    }
}
