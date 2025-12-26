package com.app.hrportal.exception.application;

public class DuplicateApplicationException extends RuntimeException{
    public DuplicateApplicationException(String message){
        super(message);
    }
}
