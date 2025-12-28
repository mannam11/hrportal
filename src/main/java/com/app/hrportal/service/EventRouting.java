package com.app.hrportal.service;

import com.app.hrportal.enums.EventType;
import com.app.hrportal.utils.QueueNames;


public final class EventRouting {
    private EventRouting() {}

    public static String resolveQueue(EventType type) {
        return switch (type) {
            case APPLICATION_SUBMITTED -> QueueNames.APPLICATION_EVENTS;
            case SIGNUP -> QueueNames.EMAIL_EVENTS;
            default -> null;
        };
    }
}
