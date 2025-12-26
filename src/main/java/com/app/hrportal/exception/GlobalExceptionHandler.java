package com.app.hrportal.exception;

import com.app.hrportal.dto.reponse.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(ValidationException ex) {
        ErrorResponse response = ErrorResponse.builder()
                .error("VALIDATION_FAILED")
                .message(ex.getMessage())
                .build();

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(InvalidException.class)
    public ResponseEntity<ErrorResponse> handleInvalidException(InvalidException ex) {
        ErrorResponse response = ErrorResponse.builder()
                .error("INVALID")
                .message(ex.getMessage())
                .build();

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<ErrorResponse> handleInvalidRequestException(InvalidRequestException ex) {
        ErrorResponse response = ErrorResponse.builder()
                .error("INVALID_REQUEST")
                .message(ex.getMessage())
                .build();

        return ResponseEntity.badRequest().body(response);
    }
}
