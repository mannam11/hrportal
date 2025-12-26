package com.app.hrportal.exception.application;

public class InvalidApplicationStateException extends RuntimeException{
    public InvalidApplicationStateException(String message){
        super(message);
    }
}
