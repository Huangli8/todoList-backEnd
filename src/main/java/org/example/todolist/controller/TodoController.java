package org.example.todolist.controller;

import org.example.todolist.entity.Todo;
import org.example.todolist.service.TodoService;
import org.example.todolist.service.dto.CreateTodoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "*")
@RestController
public class TodoController {
    @Autowired
    TodoService todoService;

    @PostMapping("/todos")
    public ResponseEntity<Todo> createTodo(@RequestBody CreateTodoDto todo) {
        return ResponseEntity.status(HttpStatus.CREATED).body(todoService.createTodo(todo));
    }

    @GetMapping("/todos")
    public List<Todo> findAll(){
        return todoService.findAllTodos();
    }

}
