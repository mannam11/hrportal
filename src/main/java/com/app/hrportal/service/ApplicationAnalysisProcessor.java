package com.app.hrportal.service;

import com.app.hrportal.dto.request.ApplicationSubmittedEvent;
import com.app.hrportal.enums.EventType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class ApplicationAnalysisProcessor implements EventProcessor<ApplicationSubmittedEvent>{

    private final ApplicationAnalysisService applicationAnalysisService;

    @Override
    public EventType getType() {
        return EventType.APPLICATION_SUBMITTED;
    }

    @Override
    public void process(ApplicationSubmittedEvent event) {
        try{
            applicationAnalysisService.analyse(event);
        }catch (IOException ex){
            log.info("Error while analysing application with id : {}",event.getApplicationId(),ex);
        }
    }
}
