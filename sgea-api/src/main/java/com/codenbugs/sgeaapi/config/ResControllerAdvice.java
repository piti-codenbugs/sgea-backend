package com.codenbugs.sgeaapi.config;

import com.codenbugs.sgeaapi.exception.InvalidArgumentException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class ResControllerAdvice {

    @ExceptionHandler(InvalidArgumentException.class)
    public ResponseEntity<Map<String, String>> handleInvalidArgument(InvalidArgumentException ex) {
        Map<String, String> error = Map.of(
                "error", ex.getMessage(),
                "status", String.valueOf(HttpStatus.BAD_REQUEST.value())
        );
        return ResponseEntity.badRequest().body(error);
    }
}
