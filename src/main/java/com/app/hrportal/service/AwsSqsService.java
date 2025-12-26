package com.app.hrportal.service;

import com.app.hrportal.dto.request.AnalysisRequest;
import com.app.hrportal.exception.application.QueueSendException;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AwsSqsService implements QueueService{

    private final SqsTemplate sqsClient;

    @Value("${aws.sqs.jobQueueUrl}")
    private String jobQueueUrl;

    @Override
    public void send(AnalysisRequest request) {
        try {

            var result = sqsClient.send(to -> to
                    .queue(jobQueueUrl)
                    .payload(request)
            );

            log.info("Message sent with ID: {}", result.messageId());

        } catch (Exception ex) {
            log.error("Failed to send job {}", request.getApplicationId(), ex);
            throw new QueueSendException("Failed to send job to SQS", ex);
        }
    }
}
