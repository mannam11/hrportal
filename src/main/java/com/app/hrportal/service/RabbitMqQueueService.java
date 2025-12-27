package com.app.hrportal.service;

import com.app.hrportal.dto.request.AnalysisRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RabbitMqQueueService implements QueueService{

    @Value("${resume.analysis.queue}")
    private String resumeAnalysisQueue;
    private final RabbitTemplate rabbitTemplate;

    @Override
    public void send(AnalysisRequest request) {
        rabbitTemplate.convertAndSend(resumeAnalysisQueue,request);
    }
}
