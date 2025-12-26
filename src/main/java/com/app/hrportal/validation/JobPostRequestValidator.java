package com.app.hrportal.validation;

import com.app.hrportal.dto.request.JobPostRequest;
import com.app.hrportal.exception.ValidationException;

public class JobPostRequestValidator {

    public static void validate(JobPostRequest request){
        if (request == null) {
            throw new ValidationException("Request body cannot be null");
        }

        if (request.getRole() == null || request.getRole().trim().isEmpty()) {
            throw new ValidationException("Role is required");
        }

        if (request.getDescription() == null || request.getDescription().trim().isEmpty()) {
            throw new ValidationException("Description is required");
        }

        if (request.getRequiredSkills() == null || request.getRequiredSkills().isEmpty()) {
            throw new ValidationException("Required skills must not be empty");
        }

        if (request.getMinExperience() == null) {
            throw new ValidationException("Minimum experience is required");
        }

        if (request.getMinExperience() < 0) {
            throw new ValidationException("Minimum experience cannot be negative");
        }

        if (request.getAutoShortlistThreshold() == null) {
            request.setAutoShortlistThreshold(75.0);
        }

        if (request.getAutoShortlistThreshold() < 0 || request.getAutoShortlistThreshold() > 100) {
            throw new ValidationException("autoShortlistThreshold must be between 0 and 100");
        }
    }
}
