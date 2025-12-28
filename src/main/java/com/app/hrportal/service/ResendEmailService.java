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
    public void send(SignupEvent signupEvent) {

        String email = signupEvent.getEmail();

        CreateEmailOptions params = CreateEmailOptions.builder()
                .to(signupEvent.getEmail())
                .template(Template.builder()
                        .id("signup-verification")
                        .addVariable("otp",signupEvent.getOtp())
                        .build())
                .build();

        try {
            CreateEmailResponse data = resendClient.emails().send(params);
            System.out.println(data.getId());

            log.info("Email sent to : {}",email);
        } catch (ResendException e) {
            e.printStackTrace();
        }

    }
}
