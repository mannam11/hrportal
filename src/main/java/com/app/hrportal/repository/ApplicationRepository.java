package com.app.hrportal.repository;

import com.app.hrportal.dto.reponse.ApplicationSourceCount;
import com.app.hrportal.dto.reponse.JobApplicationCount;
import com.app.hrportal.enums.ApplicationStatus;
import com.app.hrportal.model.Application;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ApplicationRepository extends MongoRepository<Application,String> {
    boolean existsByJobIdAndEmailIgnoreCase(String jobId, String email);
    @Aggregation(pipeline = {
            "{ $match: { createdBy: ?0 } }",
            "{ $group: { _id: '$applicationSource', count: { $sum: 1 } } }"
    })
    List<ApplicationSourceCount> countByApplicationSource(String userId);
    List<Application> findByCreatedByAndJobId(
            String userId,
            String jobId
    );

    List<Application> findByCreatedByAndJobIdAndApplicationStatusIn(
            String userId,
            String jobId,
            List<ApplicationStatus> statuses
    );

    @Aggregation(pipeline = {
            "{ $match: { createdBy: ?0 } }",
            "{ $group: { _id: '$jobId', count: { $sum: 1 } } }"
    })
    List<JobApplicationCount> countApplicationsByJobId(String userId);

    List<Application> findByCreatedByAndCreatedAtAfterOrderByCreatedAtAsc(
            String createdBy,
            LocalDateTime after
    );

}
