package org.example.todolist;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@ActiveProfiles("test")
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
    void should_return_unprocessable_entity_when_create_without_given_text() throws Exception {
        String requestBody = """
                {
                   "done": false
                }
                """;
        mockMvc.perform(post("/todos").contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isUnprocessableEntity());

    }

    @Test
    void should_return_unprocessable_entity_when_create_given_null_text() throws Exception {
        String requestBody = """
                {
                   "text": "",
                   "done": false
                }
                """;
        mockMvc.perform(post("/todos").contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isUnprocessableEntity());

    }

    @Test
    void should_ignore_client_sent_id_when_create() throws Exception {
        String requestBody = """
        {
           "id": 1234,
           "text": "this is a text.",
           "done": false
        }
        """;
        MvcResult result = mockMvc.perform(post("/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.text").value("this is a text."))
                .andExpect(jsonPath("$.done").value(false))
                .andReturn();

        // 解析响应体
        String responseBody = result.getResponse().getContentAsString();
        // 用 Jackson 解析 JSON
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(responseBody);
        long returnedId = jsonNode.get("id").asLong();

        // 断言 id 不是 1234（即不是客户端发来的 id）
        assertNotEquals(1234L, returnedId);
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

    @Test
    void should_return_correct_todo_when_a_valid_id_given()throws Exception{
        CreateTodoDto todo1 = new CreateTodoDto();
        todo1.setText("this is a text 1.");
        CreateTodoDto todo2 = new CreateTodoDto();
        todo2.setText("this is a text 2.");

        Long id2=todoService.createTodo(todo2).getId();

        mockMvc.perform(get("/todos/{id}",id2).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.text").value("this is a text 2."))
                .andExpect(jsonPath("$.done").value(false));
    }

    @Test
    void should_update_todo_when_given_a_valid_id() throws Exception{
        CreateTodoDto todo = new CreateTodoDto();
        todo.setText("this is a text before update.");
        Long id=todoService.createTodo(todo).getId();

        String requestBody = """
                {
                   "text": "this is a text after update.",
                   "done": true
                }
                """;
        mockMvc.perform(put("/todos/{id}",id).contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.text").value("this is a text after update."))
                .andExpect(jsonPath("$.done").value(true));
    }
    @Test
    void should_ignore_surplus_id_when_update() throws Exception{
        CreateTodoDto todo1 = new CreateTodoDto();
        todo1.setText("this is todo1");
        CreateTodoDto todo2 = new CreateTodoDto();
        todo2.setText("this is todo2");
        Long id1=todoService.createTodo(todo1).getId();
        Long id2=todoService.createTodo(todo1).getId();

        String requestBody = """
                {
                  "id": %d,
                  "text": "update todo1",
                  "done": true
                }
                """.formatted(id2);
        mockMvc.perform(put("/todos/{id}",id1).contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id1))
                .andExpect(jsonPath("$.text").value("update todo1"))
                .andExpect(jsonPath("$.done").value(true));
    }


    @Test
    void should_return_unprocessable_entity_when_update_given_an_empty_object() throws Exception{
        CreateTodoDto todo = new CreateTodoDto();
        todo.setText("this is a text before update.");
        Long id=todoService.createTodo(todo).getId();

        String requestBody = """
                {
               
                }
                """;
        mockMvc.perform(put("/todos/{id}",id).contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isUnprocessableEntity());
    }


    @Test
    void should_get_not_found_when_given_a_invalid_id() throws Exception{
        String requestBody = """
                {
                   "text": "this is a text after update.",
                   "done": true
                }
                """;
        mockMvc.perform(get("/todos/{id}",-1).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        mockMvc.perform(put("/todos/{id}",-1).contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isNotFound());
        mockMvc.perform(delete("/todos/{id}",-1).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void should_return_no_content_when_delete_todo_given_valid_id()throws Exception{
        CreateTodoDto todo = new CreateTodoDto();
        todo.setText("this is a text before update.");
        Long id=todoService.createTodo(todo).getId();
        mockMvc.perform(delete("/todos/{id}",id).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

}
