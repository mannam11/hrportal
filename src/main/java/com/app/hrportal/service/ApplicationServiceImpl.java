package com.app.hrportal.service;

import com.app.hrportal.dto.reponse.ApplicationResponse;
import com.app.hrportal.dto.request.AnalysisRequest;
import com.app.hrportal.dto.request.ApplicationRequest;
import com.app.hrportal.enums.AnalysisStatus;
import com.app.hrportal.enums.ApplicationStatus;
import com.app.hrportal.enums.JobPostStatus;
import com.app.hrportal.exception.ValidationException;
import com.app.hrportal.exception.application.CsvExportException;
import com.app.hrportal.exception.application.DuplicateApplicationException;
import com.app.hrportal.exception.application.InvalidApplicationStateException;
import com.app.hrportal.exception.jobpost.JobPostNotFoundException;
import com.app.hrportal.mapper.ApplicationMapper;
import com.app.hrportal.model.Application;
import com.app.hrportal.model.JobPost;
import com.app.hrportal.repository.ApplicationRepository;
import com.app.hrportal.repository.JobPostRepository;
import com.app.hrportal.validation.ApplicationRequestValidator;
import com.fasterxml.uuid.Generators;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApplicationServiceImpl implements ApplicationService{

    private static final DateTimeFormatter CSV_DATE_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");


    private final ApplicationRepository applicationRepository;
    private final JobPostRepository jobPostRepository;
    private final StorageService storageService;
    private final QueueService queueService;

    @Override
    public Application save(Application application) {
        return applicationRepository.save(application);
    }

    @Override
    public void createApplication(String jobId,
                                                 ApplicationRequest request,
                                                 MultipartFile file) {
        if(file.isEmpty()){
            throw new ValidationException("Resume should not be empty");
        }

        ApplicationRequestValidator.validate(request);

        if (applicationRepository.existsByJobIdAndEmailIgnoreCase(jobId, request.getEmail())) {
            throw new DuplicateApplicationException("You have already applied for this job");
        }

        JobPost jobPost = findOpenJobForApplication(jobId);

        Application application = buildApplication(jobPost.getId(), jobPost.getCreatedBy(),request, jobPost.getRole());

        String resumeKey = storageService.uploadResume(file,jobId,application.getId());

        application.setResumeKey(resumeKey);

        Application savedApplication = save(application);
        log.info("Application saved with id : {}",savedApplication.getId());

        queueService.send(AnalysisRequest.builder()
                .applicationId(savedApplication.getId())
                .build());
    }

    private JobPost findOpenJobForApplication(String jobId) {
        JobPost jobPost = jobPostRepository.findById(jobId)
                .orElseThrow(() ->
                        new JobPostNotFoundException("Invalid application request")
                );

        if (jobPost.getJobPostStatus() != JobPostStatus.OPEN) {
            throw new InvalidApplicationStateException("Applications are closed");
        }

        return jobPost;
    }


    @Override
    public List<ApplicationResponse> getAllByJobId(String userId, String jobId) {
        return applicationRepository.findByCreatedByAndJobId(userId,jobId)
                .stream()
                .map(ApplicationMapper::toResponse)
                .toList();
    }

    @Override
    public void exportToCsvByJobIdAndApplicationStatuses(
            String userId,
            String jobId,
            List<ApplicationStatus> applicationStatuses,
            HttpServletResponse response
    ) {

        try {

            if (applicationStatuses == null || applicationStatuses.isEmpty()) {
                applicationStatuses = List.of(
                        ApplicationStatus.SHORTLISTED,
                        ApplicationStatus.REJECTED
                );
            }

            JobPost jobPost = jobPostRepository
                    .findByIdAndCreatedBy(jobId, userId)
                    .orElseThrow(() -> new CsvExportException("Invalid JobPost"));

            List<Application> applications =
                    applicationRepository
                            .findByCreatedByAndJobIdAndApplicationStatusIn(
                                    userId,
                                    jobId,
                                    applicationStatuses
                            );

            if(applications.isEmpty()){
                throw new CsvExportException("No applications found for jobId : "+jobId);
            }

            response.setContentType("text/csv");
            response.setCharacterEncoding("UTF-8");
            response.setHeader(
                    "Content-Disposition",
                    "attachment; filename=job-" + jobId + "-applications.csv"
            );

            try (PrintWriter writer = response.getWriter()) {

                writer.println("Job ID,Job Role,Full Name,Email,Experience,Status,Source,Applied At");

                for (Application app : applications) {
                    writer.printf(
                            "%s,%s,%s,%s,%d,%s,%s,%s%n",
                            jobId,
                            escape(jobPost.getRole()),
                            escape(app.getFullName()),
                            escape(app.getEmail()),
                            app.getYearsOfExp(),
                            app.getApplicationStatus(),
                            app.getApplicationSource() == null
                            ? "Other"
                            : app.getApplicationSource(),
                            app.getCreatedAt() != null
                                    ? CSV_DATE_FORMAT.format(app.getCreatedAt())
                                    : ""
                    );
                }

                writer.flush();
            }

        } catch (IOException ex) {
            throw new CsvExportException(
                    "Failed to export CSV for jobId = " + jobId,
                    ex
            );
        }
    }

    @Override
    public List<ApplicationResponse> getNewApplicationsAfter(String userId, LocalDateTime after) {
        return applicationRepository
                .findByCreatedByAndCreatedAtAfterOrderByCreatedAtAsc(
                        userId,
                        after
                )
                .stream()
                .map(ApplicationMapper::toResponse)
                .toList();
    }

    @Override
    public ApplicationRequest getApplicationRequest(String jobId) {

        JobPost existingJobPost = jobPostRepository.findById(jobId)
                .orElseThrow(() -> new JobPostNotFoundException("Job post not found"));

        return ApplicationMapper.toRequest(existingJobPost);
    }

    private String escape(String value) {
        if (value == null) return "";

        String sanitized = value;
        if (sanitized.startsWith("=") ||
                sanitized.startsWith("+") ||
                sanitized.startsWith("-") ||
                sanitized.startsWith("@")) {
            sanitized = "'" + sanitized;
        }

        return "\"" + sanitized.replace("\"", "\"\"") + "\"";
    }

    private Application buildApplication(String jobId, String createdBy,ApplicationRequest request,String role){

        LocalDateTime now = LocalDateTime.now();

        return Application.builder()
                .id(Generators.timeBasedEpochGenerator().generate().toString())
                .createdBy(createdBy)
                .fullName(request.getFullName())
                .email(request.getEmail())
                .yearsOfExp(request.getYearsOfExp())
                .currentCtc(request.getCurrentCtc())
                .expectedCtc(request.getExpectedCtc())
                .linkedinUrl(request.getLinkedinUrl())
                .githubUrl(request.getGithubUrl())
                .applicationStatus(ApplicationStatus.RECEIVED)
                .analysisStatus(AnalysisStatus.PENDING)
                .jobId(jobId)
                .role(role)
                .applicationSource(request.getApplicationSource())
                .createdAt(now)
                .updatedAt(now)
                .build();
    }
}
