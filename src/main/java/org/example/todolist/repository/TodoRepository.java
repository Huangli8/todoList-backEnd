package org.example.todolist.repository;

import org.example.todolist.entity.Todo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TodoRepository {
    @Autowired
    TodoJPARepository todoJPARepository;

    public Todo save(Todo todo){
        return todoJPARepository.save(todo);
    }

    public void deleteAll(){
        todoJPARepository.deleteAll();
    }

    public List<Todo> findAll(){
        return todoJPARepository.findAll();
    }

    public Optional<Todo> findById(Long id){
        return todoJPARepository.findById(id);
    }
}
