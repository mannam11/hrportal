package com.app.hrportal.dto.request;

import com.app.hrportal.enums.JobPostStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobPostRequest {
    private String role;
    private String description;
    private List<String> requiredSkills;
    private List<String> preferredSkills;
    private Integer minExperience;
    private Integer maxExperience;
    private Double autoShortlistThreshold;
    private JobPostStatus jobPostStatus;
    private String organizationName;
}
