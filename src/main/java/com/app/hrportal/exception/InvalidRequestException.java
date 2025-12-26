package com.app.hrportal.exception;

public class InvalidRequestException extends RuntimeException{
    public InvalidRequestException(String message){
        super(message);
    }
}
