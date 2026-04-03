package com.codenbugs.sgeaapi.exception;

public class StudentDoesNotExistException extends RuntimeException {
    public StudentDoesNotExistException(String message) {
        super(message);
    }
}
