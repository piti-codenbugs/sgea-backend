package com.codenbugs.sgeaapi.exception;

//import org.hibernate.annotations.NotFound;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
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
        return  buildError(HttpStatus.CONFLICT, ex.getMessage());
//        return ResponseEntity
//                .status(HttpStatus.CONFLICT)
//                .body(Map.of(
//                        "timestamp", LocalDateTime.now(),
//                        "status", 409,
//                        "error", "Conflict",
//                        "message", ex.getMessage()
//                ));
    }

    /**
     * Sirve cuando un usuario no tiene los permisos necesarios o no está autenticado.
     * //@param ex es la excepción capturada.
     * @return un estatus del mensaje, un estado HTTP 401.
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentials( /*Exception ex */) {
        return  buildError(HttpStatus.UNAUTHORIZED, "Credenciales incorrectas");
//        return ResponseEntity
//                .status(HttpStatus.UNAUTHORIZED)
//                .body(Map.of(
//                        "timestamp", LocalDateTime.now(),
//                        "status", 401,
//                        "error", "Unauthorized",
//                        "message", "Credenciales incorrectas"
//                ));
    }

    /**
     * Sirve para capturar la excepción cuando un campo obligatorio está vacío.
     *
     * @param ex es la excepción capturada.
     *
     * @return un status del mensaje, un estado HTTP 401.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errors);
    }

    /**
     * Sirve para capturar la excepción cuando cuando se evia un parametro en un formato ivalido.
     *
     * @param ex es la excepción capturada.
     *
     * @return un status del mensaje, un estado HTTP 401.
     */
//    @ExceptionHandler(InvalidArgumentException.class)
//    public ResponseEntity<Map<String, String>> handleInvalidArgument(InvalidArgumentException ex) {
//        Map<String, String> error = Map.of(
//                "error", ex.getMessage(),
//                "status", String.valueOf(HttpStatus.BAD_REQUEST.value())
//        );
//        return ResponseEntity.badRequest().body(error);
//    }
    @ExceptionHandler(InvalidArgumentException.class)
    public ResponseEntity<?> handleInvalidArgument(InvalidArgumentException ex) {
        return buildError(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    /**
     * Sirve para capturar la excepción cuando no se encuentra algo en db.
     *
     * @param ex es la excepción capturada.
     *
     * @return un status del mensaje, un estado HTTP 401.
     */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleNotFound(NotFoundException ex) {
        return  buildError(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    /**
     * Maneja la excepción cuando un usuario intenta iniciar seesión pero su cuenta está inactiva.
     * @param ex es la excepción capturada.
     * @return es un estatus del mensaje con estado HTTP 403.
     */
    @ExceptionHandler(UserDisabledException.class)
    public ResponseEntity<?> handleUserDisabled(UserDisabledException ex) {
        return  buildError(HttpStatus.FORBIDDEN, ex.getMessage());
    }

    private ResponseEntity<?> buildError(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(Map.of(
                "timestamp", LocalDateTime.now(),
                "status", status.value(),
                "error", status.getReasonPhrase(),
                "message", message
        ));
    }
}