package com.app.hrportal.service;

import com.app.hrportal.dto.request.DomainEvent;
import com.app.hrportal.utils.QueueNames;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Slf4j
@RequiredArgsConstructor
public class RabbitMqEventPublisher implements EventPublisher{

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void publish(DomainEvent event) {

        String queue = EventRouting.resolveQueue(event.getType());

        log.info("Publishing event {} to queue {}", event.getType(), queue);

        rabbitTemplate.convertAndSend(
                Objects.requireNonNull(queue),
                event
        );
    }
}
