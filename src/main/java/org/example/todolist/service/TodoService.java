package org.example.todolist.service;

import org.example.todolist.entity.Todo;
import org.example.todolist.exception.EmptyTextException;
import org.example.todolist.repository.TodoRepository;
import org.example.todolist.service.dto.CreateTodoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class TodoService {
    @Autowired
    TodoRepository todoRepository;

    public Todo createTodo(CreateTodoDto todo){
        if(todo.getText()==null|| Objects.equals(todo.getText(), "")){
            throw new EmptyTextException();
        }
        Todo todo1 = new Todo();
        todo1.setText(todo.getText());
        return todoRepository.save(todo1);
    }

    public List<Todo> findAllTodos(){
        return todoRepository.findAll();
    }
}
