package com.app.hrportal.dto.reponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardOverviewResponse {
    private Long totalJobPosts;
    private Long activeJobPosts;
    private Long totalApplications;
    private List<ApplicationSourceCount> applicationSourceCounts;
}
