package org.example.todolist.repository;

import org.example.todolist.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoJPARepository extends JpaRepository<Todo, Long> {
}
