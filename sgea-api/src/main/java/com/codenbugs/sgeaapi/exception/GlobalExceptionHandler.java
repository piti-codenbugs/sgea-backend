package com.codenbugs.sgeaapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * Sirve para manejar excepción cuando el usuario ya existe.
     *
     * @param ex es la exepción capturada.
     * @return el estatus del mensaje, un estado HTTP 409.
     */
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<?> handleUserAlreadyExists(UserAlreadyExistsException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of(
                        "timestamp", LocalDateTime.now(),
                        "status", 409,
                        "error", "Conflict",
                        "message", ex.getMessage()
                ));
    }

    /**
     * Sirve cuando un usuario no tiene los permisos necesarios o no está autenticado.
     *
     * @param ex es la excepción capturada.
     * @return un estatus del mensaje, un estado HTTP 401.
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentials(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(Map.of(
                        "timestamp", LocalDateTime.now(),
                        "status", 401,
                        "error", "Unauthorized",
                        "message", "Credenciales incorrectas"
                ));
    }

}