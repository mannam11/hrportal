package com.app.hrportal.exception.application;

import com.app.hrportal.dto.reponse.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(DuplicateApplicationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(DuplicateApplicationException ex) {
        ErrorResponse response = ErrorResponse.builder()
                .error("DUPLICATE_APPLICATION_FOUND")
                .message(ex.getMessage())
                .build();

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(StorageException.class)
    public ResponseEntity<ErrorResponse> handleStorageException(StorageException ex) {
        ErrorResponse response = ErrorResponse.builder()
                .error("STORAGE_ERROR")
                .message(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(ApplicationNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleApplicationNotFoundException(ApplicationNotFoundException ex) {
        ErrorResponse response = ErrorResponse.builder()
                .error("APPLICATION_NOT_FOUND")
                .message(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(QueueSendException.class)
    public ResponseEntity<ErrorResponse> handleQueueSendException(QueueSendException ex){
        ErrorResponse response = ErrorResponse.builder()
                .message(ex.getMessage())
                .error("QUEUE_SEND_ERROR")
                .build();

        return ResponseEntity.internalServerError().body(response);
    }

    @ExceptionHandler(InvalidApplicationStateException.class)
    public ResponseEntity<ErrorResponse> handleInvalidApplicationStateException(InvalidApplicationStateException ex){
        ErrorResponse response = ErrorResponse.builder()
                .message(ex.getMessage())
                .error("INVALID_APPLICATION_STATE")
                .build();

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(CsvExportException.class)
    public ResponseEntity<ErrorResponse> handleCsvExportException(CsvExportException ex){
        ErrorResponse response = ErrorResponse.builder()
                .message(ex.getMessage())
                .error("CSV_EXPORT_ERROR")
                .build();

        return ResponseEntity.internalServerError().body(response);
    }

}
