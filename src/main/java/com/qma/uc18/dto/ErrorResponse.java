package com.qma.uc18.dto;

/** Generic error payload for API responses. */
public class ErrorResponse {
    private final String message;
    public ErrorResponse(String message) { this.message = message; }
    public String getMessage() { return message; }
}
