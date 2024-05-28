package ru.practicum.shareit.server.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class AvailableException extends RuntimeException {

    public AvailableException(String message) {
        super(message);
    }
}
