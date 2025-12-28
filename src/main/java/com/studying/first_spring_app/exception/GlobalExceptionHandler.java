package com.studying.first_spring_app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = NoResourceFoundException.class)
    public ResponseEntity<AppErrorResponse> handleNoFound(NoResourceFoundException e) {
        return new ResponseEntity<>(new AppErrorResponse("Resource not found", HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        var errors = e.getFieldErrors().stream().map(x -> new ValidationErrorResponse.ValidationError(x.getField(), x.getDefaultMessage())).toList();
        return new ResponseEntity<>(new ValidationErrorResponse("Validation failed", errors), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = ResponseStatusException.class)
    public ResponseEntity<AppErrorResponse> handleResponseStatusException(ResponseStatusException e) {
        return new ResponseEntity<>(new AppErrorResponse(e.getReason(), e.getStatusCode().value()), e.getStatusCode());
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<AppErrorResponse> handleException(Exception e) {
        System.err.println(e.getMessage() + " | " + e.getClass().getName());
        e.printStackTrace();
        return new ResponseEntity<>(new AppErrorResponse("Unhandled error", HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
