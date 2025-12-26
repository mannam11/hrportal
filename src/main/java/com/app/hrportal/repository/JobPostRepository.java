package com.app.hrportal.repository;

import com.app.hrportal.enums.JobPostStatus;
import com.app.hrportal.model.JobPost;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface JobPostRepository extends MongoRepository<JobPost, String> {
    long countByCreatedByAndJobPostStatus(String userId,JobPostStatus status);
    long countByCreatedBy(String createdBy);
    Optional<JobPost> findByIdAndCreatedBy(String id, String createdBy);
    List<JobPost> findByCreatedBy(String createdBy);
}