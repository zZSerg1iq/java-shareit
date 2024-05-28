package ru.practicum.shareit.server.advice;

import lombok.Getter;

import java.util.Map;

@Getter
public class ErrorResponse {
    private final String message;
    private final Map<String, String> errors;

    public ErrorResponse(String message, Map<String, String> errors) {
        this.message = message;
        this.errors = errors;
    }
}
