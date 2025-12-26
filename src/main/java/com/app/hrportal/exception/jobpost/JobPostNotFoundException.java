package com.app.hrportal.exception.jobpost;

public class JobPostNotFoundException extends RuntimeException{
    public JobPostNotFoundException(String message){
        super(message);
    }
}
