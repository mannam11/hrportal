package com.app.hrportal.dto.request;

import com.app.hrportal.enums.EventType;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class SignupEvent extends DomainEvent {
    private String email;
    private Integer otp;

    public SignupEvent(String email, Integer otp) {
        super(EventType.SIGNUP);
        this.email = email;
        this.otp = otp;
    }
}
