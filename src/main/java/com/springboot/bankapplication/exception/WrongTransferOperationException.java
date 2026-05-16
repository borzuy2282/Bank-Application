package com.springboot.bankapplication.exception;

public class WrongTransferOperationException extends RuntimeException {
    public WrongTransferOperationException(String message) {
        super(message);
    }
}
