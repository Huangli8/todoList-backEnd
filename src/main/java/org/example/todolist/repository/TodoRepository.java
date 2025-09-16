package org.example.todolist.repository;

import org.example.todolist.entity.Todo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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
}
