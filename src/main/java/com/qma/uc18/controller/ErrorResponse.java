package com.qma.uc18.controller;

/** Simple error payload returned by AuthController on failures. */
public class ErrorResponse {
    private final String message;
    public ErrorResponse(String message) { this.message = message; }
    public String getMessage() { return message; }
}
