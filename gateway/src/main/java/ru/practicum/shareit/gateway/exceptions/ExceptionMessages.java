package ru.practicum.shareit.gateway.exceptions;

public enum ExceptionMessages {
    VALIDATE_EXCEPTION("bad data"),

    ITEM_NOT_FOUND("Item not found"),
    USER_NOT_FOUND("User not found"),
    BOOKING_NOT_FOUND("Booking not found"),

    USER_DUPLICATE_EMAIL("User with this email already exists"),
    BOOKING_ALREADY_EXISTS("Booking already exists"),
    BOOKING_ALREADY_CANCELED("Booking already canceled"),

    ITEM_NOT_AVAILABLE("Item not available");


    private String message;

    ExceptionMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return this.message;
    }

}
