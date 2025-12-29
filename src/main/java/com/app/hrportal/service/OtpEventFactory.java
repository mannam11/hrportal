package com.app.hrportal.service;

import com.app.hrportal.dto.request.SignupEvent;
import com.app.hrportal.enums.OtpPurpose;
import org.springframework.stereotype.Component;

import static com.app.hrportal.enums.EventType.SIGNUP;

@Component
public class OtpEventFactory {

    public Object createEvent(String email, Integer otp, OtpPurpose purpose) {

        return switch (purpose) {
            case SIGNUP ->
                    new SignupEvent(email, otp);
        };
    }
}

