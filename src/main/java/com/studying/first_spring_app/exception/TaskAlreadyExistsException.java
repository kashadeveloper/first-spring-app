package com.studying.first_spring_app.exception;

public class TaskAlreadyExistsException extends RuntimeException {
    public TaskAlreadyExistsException(String message) {
        super(message != null ? message : "Task already exists");
    }

    public TaskAlreadyExistsException() {
        super("Task already exists");
    }
}
