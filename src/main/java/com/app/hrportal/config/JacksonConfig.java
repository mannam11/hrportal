package com.app.hrportal.config;

import com.app.hrportal.dto.request.ApplicationSubmittedEvent;
import com.app.hrportal.dto.request.SignupEvent;
import com.app.hrportal.enums.EventType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();

        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        mapper.registerSubtypes(
                new NamedType(
                        ApplicationSubmittedEvent.class,
                        EventType.APPLICATION_SUBMITTED.name()
                ),
                new NamedType(
                      SignupEvent.class,
                      EventType.SIGNUP.name()
                )
                // add more events here later
        );

        return mapper;
    }
}

