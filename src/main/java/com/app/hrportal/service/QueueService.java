package com.app.hrportal.service;

import com.app.hrportal.dto.request.AnalysisRequest;

public interface QueueService {
    public void send(AnalysisRequest request);
}
