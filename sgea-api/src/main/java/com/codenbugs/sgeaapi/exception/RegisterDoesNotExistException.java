package com.codenbugs.sgeaapi.exception;

public class RegisterDoesNotExistException extends RuntimeException {
    public RegisterDoesNotExistException(String message) {
        super(message);
    }
}
