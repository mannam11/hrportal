package com.app.hrportal.controller;

import com.app.hrportal.dto.reponse.ApplicationResponse;
import com.app.hrportal.dto.reponse.JobPostResponse;
import com.app.hrportal.dto.request.ApplicationRequest;
import com.app.hrportal.dto.request.JobPostRequest;
import com.app.hrportal.enums.ApplicationStatus;
import com.app.hrportal.exception.InvalidRequestException;
import com.app.hrportal.mapper.JobPostMapper;
import com.app.hrportal.model.User;
import com.app.hrportal.service.ApplicationService;
import com.app.hrportal.service.JobPostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/job-posts")
public class JobPostController {

    private final JobPostService jobPostService;
    private final ApplicationService applicationService;
    private final ObjectMapper objectMapper;

    @PostMapping
    public ResponseEntity<JobPostResponse> create(@AuthenticationPrincipal User currentUser,
                                                  @RequestBody JobPostRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(jobPostService.createJobPost(currentUser.getId(), request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobPostResponse> getById( @AuthenticationPrincipal User currentUser,
                                                    @PathVariable String id){
        return ResponseEntity.ok(JobPostMapper.toResponse(jobPostService.findByIdAndCreatedBy(id, currentUser.getId())));
    }

    @PostMapping("/{id}")
    public ResponseEntity<String> update(
            @AuthenticationPrincipal User currentUser,
            @PathVariable String id,
            @RequestBody JobPostRequest request
    ) {
        return ResponseEntity.ok(
                jobPostService.update(
                        currentUser.getId(),
                        id,
                        request
                )
        );
    }

    @GetMapping("/{id}/apply")
    public ResponseEntity<ApplicationRequest> getApplicationRequest(@PathVariable("id") String jobId){
        return ResponseEntity.ok(applicationService.getApplicationRequest(jobId));
    }

    @PostMapping(value = "/{id}/apply", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> createApplication(
            @PathVariable String id,
            @RequestPart("request") String requestJson,
            @RequestPart("file") MultipartFile file) {


        ApplicationRequest request;
        try {
            request = objectMapper.readValue(requestJson, ApplicationRequest.class);
        } catch (Exception e) {
            throw new InvalidRequestException("Invalid application request payload");
        }

        applicationService.createApplication(id, request, file);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{id}/applications")
    public ResponseEntity<List<ApplicationResponse>> getAllApplicationsByJobId(
            @AuthenticationPrincipal User currentUser,
            @PathVariable String id
    ){
        return ResponseEntity.ok(
                applicationService.getAllByJobId(
                        currentUser.getId(),
                        id
                )
        );
    }


    @GetMapping
    public ResponseEntity<List<JobPostResponse>> getAllJobPostsByCreatedBy(
            @AuthenticationPrincipal User currentUser
    ){
        return ResponseEntity.ok(
                jobPostService.getAllJobPostsByCreatedBy(
                        currentUser.getId()
                )
        );
    }


    @GetMapping("/{id}/applications/export")
    public void exportToCsvByJobIdAndStatuses(
            @AuthenticationPrincipal User currentUser,
            @PathVariable("id") String jobId,
            @RequestParam(required = false) List<ApplicationStatus> status,
            HttpServletResponse response
    ) {

        applicationService.exportToCsvByJobIdAndApplicationStatuses(
                currentUser.getId(),
                jobId,
                status,
                response
        );
    }

}