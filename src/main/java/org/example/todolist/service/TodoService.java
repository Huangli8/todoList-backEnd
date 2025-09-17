package org.example.todolist.service;

import org.example.todolist.entity.Todo;
import org.example.todolist.exception.EmptyTextException;
import org.example.todolist.exception.InvalidUpdateException;
import org.example.todolist.exception.TodoNotFoundException;
import org.example.todolist.repository.TodoRepository;
import org.example.todolist.service.dto.CreateTodoDto;
import org.example.todolist.service.dto.UpdateTodoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class TodoService {
    @Autowired
    TodoRepository todoRepository;

    public Todo createTodo(CreateTodoDto todo) {
        if (todo.getText() == null || Objects.equals(todo.getText(), "")) {
            throw new EmptyTextException();
        }
        Todo todo1 = new Todo();
        todo1.setText(todo.getText());
        return todoRepository.save(todo1);
    }

    public List<Todo> findAllTodos() {
        return todoRepository.findAll();
    }

    public Todo findTodoById(Long id) {
        Optional<Todo> todo = todoRepository.findById(id);
        if (todo.isEmpty()) {
            throw new TodoNotFoundException("Todo with id:%d is not found.".formatted(id));
        }
        return todo.get();
    }

    public Todo updateTodo(Long id, UpdateTodoDto updateTodoDto) {
        Optional<Todo> itemToUpdate = todoRepository.findById(id);
        if (itemToUpdate.isEmpty()) {
            throw new TodoNotFoundException("Todo with id:%d is not found.".formatted(id));
        }
        if (updateTodoDto.isDone() == null && (updateTodoDto.getText() == null || updateTodoDto.getText().isBlank()) ) {
            throw new InvalidUpdateException();
        }
        if (updateTodoDto.getText() != null) {
            itemToUpdate.get().setText(updateTodoDto.getText());
        }
        if (updateTodoDto.isDone() != null) {
            itemToUpdate.get().setDone(updateTodoDto.isDone());
        }
        todoRepository.save(itemToUpdate.get());
        return itemToUpdate.get();
    }

    public void deleteTodo(Long id) {
        Optional<Todo> itemToDelete = todoRepository.findById(id);
        if (itemToDelete.isEmpty()) {
            throw new TodoNotFoundException("Todo with id:%d is not found.".formatted(id));
        }
        todoRepository.delete(itemToDelete.get());
    }
}
