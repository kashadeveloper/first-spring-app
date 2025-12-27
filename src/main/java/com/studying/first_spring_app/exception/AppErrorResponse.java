package com.studying.first_spring_app.exception;

import lombok.Data;

@Data
public class AppErrorResponse {
    private String message;
    private int status;

    public AppErrorResponse(String message, int status) {
        this.message = message;
        this.status = status;
    }
}
