package com.app.hrportal.service;

import com.app.hrportal.dto.reponse.DashboardOverviewResponse;

public interface DashboardService {
    public DashboardOverviewResponse getDashboardOverview(String userId);
}
