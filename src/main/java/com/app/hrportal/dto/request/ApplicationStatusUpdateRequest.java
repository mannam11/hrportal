package com.app.hrportal.dto.request;

import com.app.hrportal.enums.ApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationStatusUpdateRequest {
    private ApplicationStatus status;
}

