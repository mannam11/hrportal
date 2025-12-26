package com.app.hrportal.dto.reponse;

import com.app.hrportal.enums.JobPostStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobPostResponse {
    private String id;
    private String role;
    private String description;
    private List<String> requiredSkills;
    private List<String> preferredSkills;
    private Integer minExperience;
    private Integer maxExperience;
    private LocalDateTime createdAt;
    private Double autoShortlistThreshold;
    private JobPostStatus jobPostStatus;
    private Long noOfApplicationsReceived;
    private String organizationName;
}
