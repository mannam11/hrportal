package com.app.hrportal.service;

import com.app.hrportal.dto.request.AnalysisRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class RabbitMqResumeAnalysisListener {

    private final ConsumerService consumerService;

    @RabbitListener(queues = "${resume.analysis.queue}")
    public void onMessage(AnalysisRequest request) throws IOException {
        log.info("Received message from RabbitMQ: {}", request);
        consumerService.consume(request);
    }
}

