package com.taskapp.taskapp;


import com.taskapp.taskapp.Models.Task;
import com.taskapp.taskapp.Repository.TaskRepository;
import com.taskapp.taskapp.Services.TaskService;
import com.taskapp.taskapp.Util.Exceptions.TaskNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

public class TaskServiceTest {

    private TaskRepository taskRepository;
    private TaskService taskService;

    @BeforeEach
    public void setup() {
        taskRepository = Mockito.mock(TaskRepository.class);
        taskService = new TaskService(taskRepository);
    }

    @Test
    public void testCreateTask() {
        Task task = new Task();
        task.setTitle("Test Task");
        task.setDescription("This is a test task");
        task.setCompleted(false);

        when(taskRepository.save(task)).thenAnswer(invocation -> {
            Task savedTask = invocation.getArgument(0);
            savedTask.setId(UUID.randomUUID());
            return savedTask;
        });

        Task createdTask = taskService.createTask(task);

        assertThat(createdTask.getId()).isNotNull();
        assertThat(createdTask.getTitle()).isEqualTo("Test Task");
        assertThat(createdTask.getDescription()).isEqualTo("This is a test task");
        assertThat(createdTask.isCompleted()).isFalse();

        verify(taskRepository, times(1)).save(task);
    }

    @Test
    public void testUpdateTask_Success() {
        UUID taskId = UUID.randomUUID();
        Task existingTask = new Task();
        existingTask.setId(taskId);
        existingTask.setTitle("Old Task");
        existingTask.setDescription("Old description");
        existingTask.setCompleted(false);

        Task updatedTask = new Task();
        updatedTask.setTitle("Updated Task");
        updatedTask.setDescription("Updated description");
        updatedTask.setCompleted(true);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Task result = taskService.updateTask(taskId, updatedTask);

        assertThat(result.getTitle()).isEqualTo("Updated Task");
        assertThat(result.getDescription()).isEqualTo("Updated description");
        assertThat(result.isCompleted()).isTrue();

        verify(taskRepository, times(1)).findById(taskId);
        verify(taskRepository, times(1)).save(existingTask);
    }

    @Test
    public void testUpdateTask_NotFound() {
        UUID taskId = UUID.randomUUID();
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        Task updatedTask = new Task();
        updatedTask.setTitle("Updated Task");

        assertThatThrownBy(() -> taskService.updateTask(taskId, updatedTask))
                .isInstanceOf(TaskNotFoundException.class)
                .hasMessage("Task not found with id: " + taskId);

        verify(taskRepository, times(1)).findById(taskId);
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    public void testSearchTasks() {
        Task task = new Task();
        task.setId(UUID.randomUUID());
        task.setTitle("Search Task");
        task.setDescription("Task for search");
        task.setCompleted(false);

        Page<Task> taskPage = new PageImpl<>(Collections.singletonList(task));
        when(taskRepository.searchTasks(eq("search"), any(PageRequest.class))).thenReturn(taskPage);

        Page<Task> result = taskService.searchTasks("search", 0, 10, "title", "ASC");

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getTitle()).isEqualTo("Search Task");

        verify(taskRepository, times(1)).searchTasks(eq("search"), any(PageRequest.class));
    }

    @Test
    public void testDeleteTask_Success() {
        UUID taskId = UUID.randomUUID();
        when(taskRepository.existsById(taskId)).thenReturn(true);

        taskService.deleteTask(taskId);

        verify(taskRepository, times(1)).existsById(taskId);
        verify(taskRepository, times(1)).deleteById(taskId);
    }

    @Test
    public void testDeleteTask_NotFound() {
        UUID taskId = UUID.randomUUID();
        when(taskRepository.existsById(taskId)).thenReturn(false);

        assertThatThrownBy(() -> taskService.deleteTask(taskId))
                .isInstanceOf(TaskNotFoundException.class)
                .hasMessage("Task not found with id: " + taskId);

        verify(taskRepository, times(1)).existsById(taskId);
        verify(taskRepository, never()).deleteById(any(UUID.class));
    }

    @Test
    public void testPartialUpdate_Success() {
        UUID taskId = UUID.randomUUID();
        Task existingTask = new Task();
        existingTask.setId(taskId);
        existingTask.setTitle("Old Task");
        existingTask.setCompleted(false);

        Map<String, Object> updates = new HashMap<>();
        updates.put("title", "New Task");
        updates.put("completed", true);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(existingTask)).thenReturn(existingTask);

        Task result = taskService.partialUpdate(taskId, updates);

        assertThat(result.getTitle()).isEqualTo("New Task");
        assertThat(result.isCompleted()).isTrue();

        verify(taskRepository, times(1)).findById(taskId);
        verify(taskRepository, times(1)).save(existingTask);
    }

    @Test
    public void testPartialUpdate_TaskNotFound() {
        UUID taskId = UUID.randomUUID();
        Map<String, Object> updates = Map.of("title", "New Task");

        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.partialUpdate(taskId, updates))
                .isInstanceOf(TaskNotFoundException.class)
                .hasMessage("Task not found with id: " + taskId);

        verify(taskRepository, times(1)).findById(taskId);
        verify(taskRepository, never()).save(any(Task.class));
    }
}