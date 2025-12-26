package com.app.hrportal.dto.reponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationResponse {
    private String applicationId;
    private String jobId;
    private String role;
    private String fullName;
    private String email;
    private String applicationStatus;
    private LocalDateTime createdAt;
}
