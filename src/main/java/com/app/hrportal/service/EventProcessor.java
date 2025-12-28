package com.app.hrportal.service;

import com.app.hrportal.dto.request.DomainEvent;
import com.app.hrportal.enums.EventType;

public interface EventProcessor<T extends DomainEvent> {
    EventType getType();
    void process(T event);
}
