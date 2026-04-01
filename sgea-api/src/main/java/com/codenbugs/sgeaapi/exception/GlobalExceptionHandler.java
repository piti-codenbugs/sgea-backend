package com.codenbugs.sgeaapi.exception;

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
     * @param ex es la excepción capturada de tipo UserAlreadyExistsException.
     * @return el estatus del mensaje, un estado HTTP 409.
     */
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<?> handleUserAlreadyExists(UserAlreadyExistsException ex) {
        return buildError(HttpStatus.CONFLICT, ex.getMessage());
    }

    /**
     * Sirve cuando un usuario no tiene los permisos necesarios o no está autenticado.
     *
     * @return un estatus del mensaje, un estado HTTP 401.
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentials() {
        return buildError(HttpStatus.UNAUTHORIZED, "Credenciales incorrectas");
    }

    /**
     * Sirve para capturar la excepción cuando un campo obligatorio está vacío.
     *
     * @param ex es la excepción capturada de tipo MethodArgumentNotValidException.
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
     * Sirve para capturar la excepción cuando cuando se envía un parámetro en un formato inválido.
     *
     * @param ex es la excepción capturada de tipo InvalidArgumentException.
     * @return un status del mensaje, un estado HTTP 401.
     */
    @ExceptionHandler(InvalidArgumentException.class)
    public ResponseEntity<?> handleInvalidArgument(InvalidArgumentException ex) {
        return buildError(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    /**
     * Sirve para capturar la excepción cuando no se encuentra algo en db.
     *
     * @param ex es la excepción capturada de tipo NotFoundException.
     * @return un status del mensaje, un estado HTTP 401.
     */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleNotFound(NotFoundException ex) {
        return buildError(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    /**
     * Maneja la excepción cuando un usuario intenta iniciar sesión, pero su cuenta está inactiva.
     *
     * @param ex es la excepción capturada de tipo UserDisabledException.
     * @return un estatus del mensaje con estado HTTP 403.
     */
    @ExceptionHandler(UserDisabledException.class)
    public ResponseEntity<?> handleUserDisabled(UserDisabledException ex) {
        return buildError(HttpStatus.FORBIDDEN, ex.getMessage());
    }

    /**
     * Maneja la excepción cuando el docente ya está asignado a un curso en el periodo en curso.
     *
     * @param ex es la excepción capturada de tipo AssignmentExistException.
     * @return un estatus del mensaje con estado HTTP 409.
     */
    @ExceptionHandler(AssignmentExistException.class)
    public ResponseEntity<?> handleAssignmentExist(AssignmentExistException ex) {
        return buildError(HttpStatus.CONFLICT, ex.getMessage());
    }

    /**
     * Maneja la excepción cuando el docente no existe.
     *
     * @param ex es la excepción capturada de tipo ProfessorDoesNotExistException.
     * @return un estatus del mensaje con estado HTTP 401.
     */
    @ExceptionHandler(ProfessorDoesNotExistException.class)
    public ResponseEntity<?> handleProfessorNotFound(ProfessorDoesNotExistException ex) {
        return buildError(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    /**
     * Maneja la excepción cuando el curso no existe.
     *
     * @param ex es la excepción capturada de tipo CourseDoesNotExistException.
     * @return un estatus del mensaje con estado HTTP 401.
     */
    @ExceptionHandler(CourseDoesNotExistException.class)
    public ResponseEntity<?> handleCourseNotFound(CourseDoesNotExistException ex) {
        return buildError(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    /**
     * Maneja la excepción cuando un registro no existe.
     *
     * @param ex es la excepción capturada de tipo RegisterDoesNotExistException.
     * @return un estatus del mensaje con estado HTTP 409.
     */
    @ExceptionHandler(RegisterDoesNotExistException.class)
    public ResponseEntity<?> handleRegisterDoesNotExist(RegisterDoesNotExistException ex) {
        return buildError(HttpStatus.CONFLICT, ex.getMessage());
    }

    /**
     * Método utilitario para estandarizar la respuesta.
     *
     * @param status  es el estado de la respuesta de tipo HttpStatus.
     * @param message es el mensaje de la respuesta de tipo String.
     * @return un formato estandarizado JSON.
     */
    private ResponseEntity<?> buildError(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(Map.of(
                "timestamp", LocalDateTime.now(),
                "status", status.value(),
                "error", status.getReasonPhrase(),
                "message", message
        ));
    }
}