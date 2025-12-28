package com.app.hrportal.service;

import com.app.hrportal.dto.request.DomainEvent;
import com.app.hrportal.utils.QueueNames;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class EmailQueueListener {

    private final EventDistributor eventDistributor;

    @RabbitListener(queues = QueueNames.EMAIL_EVENTS)
    public void distribute(DomainEvent event){
        log.info("Received event {} from emails queue", event.getType());
        eventDistributor.distribute(event);
    }
}
