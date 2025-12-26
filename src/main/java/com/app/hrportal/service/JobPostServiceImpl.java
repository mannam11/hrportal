package com.app.hrportal.service;

import com.app.hrportal.dto.reponse.JobApplicationCount;
import com.app.hrportal.dto.reponse.JobPostResponse;
import com.app.hrportal.dto.request.JobPostRequest;
import com.app.hrportal.enums.JobPostStatus;
import com.app.hrportal.exception.InvalidRequestException;
import com.app.hrportal.exception.jobpost.JobPostNotFoundException;
import com.app.hrportal.mapper.JobPostMapper;
import com.app.hrportal.model.JobPost;
import com.app.hrportal.repository.ApplicationRepository;
import com.app.hrportal.repository.JobPostRepository;
import com.app.hrportal.validation.JobPostRequestValidator;
import com.fasterxml.uuid.Generators;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class JobPostServiceImpl implements JobPostService{

    private final JobPostRepository jobPostRepository;
    private final ApplicationRepository applicationRepository;

    @Override
    public JobPostResponse createJobPost(String userId,JobPostRequest request) {

        JobPostRequestValidator.validate(request);

        JobPost jobPost = save(buildJobPost(request,userId));
        log.info("Job post created with id : {} successfully",jobPost.getId());

        return JobPostMapper.toResponse(jobPost);
    }

    private JobPost buildJobPost(JobPostRequest request,String createBy){

        LocalDateTime now = LocalDateTime.now();

        return JobPost.builder()
                .id(Generators.timeBasedEpochGenerator().generate().toString())
                .createdBy(createBy)
                .role(request.getRole())
                .description(request.getDescription())
                .preferredSkills(request.getPreferredSkills())
                .requiredSkills(request.getRequiredSkills())
                .minExperience(request.getMinExperience())
                .maxExperience(request.getMaxExperience())
                .autoShortlistThreshold(request.getAutoShortlistThreshold())
                .jobPostStatus(JobPostStatus.OPEN)
                .organizationName(request.getOrganizationName())
                .createdAt(now)
                .updatedAt(now)
                .build();

    }

    @Override
    public JobPost save(JobPost jobPost) {
        return jobPostRepository.save(jobPost);
    }

    @Override
    public JobPost findByIdAndCreatedBy(String id, String userId) {

        Optional<JobPost> optionalJobPost = jobPostRepository.findByIdAndCreatedBy(id,userId);

        if(optionalJobPost.isEmpty()){
            throw new JobPostNotFoundException("Job post not found");
        }

        return optionalJobPost.get();
    }

    @Override
    public String update(String userId,String id, JobPostRequest request) {

        log.info("Updating job post with id : {}",id);

        JobPost jobPost = findByIdAndCreatedBy(id,userId);

        if (jobPost.getJobPostStatus().equals(JobPostStatus.CLOSED)) {
            throw new InvalidRequestException("Cannot update a closed job post");
        }

        Optional.ofNullable(request.getRole()).ifPresent(jobPost::setRole);
        Optional.ofNullable(request.getDescription()).ifPresent(jobPost::setDescription);
        Optional.ofNullable(request.getMinExperience()).ifPresent(jobPost::setMinExperience);
        Optional.ofNullable(request.getMaxExperience()).ifPresent(jobPost::setMaxExperience);
        Optional.ofNullable(request.getPreferredSkills()).ifPresent(jobPost::setPreferredSkills);
        Optional.ofNullable(request.getRequiredSkills()).ifPresent(jobPost::setRequiredSkills);
        Optional.ofNullable(request.getAutoShortlistThreshold()).ifPresent(jobPost::setAutoShortlistThreshold);
        Optional.ofNullable(request.getJobPostStatus()).ifPresent(jobPost::setJobPostStatus);

        jobPost.setUpdatedAt(LocalDateTime.now());

        return save(jobPost).getId();
    }

    @Override
    public List<JobPostResponse> getAllJobPostsByCreatedBy(String userId) {

        log.info("Fetching all posts for user with id : {}",userId);

        List<JobPost> jobPosts = jobPostRepository.findByCreatedBy(userId);

        Map<String, Long> applicationCountMap =
                applicationRepository.countApplicationsByJobId(userId)
                        .stream()
                        .collect(Collectors.toMap(
                                JobApplicationCount::getJobId,
                                JobApplicationCount::getCount
                        ));

        return jobPosts.stream()
                .map(jobPost -> {
                    JobPostResponse response = JobPostMapper.toResponse(jobPost);
                    response.setNoOfApplicationsReceived(
                            applicationCountMap.getOrDefault(jobPost.getId(), 0L)
                    );
                    return response;
                })
                .toList();
    }

}