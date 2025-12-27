package com.studying.first_spring_app.exception;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

public class NotFoundException extends ResponseStatusException {
    public NotFoundException(String message, int status) {
        super(HttpStatusCode.valueOf(status), message);
    }

    public NotFoundException(String message) {
        super(HttpStatusCode.valueOf(404), message);
    }
}
