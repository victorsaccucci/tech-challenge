package com.fiap.zecomanda.controllers.handler;

import com.fiap.zecomanda.commons.exceptions.AccessDeniedException;
import com.fiap.zecomanda.commons.exceptions.ResourceAlreadyExistsException;
import com.fiap.zecomanda.commons.exceptions.UnauthorizedAccessException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ControllerExceptionHandler {

    private ResponseEntity<Map<String, Object>> body(
            HttpStatus status, String error, String message, String path, Map<String, ?> fields
    ) {
        Map<String, Object> b = new HashMap<>();
        b.put("timestamp", LocalDateTime.now());
        b.put("status", status.value());
        b.put("error", error);
        if (message != null) b.put("message", message);
        b.put("path", path);
        if (fields != null && !fields.isEmpty()) b.put("fields", fields);
        return ResponseEntity.status(status).body(b);
    }

    /* ===== 400 — Bad Request ===== */

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleDto(
            MethodArgumentNotValidException ex, HttpServletRequest request
    ) {
        Map<String, String> fields = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(err -> fields.put(err.getField(), err.getDefaultMessage()));
        return body(HttpStatus.BAD_REQUEST, "validation error",
                "One or more fields are invalid.", request.getRequestURI(), fields);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handleConstraint(
            ConstraintViolationException ex, HttpServletRequest request
    ) {
        Map<String, String> fields = new HashMap<>();
        for (ConstraintViolation<?> v : ex.getConstraintViolations()) {
            fields.put(v.getPropertyPath().toString(), v.getMessage());
        }
        return body(HttpStatus.BAD_REQUEST, "constraint violation",
                "One or more constraints were violated.", request.getRequestURI(), fields);
    }

    // JSON malformado / tipo inválido no corpo
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleNotReadable(
            HttpMessageNotReadableException ex, HttpServletRequest request
    ) {

        return body(HttpStatus.BAD_REQUEST, "malformed request",
                "Request body is malformed or has invalid types.", request.getRequestURI(), null);
    }

    // Argumento/regra inválida (ex.: senha fraca, name em branco pós-trim, etc.)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(
            IllegalArgumentException ex, HttpServletRequest request
    ) {
        return body(
                HttpStatus.BAD_REQUEST,
                "invalid argument",
                ex.getMessage(),
                request.getRequestURI(),
                null
        );
    }

    // Tipo de argumento errado em path/query (ex: id não-numérico)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex, HttpServletRequest request
    ) {
        Map<String, String> fields = new HashMap<>();
        fields.put(String.valueOf(ex.getName()), "invalid type");
        return body(HttpStatus.BAD_REQUEST, "type mismatch",
                "Request contains parameters of invalid type.", request.getRequestURI(), fields);
    }

    // Parâmetro obrigatório ausente
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Map<String, Object>> handleMissingParam(
            MissingServletRequestParameterException ex, HttpServletRequest request
    ) {
        Map<String, String> fields = new HashMap<>();
        fields.put(ex.getParameterName(), "required parameter is missing");
        return body(HttpStatus.BAD_REQUEST, "missing parameter",
                "Required parameter is missing.", request.getRequestURI(), fields);
    }

    /* ===== 409 — Conflict ===== */

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleIntegrity(
            DataIntegrityViolationException ex, HttpServletRequest request
    ) {
        System.err.println("Integrity violation: " + ex.getMessage());
        return body(HttpStatus.CONFLICT, "Conflict",
                "Registration failed. Please check your data.", request.getRequestURI(), null);
    }

//    @ExceptionHandler(HttpMessageNotReadableException.class)
//    public ResponseEntity<Map<String, Object>> handlerValidation(
//        HttpMessageNotReadableException ex,
//        HttpServletRequest request
//    ) {
//
//        Map<String, Object> resposta = new HashMap<>();
//        resposta.put("timestamp", LocalDateTime.now());
//        resposta.put("status", HttpStatus.BAD_REQUEST.value());
//        resposta.put("error", ex.getMessage());
//        resposta.put("path", request.getRequestURI());
//
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resposta);
//    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleResourceAlreadyExists(
            ResourceAlreadyExistsException ex, HttpServletRequest request
    ) {
        System.err.println("Resource conflict: " + ex.getMessage());
        return body(HttpStatus.CONFLICT, "Registration failed",
                "Could not register user. Please check your data.", request.getRequestURI(), null);
    }

    /* ===== 404 — NotFound ===== */
    @ExceptionHandler(com.fiap.zecomanda.commons.exceptions.ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(
            com.fiap.zecomanda.commons.exceptions.ResourceNotFoundException ex,
            HttpServletRequest request
    ) {
        return body(HttpStatus.NOT_FOUND, "Not Found", ex.getMessage(), request.getRequestURI(), null);
    }

    /* ===== 401 — Unauthorized ===== */

    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<Map<String, Object>> handleUnauthorizedAccess(
            UnauthorizedAccessException ex, HttpServletRequest request
    ) {
        return body(HttpStatus.UNAUTHORIZED, "Unauthorized",
                "Invalid login or password.", request.getRequestURI(), null);
    }

    /* ===== 403 — Forbidden ===== */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleAccessDenied(
            AccessDeniedException ex, HttpServletRequest request
    ) {
        return body(HttpStatus.FORBIDDEN, "Forbidden",
                ex.getMessage(), request.getRequestURI(), null);
    }

    /* ===== 500 — Fallback ===== */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(
            Exception ex, HttpServletRequest request
    ) {
        System.err.println("Unhandled exception: " + ex.getMessage());
        return body(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error",
                "An unexpected error occurred.", request.getRequestURI(), null);
    }

}
