package com.taskapp.taskapp.Util.Exceptions;


import lombok.Getter;

@Getter
public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException(String message) {
        super(message);
    }
}
