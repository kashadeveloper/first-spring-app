package com.studying.first_spring_app.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ValidationErrorResponse {
    private String message;
    private List<ValidationError> errors;

    public record ValidationError(String fieldName, String message) {
    }
}
