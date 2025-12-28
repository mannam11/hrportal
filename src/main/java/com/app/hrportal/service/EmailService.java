package com.app.hrportal.service;

import com.app.hrportal.dto.request.SignupEvent;

public interface EmailService {
    public void send(SignupEvent signupEvent);
}
