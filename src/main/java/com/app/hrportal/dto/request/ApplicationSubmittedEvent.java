package com.app.hrportal.dto.request;

import com.app.hrportal.enums.EventType;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class ApplicationSubmittedEvent extends DomainEvent {
    private String applicationId;
    private String email;

    public ApplicationSubmittedEvent(String applicationId, String email) {
        super(EventType.APPLICATION_SUBMITTED);
        this.applicationId = applicationId;
        this.email = email;
    }
}
