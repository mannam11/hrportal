package com.app.hrportal.service;

import com.app.hrportal.dto.request.SignupEvent;
import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import com.resend.services.emails.model.Template;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ResendEmailService implements EmailService{

    private final Resend resendClient;

    @Override
    public void send(CreateEmailOptions params) {

        try {
            CreateEmailResponse data = resendClient.emails().send(params);

            log.info("Email sent to : {}",params.getTo());
        } catch (ResendException e) {
            e.printStackTrace();
        }

    }
}
