package ru.practicum.shareit.exception;

public class DuplicateDataException extends RuntimeException {
    public DuplicateDataException(String error) {
        super(error);
    }
}
