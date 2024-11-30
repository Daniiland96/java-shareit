package ru.practicum.shareit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            MissingRequestHeaderException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(final Exception e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler({NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(final Exception e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler({DuplicateDataException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleDuplicateDataException(final Exception e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler({AccessRightsException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleAccessRightsException(final Exception e) {
        return new ErrorResponse(e.getMessage());
    }
}
