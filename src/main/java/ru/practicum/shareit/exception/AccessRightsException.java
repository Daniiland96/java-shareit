package ru.practicum.shareit.exception;

public class AccessRightsException extends RuntimeException {
    public AccessRightsException(String error) {
        super(error);
    }
}