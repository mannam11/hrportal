package com.app.hrportal.mapper;

import com.app.hrportal.dto.reponse.JobPostResponse;
import com.app.hrportal.model.JobPost;

public class JobPostMapper {

    public static JobPostResponse toResponse(JobPost jobPost){
        return JobPostResponse.builder()
                .id(jobPost.getId())
                .role(jobPost.getRole())
                .description(jobPost.getDescription())
                .preferredSkills(jobPost.getPreferredSkills())
                .requiredSkills(jobPost.getRequiredSkills())
                .minExperience(jobPost.getMinExperience())
                .maxExperience(jobPost.getMaxExperience())
                .createdAt(jobPost.getCreatedAt())
                .jobPostStatus(jobPost.getJobPostStatus())
                .autoShortlistThreshold(jobPost.getAutoShortlistThreshold())
                .organizationName(jobPost.getOrganizationName())
                .build();
    }
}
