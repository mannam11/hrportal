package com.app.hrportal.model;

import com.app.hrportal.enums.JobPostStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "job_posts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobPost {

    @Id
    private String id;
    private String role;
    private String description;
    private String createdBy;
    private List<String> requiredSkills;
    private List<String> preferredSkills;
    private Integer minExperience;
    private Integer maxExperience;
    private Double autoShortlistThreshold;
    private JobPostStatus jobPostStatus;
    private String organizationName;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
