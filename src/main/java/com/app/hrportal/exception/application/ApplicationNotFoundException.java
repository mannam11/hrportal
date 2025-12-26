package com.app.hrportal.exception.application;

public class ApplicationNotFoundException extends RuntimeException{
    public ApplicationNotFoundException(String message){
        super(message);
    }
}
