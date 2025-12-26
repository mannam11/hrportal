package com.app.hrportal.exception.jobpost;

import com.app.hrportal.dto.reponse.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class JobPostExceptionHandler {

    @ExceptionHandler(JobPostNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleJobPostExceptionHandler(JobPostNotFoundException ex){
        ErrorResponse response = ErrorResponse.builder()
                .error("NOT_FOUND")
                .message(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

}
