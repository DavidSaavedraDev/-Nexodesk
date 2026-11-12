package com.nexodesk.common.exception;

import java.time.Instant;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    ResponseEntity<ApiError> notFound(ResourceNotFoundException exception) {
        return response(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<ApiError> badRequest(RuntimeException exception) {
        return response(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    ResponseEntity<ApiError> unauthorized(BadCredentialsException exception) {
        return response(HttpStatus.UNAUTHORIZED, "Invalid credentials");
    }

    @ExceptionHandler(AccessDeniedException.class)
    ResponseEntity<ApiError> forbidden(AccessDeniedException exception) {
        return response(HttpStatus.FORBIDDEN, exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ApiError> validation(MethodArgumentNotValidException exception) {
        Map<String, String> fields = exception.getBindingResult().getFieldErrors().stream()
            .collect(java.util.stream.Collectors.toMap(
                error -> error.getField(),
                error -> error.getDefaultMessage() == null ? "Invalid value" : error.getDefaultMessage(),
                (first, ignored) -> first
            ));
        return new ResponseEntity<>(new ApiError(Instant.now(), "Validation failed", fields), HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<ApiError> response(HttpStatus status, String message) {
        return new ResponseEntity<>(new ApiError(Instant.now(), message, Map.of()), status);
    }

    public record ApiError(Instant timestamp, String message, Map<String, String> fields) {
    }
}
