package com.app.hrportal.config;

import com.app.hrportal.utils.QueueNames;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue applicationsEventsQueue(){
        return new Queue(QueueNames.APPLICATION_EVENTS,true);
    }

    @Bean
    public Queue emailEventsQueue(){
        return new Queue(QueueNames.EMAIL_EVENTS,true);
    }

    @Bean
    public MessageConverter jsonMessageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }

}
