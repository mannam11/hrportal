package com.app.hrportal.dto.reponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobApplicationCount {

    @Field("_id")
    private String jobId;
    private long count;
}

