package com.app.hrportal.service;

import com.app.hrportal.dto.reponse.AnalysisResponse;
import com.app.hrportal.dto.reponse.ScoreResult;
import com.app.hrportal.dto.request.ApplicationSubmittedEvent;
import com.app.hrportal.enums.AnalysisStatus;
import com.app.hrportal.enums.ApplicationStatus;
import com.app.hrportal.exception.application.ApplicationNotFoundException;
import com.app.hrportal.exception.jobpost.JobPostNotFoundException;
import com.app.hrportal.mapper.ApplicationMapper;
import com.app.hrportal.model.Application;
import com.app.hrportal.model.JobPost;
import com.app.hrportal.repository.ApplicationRepository;
import com.app.hrportal.repository.JobPostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class ApplicationAnalysisService {

    private final ResumeAnalysisService resumeAnalysisService;
    private final ApplicationService applicationService;
    private final StorageService storageService;
    private final MatchingScoreService matchingScoreService;
    private final JobPostRepository jobPostRepository;
    private final ApplicationRepository applicationRepository;

    public void analyse(ApplicationSubmittedEvent event) throws IOException {

        String applicationId = event.getApplicationId();
        log.info("Processing application {}", applicationId);

        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ApplicationNotFoundException("Application not found"));

        if (application.getAnalysisStatus() == AnalysisStatus.COMPLETED) {
            log.info("Application {} already analysed. Skipping.", applicationId);
            return;
        }

        application.setAnalysisStatus(AnalysisStatus.PROCESSING);
        application.setUpdatedAt(LocalDateTime.now());
        applicationService.save(application);

        JobPost jobPost = jobPostRepository.findById(application.getJobId())
                .orElseThrow(() -> new JobPostNotFoundException("Job post not found"));

        try (InputStream resumeInputStream =
                     storageService.getResume(application.getResumeKey())) {

            AnalysisResponse response =
                    resumeAnalysisService.analyse(
                            resumeInputStream,
                            jobPost,
                            application
                    );

            if (response == null) {
                application.setAnalysisStatus(AnalysisStatus.FAILED);
                application.setUpdatedAt(LocalDateTime.now());
                applicationService.save(application);
                return;
            }

            buildApplicationWithResult(response, jobPost, application);
            applicationService.save(application);

            log.info("Analysis completed for application {}", applicationId);

        } catch (Exception ex) {
            log.error("Error processing application {}", applicationId, ex);
            throw ex;
        }
    }

    private void buildApplicationWithResult(
            AnalysisResponse response,
            JobPost jobPost,
            Application application
    ) {
        Application.AnalysisResult analysisResult =
                ApplicationMapper.toAnalysisResult(response);

        ScoreResult scoreResult =
                matchingScoreService.calculate(analysisResult, jobPost);

        analysisResult.setRequiredSkillsMatchPercentage(
                scoreResult.getRequiredSkillsMatchPercentage()
        );
        analysisResult.setPreferredSkillsMatchPercentage(
                scoreResult.getPreferredSkillsMatchPercentage()
        );
        analysisResult.setOverallFitPercentage(
                scoreResult.getOverallFitPercentage()
        );

        application.setAnalysisResult(analysisResult);

        if (analysisResult.getOverallFitPercentage() < jobPost.getAutoShortlistThreshold()
                || application.getYearsOfExp() < jobPost.getMinExperience()) {
            application.setApplicationStatus(ApplicationStatus.REJECTED);
        } else {
            application.setApplicationStatus(ApplicationStatus.SHORTLISTED);
        }

        application.setAnalysisStatus(AnalysisStatus.COMPLETED);
        application.setUpdatedAt(LocalDateTime.now());
    }
}

