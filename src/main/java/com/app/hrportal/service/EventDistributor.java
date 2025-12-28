package com.app.hrportal.service;

import com.app.hrportal.dto.request.DomainEvent;
import com.app.hrportal.enums.EventType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class EventDistributor {
    private final Map<EventType, List<EventProcessor<?>>> processorMap;

    public EventDistributor(List<EventProcessor<?>> processors) {
        this.processorMap = processors.stream()
                .collect(Collectors.groupingBy(EventProcessor::getType));
    }

    @SuppressWarnings("unchecked")
    public <T extends DomainEvent> void distribute(T event) {
        List<EventProcessor<?>> processors =
                processorMap.getOrDefault(event.getType(), List.of());

        if (processors.isEmpty()) {
            // optional: log / metric
            return;
        }

        for (EventProcessor<?> processor : processors) {
            ((EventProcessor<T>) processor).process(event);
        }
    }
}
