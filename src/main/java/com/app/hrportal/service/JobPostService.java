package com.app.hrportal.service;

import com.app.hrportal.dto.reponse.JobPostResponse;
import com.app.hrportal.dto.request.JobPostRequest;
import com.app.hrportal.model.JobPost;

import java.util.List;

public interface JobPostService {
    public JobPostResponse createJobPost(String userId, JobPostRequest request);
    public JobPost save(JobPost jobPost);
    public JobPost findByIdAndCreatedBy(String id,String userId);
    public String update(String userId,String id,JobPostRequest request);
    public List<JobPostResponse> getAllJobPostsByCreatedBy(String userId);
}
