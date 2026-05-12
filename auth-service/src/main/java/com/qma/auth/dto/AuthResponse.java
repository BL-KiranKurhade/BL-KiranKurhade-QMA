package com.qma.auth.dto;

public class AuthResponse {
    private boolean success;
    private String message;
    private String token;
    private String tokenType = "Bearer";
    private UserDto user;

    public static AuthResponse ok(String token, UserDto user) {
        AuthResponse r = new AuthResponse();
        r.success = true; r.message = "Success"; r.token = token; r.user = user;
        return r;
    }
    public static AuthResponse err(String message) {
        AuthResponse r = new AuthResponse();
        r.success = false; r.message = message;
        return r;
    }

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public String getToken() { return token; }
    public String getTokenType() { return tokenType; }
    public UserDto getUser() { return user; }

    public static class UserDto {
        private Long id;
        private String username;
        private String email;
        private String role;

        public UserDto(Long id, String username, String email, String role) {
            this.id = id; this.username = username; this.email = email; this.role = role;
        }
        public Long getId() { return id; }
        public String getUsername() { return username; }
        public String getEmail() { return email; }
        public String getRole() { return role; }
    }
}
