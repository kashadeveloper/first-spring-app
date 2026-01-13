package com.studying.first_spring_app.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.core.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = NoResourceFoundException.class)
    public ResponseEntity<AppErrorResponse> handleNoFound(NoResourceFoundException e) {
        return new ResponseEntity<>(new AppErrorResponse("Resource not found", HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<ValidationErrorResponse.ValidationError> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> new ValidationErrorResponse.ValidationError(
                        error.getField(),
                        error.getDefaultMessage()
                ))
                .toList();

        return new ValidationErrorResponse("Validation error", errors);
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public AppErrorResponse handleBadCredentials(BadCredentialsException ex) {
        return new AppErrorResponse("Bad credentials", HttpStatus.UNAUTHORIZED.value());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<AppErrorResponse> handleUsernameNotFound(UsernameNotFoundException e) {
        return new ResponseEntity<>(new AppErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = ResponseStatusException.class)
    public ResponseEntity<AppErrorResponse> handleResponseStatusException(ResponseStatusException e) {
        return new ResponseEntity<>(new AppErrorResponse(e.getReason(), e.getStatusCode().value()), e.getStatusCode());
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, MissingServletRequestParameterException.class, MethodArgumentTypeMismatchException.class,})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public AppErrorResponse handleBadRequest(Exception ex) {
        if (ex instanceof MethodArgumentTypeMismatchException) {
            return new AppErrorResponse(((MethodArgumentTypeMismatchException) ex).getPropertyName() + " is invalid", HttpStatus.BAD_REQUEST.value());
        }

        return new AppErrorResponse("Bad Request", HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public AppErrorResponse handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex) {
        return new AppErrorResponse("Method not allowed", HttpStatus.METHOD_NOT_ALLOWED.value());
    }

    @ExceptionHandler(TaskAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public AppErrorResponse handleTaskAlreadyExists(TaskAlreadyExistsException e) {
        return new AppErrorResponse(e.getMessage(), HttpStatus.CONFLICT.value());
    }


    @ExceptionHandler(TaskNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public AppErrorResponse handleTaskNotFound(TaskNotFoundException e) {
        return new AppErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND.value());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(UserAlreadyExistsException.class)
    public AppErrorResponse handleUserAlreadyExists(UserAlreadyExistsException e) {
        return new AppErrorResponse(e.getMessage(), HttpStatus.CONFLICT.value());
    }
    @ExceptionHandler(value = MultipartException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public AppErrorResponse handleMultipartException(MultipartException e) {
        return new AppErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler(value = PropertyReferenceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public AppErrorResponse handlePropertyReferenceException(PropertyReferenceException e) {
        var propertyName = e.getPropertyName();
        var entityName = e.getType().getType().getSimpleName();
        String message = String.format(
                "Invalid field name '%s' for property reference '%s'",
                propertyName,
                entityName
        );

        return new AppErrorResponse(message, HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<AppErrorResponse> handleException(Exception e) {
        log.error("Exception: {}", e.getMessage(), e);
        return new ResponseEntity<>(new AppErrorResponse("Unhandled error", HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
