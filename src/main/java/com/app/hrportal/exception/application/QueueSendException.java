package com.app.hrportal.exception.application;

public class QueueSendException extends RuntimeException{
    public QueueSendException(String message){
        super(message);
    }

    public QueueSendException(String message,Throwable throwable){
        super(message,throwable);
    }
}
