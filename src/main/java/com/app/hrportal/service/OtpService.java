package com.app.hrportal.service;

import com.app.hrportal.dto.request.OtpRequest;
import com.app.hrportal.dto.request.VerifyOtpRequest;

public interface OtpService {
    void verifySignupOtp(VerifyOtpRequest request);
    void requestOtp(OtpRequest request);
}

