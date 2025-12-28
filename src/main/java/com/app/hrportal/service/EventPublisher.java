package com.app.hrportal.service;

import com.app.hrportal.dto.request.DomainEvent;

public interface EventPublisher {
    public void publish(DomainEvent event);
}
