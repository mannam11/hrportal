package com.app.hrportal.service;

import com.app.hrportal.dto.request.SignupEvent;
import com.app.hrportal.enums.EventType;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.Template;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SignupEventProcessor implements EventProcessor<SignupEvent> {

    private final EmailService emailService;

    @Override
    public EventType getType() {
        return EventType.SIGNUP;
    }

    @Override
    public void process(SignupEvent event) {

        CreateEmailOptions params = CreateEmailOptions.builder()
                .to(event.getEmail())
                .template(Template.builder()
                        .id("signup-verification")
                        .addVariable("otp",event.getOtp())
                        .build())
                .build();

        emailService.send(params);
    }
}
