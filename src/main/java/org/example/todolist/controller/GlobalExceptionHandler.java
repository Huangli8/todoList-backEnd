package org.example.todolist.controller;

import org.example.todolist.exception.EmptyTextException;
import org.example.todolist.exception.InvalidUpdateException;
import org.example.todolist.exception.TodoNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice

public class GlobalExceptionHandler {

    @ExceptionHandler(EmptyTextException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public String handleEmployeeAlreadyDeletedException(EmptyTextException e) {
        return e.getMessage();
    }

    @ExceptionHandler(TodoNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleTodoNotFoundException(TodoNotFoundException e) {
        return e.getMessage();
    }

    @ExceptionHandler(InvalidUpdateException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public String handleInvalidUpdateException(InvalidUpdateException e) {
        return e.getMessage();
    }

}
