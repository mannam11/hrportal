package com.app.hrportal.service;

import com.app.hrportal.dto.request.SignupEvent;
import com.app.hrportal.enums.EventType;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.Template;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SignupEventProcessor implements EventProcessor<SignupEvent> {

    @Value("${resend.email-templates.signup.name}")
    private String templateName;

    @Value("${resend.email-templates.signup.variables[0]}")
    private String otpVariable;

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
                        .id(templateName)
                        .addVariable(otpVariable,event.getOtp())
                        .build())
                .build();

        emailService.send(params);
    }
}
