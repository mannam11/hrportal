package com.app.hrportal.dto.request;

import lombok.Data;

@Data
public class VerifyOtpRequest {
    private String email;
    private Integer otp;
}

