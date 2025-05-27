package org.products.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class ControllerAdvisor  {

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Object>handleProductNotFound(ProductNotFoundException e){
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status",HttpStatus.NOT_FOUND.value());
        body.put("error", e.getClass().getSimpleName());
        body.put("message", e.getMessage());
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ProductTypeNotFoundException.class)
    public ResponseEntity<Object>handleProductTypeNotFound(ProductTypeNotFoundException e){
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status",HttpStatus.NOT_FOUND.value());
        body.put("error", e.getClass().getSimpleName());
        body.put("message", e.getMessage());
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ProductTypeRelatedException.class)
    public ResponseEntity<Object>handleProductTypeRelated(ProductTypeRelatedException e){
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status",HttpStatus.CONFLICT.value());
        body.put("error", e.getClass().getSimpleName());
        body.put("message", e.getMessage());
        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object>handleIllegalArgument(IllegalArgumentException e){
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status",HttpStatus.CONFLICT.value());
        body.put("error", e.getClass().getSimpleName());
        body.put("message", e.getMessage());
        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object>handleResourceNotFound(ResourceNotFoundException e){
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status",HttpStatus.NOT_FOUND.value());
        body.put("error", e.getClass().getSimpleName());
        body.put("message", e.getMessage());
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Object>handleUnauthorized(UnauthorizedException e){
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status",HttpStatus.UNAUTHORIZED.value());
        body.put("error", e.getClass().getSimpleName());
        body.put("message", e.getMessage());
        return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ForbiddenAccessException.class)
    public ResponseEntity<Object>handleForbiddenAccess(ForbiddenAccessException e){
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status",HttpStatus.FORBIDDEN.value());
        body.put("error", e.getClass().getSimpleName());
        body.put("message", e.getMessage());
        return new ResponseEntity<>(body, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException e){
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status",HttpStatus.BAD_REQUEST.value());
        body.put("error", "Validation failed");

        List<String> errors = e.getBindingResult().getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.toList());

        body.put("message",errors);

        return new ResponseEntity<>(body,HttpStatus.BAD_REQUEST);
    }
}
