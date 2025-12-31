package com.studying.first_spring_app.exception;

public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException(String message) {
        super(message);
    }

    public TaskNotFoundException() {
        super("Task not found");
    }
}
