package com.app.hrportal.model;

import com.app.hrportal.enums.AnalysisStatus;
import com.app.hrportal.enums.ApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "applications")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Application {

    @Id
    private String id;
    private String jobId;
    private String role;
    private String createdBy;
    private String fullName;
    private String email;
    private Integer yearsOfExp;
    private Double currentCtc;
    private Double expectedCtc;
    private String resumeKey;
    private String linkedinUrl;
    private String githubUrl;
    private String applicationSource;
    private ApplicationStatus applicationStatus;
    private AnalysisStatus analysisStatus;
    private AnalysisResult analysisResult;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AnalysisResult {
        private Double requiredSkillsMatchPercentage;
        private Double preferredSkillsMatchPercentage;
        private Double overallFitPercentage;

        private List<String> matchedRequiredSkills;
        private List<String> missingRequiredSkills;

        private List<String> matchedPreferredSkills;
        private List<String> missingPreferredSkills;
    }

}
