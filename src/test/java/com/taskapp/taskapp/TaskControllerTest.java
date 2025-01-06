package com.taskapp.taskapp;

import com.taskapp.taskapp.Controller.TaskController;
import com.taskapp.taskapp.Models.Task;
import com.taskapp.taskapp.Services.TaskService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.when;

@WebMvcTest(TaskController.class)
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskService taskService;

    @Test
    public void testCreateTaskShouldReturn_IsCreated() throws Exception {
        Task task = new Task();
        task.setId(UUID.randomUUID());
        task.setTitle("New Task");
        task.setDescription("This is a new task");
        task.setCompleted(false);

        when(taskService.createTask(any(Task.class))).thenReturn(task);

        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "title": "New Task",
                          "description": "This is a new task",
                          "completed": false
                        }
                        """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("New Task"))
                .andExpect(jsonPath("$.description").value("This is a new task"))
                .andExpect(jsonPath("$.completed").value(false));
    }

    @Test
    public void testUpdateTaskShouldReturn_Ok() throws Exception {
        UUID taskId = UUID.randomUUID();
        Task updatedTask = new Task();
        updatedTask.setId(taskId);
        updatedTask.setTitle("Updated Task");
        updatedTask.setDescription("This is an updated task");
        updatedTask.setCompleted(true);

        when(taskService.updateTask(eq(taskId), any(Task.class))).thenReturn(updatedTask);

        mockMvc.perform(put("/api/tasks/{id}", taskId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "title": "Updated Task",
                          "description": "This is an updated task",
                          "completed": true
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Task"))
                .andExpect(jsonPath("$.description").value("This is an updated task"))
                .andExpect(jsonPath("$.completed").value(true));
    }

    @Test
    public void testSearchTasksShouldReturn_Ok() throws Exception {
        Task task = new Task();
        task.setId(UUID.randomUUID());
        task.setTitle("Sample Task");
        task.setDescription("This is a sample task");
        task.setCompleted(false);

        Page<Task> page = new PageImpl<>(Collections.singletonList(task));

        when(taskService.searchTasks(anyString(), anyInt(), anyInt(), anyString(), anyString()))
                .thenReturn(page);

        mockMvc.perform(get("/api/tasks")
                .param("search", "sample")
                .param("page", "0")
                .param("size", "10")
                .param("sortBy", "title")
                .param("sortDir", "ASC"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("Sample Task"))
                .andExpect(jsonPath("$.content[0].description").value("This is a sample task"))
                .andExpect(jsonPath("$.content[0].completed").value(false));
    }

    @Test
    public void testDeleteTaskShouldReturn_NoContent() throws Exception {
        UUID taskId = UUID.randomUUID();

        Mockito.doNothing().when(taskService).deleteTask(taskId);

        mockMvc.perform(delete("/api/tasks/{id}", taskId))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testPartialUpdateTaskShouldReturn_Ok() throws Exception {
        UUID taskId = UUID.randomUUID();
        Task updatedTask = new Task();
        updatedTask.setId(taskId);
        updatedTask.setTitle("Partially Updated Task");
        updatedTask.setCompleted(true);

        when(taskService.partialUpdate(eq(taskId), any(Map.class))).thenReturn(updatedTask);

        mockMvc.perform(patch("/api/tasks/{id}", taskId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "title": "Partially Updated Task",
                          "completed": true
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Partially Updated Task"))
                .andExpect(jsonPath("$.completed").value(true));
    }
}
