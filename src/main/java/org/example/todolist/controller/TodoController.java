package org.example.todolist.controller;

import org.example.todolist.entity.Todo;
import org.example.todolist.service.TodoService;
import org.example.todolist.service.dto.CreateTodoDto;
import org.example.todolist.service.dto.UpdateTodoDto;
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

    @GetMapping("/todos/{id}")
    public ResponseEntity<Todo> findById(@PathVariable Long id){
        return ResponseEntity.ok(todoService.findTodoById(id));
    }

    @PutMapping("/todos/{id}")
    public ResponseEntity<Todo> updateTodo(@PathVariable Long id, @RequestBody UpdateTodoDto todo){
        return ResponseEntity.ok(todoService.updateTodo(id,todo));
    }

}
