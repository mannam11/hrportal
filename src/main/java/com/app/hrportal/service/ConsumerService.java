package com.app.hrportal.service;

import com.app.hrportal.dto.request.AnalysisRequest;

import java.io.IOException;

public interface ConsumerService {
    public void consume(AnalysisRequest request) throws IOException;
}
