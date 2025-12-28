package com.app.hrportal.exception.user;

import com.app.hrportal.dto.reponse.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserExceptionHandler {

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex){
        ErrorResponse response = ErrorResponse.builder()
                .message(ex.getMessage())
                .error("EMAIL_ALREADY_EXISTS")
                .build();

        return ResponseEntity.badRequest().body(response);
    }

}
