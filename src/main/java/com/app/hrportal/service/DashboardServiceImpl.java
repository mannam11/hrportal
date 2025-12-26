package com.app.hrportal.service;

import com.app.hrportal.dto.reponse.ApplicationSourceCount;
import com.app.hrportal.dto.reponse.DashboardOverviewResponse;
import com.app.hrportal.enums.JobPostStatus;
import com.app.hrportal.repository.ApplicationRepository;
import com.app.hrportal.repository.JobPostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardServiceImpl implements DashboardService{

    private final ApplicationRepository applicationRepository;
    private final JobPostRepository jobPostRepository;

    @Override
    public DashboardOverviewResponse getDashboardOverview(String userId) {

        long totalJobPosts = jobPostRepository.countByCreatedBy(userId);
        long activeJobPosts = jobPostRepository.countByCreatedByAndJobPostStatus(userId,JobPostStatus.OPEN);

        List<ApplicationSourceCount> applicationSourceCounts = applicationRepository.countByApplicationSource(userId);

        long totalApplications = applicationSourceCounts.stream().mapToLong(ApplicationSourceCount::getCount).sum();

        return DashboardOverviewResponse.builder()
                .activeJobPosts(activeJobPosts)
                .totalJobPosts(totalJobPosts)
                .totalApplications(totalApplications)
                .applicationSourceCounts(applicationSourceCounts)
                .build();
    }
}
