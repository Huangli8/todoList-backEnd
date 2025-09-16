package org.example.todolist;

import org.example.todolist.entity.Todo;
import org.example.todolist.repository.TodoRepository;
import org.example.todolist.service.TodoService;
import org.example.todolist.service.dto.CreateTodoDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TodoControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private TodoService todoService;

    @BeforeEach
    void setUp(){
        todoRepository.deleteAll();
    }
    @Test
    void should_create_todo_when_given_a_valid_body() throws Exception {
        String requestBody = """
                {
                   "text": "this is a text."
                }
                """;
        mockMvc.perform(post("/todos").contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.text").value("this is a text."))
                .andExpect(jsonPath("$.done").value(false));

    }

    @Test
    void should_return_all_todos_when_no_filter_given()throws Exception{
        CreateTodoDto todo1 = new CreateTodoDto();
        todo1.setText("this is a text 1.");
        CreateTodoDto todo2 = new CreateTodoDto();
        todo2.setText("this is a text 2.");
        todoService.createTodo(todo1);
        todoService.createTodo(todo2);
        mockMvc.perform(get("/todos").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").isNumber())
                .andExpect(jsonPath("$[0].text").value("this is a text 1."))
                .andExpect(jsonPath("$[0].done").value(false))
                .andExpect(jsonPath("$[1].id").isNumber())
                .andExpect(jsonPath("$[1].text").value("this is a text 2."))
                .andExpect(jsonPath("$[1].done").value(false));
    }

}
