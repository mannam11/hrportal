package com.app.hrportal.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

public interface StorageService {
    String uploadResume(MultipartFile file,String jobId, String applicationId);
    InputStream getResume(String resumeKey);
}
