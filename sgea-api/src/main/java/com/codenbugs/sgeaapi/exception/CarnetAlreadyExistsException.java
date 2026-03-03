package com.codenbugs.sgeaapi.exception;

public class CarnetAlreadyExistsException extends RuntimeException {
    public CarnetAlreadyExistsException(String message) {
        super(message);
    }
}