package com.app.hrportal.mapper;

import com.app.hrportal.dto.reponse.AnalysisResponse;
import com.app.hrportal.dto.reponse.ApplicationResponse;
import com.app.hrportal.dto.request.ApplicationRequest;
import com.app.hrportal.model.Application;
import com.app.hrportal.model.JobPost;

import java.util.List;

public class ApplicationMapper {

    public static ApplicationResponse toResponse(Application application){

        return ApplicationResponse.builder()
                .applicationId(application.getId())
                .jobId(application.getJobId())
                .role(application.getRole())
                .email(application.getEmail())
                .fullName(application.getFullName())
                .applicationStatus(application.getApplicationStatus().name())
                .createdAt(application.getCreatedAt())
                .build();

    }

    public static Application.AnalysisResult toAnalysisResult(AnalysisResponse response) {
        return Application.AnalysisResult.builder()
                .matchedRequiredSkills(
                        defaultList(response.getMatchedRequiredSkills())
                )
                .missingRequiredSkills(
                        defaultList(response.getMissingRequiredSkills())
                )
                .matchedPreferredSkills(
                        defaultList(response.getMatchedPreferredSkills())
                )
                .missingPreferredSkills(
                        defaultList(response.getMissingPreferredSkills())
                )
                .build();
    }

    private static List<String> defaultList(List<String> list) {
        return list == null ? List.of() : list;
    }

    public static ApplicationRequest toRequest(JobPost jobPost){
        return ApplicationRequest.builder()
                .role(jobPost.getRole())
                .build();
    }
}
