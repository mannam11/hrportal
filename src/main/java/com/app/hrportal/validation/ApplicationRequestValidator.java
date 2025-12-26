package com.app.hrportal.validation;

import com.app.hrportal.dto.request.ApplicationRequest;
import com.app.hrportal.exception.ValidationException;

public class ApplicationRequestValidator {

    public static void validate(ApplicationRequest request) {

        if (request == null) {
            throw new ValidationException("Request body cannot be null");
        }

        if (request.getFullName() == null || request.getFullName().trim().isEmpty()) {
            throw new ValidationException("Full name is required");
        }

        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new ValidationException("Email is required");
        }
        if (!request.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new ValidationException("Invalid email format");
        }

        if (request.getYearsOfExp() == null || request.getYearsOfExp() < 0) {
            throw new ValidationException("Years of experience cannot be negative");
        }

    }
}

