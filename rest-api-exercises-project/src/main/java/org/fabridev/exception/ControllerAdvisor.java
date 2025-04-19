package org.fabridev.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class ControllerAdvisor extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Object>handleProductNotFound(ProductNotFoundException e){
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status",HttpStatus.NOT_FOUND.value());
        body.put("error", e.getClass().getSimpleName());
        body.put("message", e.getMessage());
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidProductException.class)
    public ResponseEntity<Object>handleInvalidProduct(InvalidProductException e){
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status",HttpStatus.BAD_REQUEST.value());
        body.put("error", e.getClass().getSimpleName());
        body.put("message", e.getMessage());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ProductAlreadyExistsException.class)
    public ResponseEntity<Object>handleProductAlreadyExists(ProductAlreadyExistsException e){
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status",HttpStatus.BAD_REQUEST.value());
        body.put("error", e.getClass().getSimpleName());
        body.put("message", e.getMessage());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }


//    @ExceptionHandler(ProductNullFieldFoundException.class)
//    public ResponseEntity<Object>handleProductNullFiled(ProductNullFieldFoundException e){
//        Map<String, Object> body = new LinkedHashMap<>();
//        body.put("timestamp", LocalDateTime.now());
//        body.put("status",HttpStatus.BAD_REQUEST.value());
//        body.put("error",HttpStatus.BAD_REQUEST);
//        body.put("message", e.getMessage());
//        return new ResponseEntity<>(body,HttpStatus.BAD_REQUEST);
//    }

}
