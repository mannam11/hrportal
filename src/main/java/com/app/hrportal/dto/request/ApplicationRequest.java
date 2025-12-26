package com.app.hrportal.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationRequest {
    private String role;
    private String fullName;
    private String email;
    private Integer yearsOfExp;
    private Double currentCtc;
    private Double expectedCtc;
    private String linkedinUrl;
    private String githubUrl;
    private String applicationSource;
}
