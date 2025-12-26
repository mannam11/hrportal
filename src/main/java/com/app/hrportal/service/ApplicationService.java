package com.app.hrportal.service;

import com.app.hrportal.dto.reponse.ApplicationResponse;
import com.app.hrportal.dto.request.ApplicationRequest;
import com.app.hrportal.enums.ApplicationStatus;
import com.app.hrportal.model.Application;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

public interface ApplicationService {
    public Application save(Application application);
    public void createApplication(String jobId,
                                                 ApplicationRequest request,
                                                 MultipartFile file);
    public List<ApplicationResponse> getAllByJobId(String userId,String jobId);
    void exportToCsvByJobIdAndApplicationStatuses(String userId,
                                                  String jobId,
                                                  List<ApplicationStatus> applicationStatuses,
                                                  HttpServletResponse response);
    List<ApplicationResponse> getNewApplicationsAfter(
            String userId,
            LocalDateTime after
    );
    ApplicationRequest getApplicationRequest(String jobId);
}
