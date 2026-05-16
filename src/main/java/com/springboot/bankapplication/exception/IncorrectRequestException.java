package com.springboot.bankapplication.exception;

public class IncorrectRequestException extends RuntimeException {
    public IncorrectRequestException(String message) {
        super(message);
    }
}
