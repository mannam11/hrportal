package com.app.hrportal.service;

import com.app.hrportal.dto.reponse.AnalysisResponse;
import com.app.hrportal.model.Application;
import com.app.hrportal.model.JobPost;

import java.io.InputStream;

public interface ResumeAnalysisService {
    public AnalysisResponse analyse(InputStream inputStream, JobPost jobPost, Application application);
}
