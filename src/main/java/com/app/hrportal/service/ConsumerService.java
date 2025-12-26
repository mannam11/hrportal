package com.app.hrportal.service;

import software.amazon.awssdk.services.sqs.model.Message;

import java.io.IOException;

public interface ConsumerService {
    public void consume(Message message) throws IOException;
}
