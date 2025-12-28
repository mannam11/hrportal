package com.app.hrportal.service;

import com.app.hrportal.dto.request.SignupEvent;
import com.resend.services.emails.model.CreateEmailOptions;

public interface EmailService {
    public void send(CreateEmailOptions params);
}
