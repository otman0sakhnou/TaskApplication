package com.taskapp.taskapp.Services;

import com.taskapp.taskapp.Models.Task;
import com.taskapp.taskapp.Repository.TaskRepository;
import com.taskapp.taskapp.Util.Exceptions.TaskNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Map;
import java.util.UUID;

@Service
@Transactional
public class TaskService {
  private final TaskRepository taskRepository;
  private static final Logger logger = LoggerFactory.getLogger(TaskService.class);

  public TaskService(TaskRepository taskRepository) {
    this.taskRepository = taskRepository;
  }

  public Task createTask(Task task) {
    logger.debug("Creating task with title: {}", task.getTitle());
    Task savedTask = taskRepository.save(task);
    logger.info("Task created successfully with ID: {}", savedTask.getId());
    return savedTask;
  }

  public Task updateTask(UUID id, Task task) {
    logger.debug("Updating task with ID: {}", id);
    Task existingTask = taskRepository.findById(id)
        .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + id));

    logger.debug("Found task: {}", existingTask);
    existingTask.setTitle(task.getTitle());
    existingTask.setDescription(task.getDescription());
    existingTask.setCompleted(task.isCompleted());
    Task updatedTask = taskRepository.save(existingTask);
    logger.info("Task updated successfully with ID: {}", updatedTask.getId());
    return updatedTask;
  }

  public Page<Task> searchTasks(String searchTerm, int page, int size, String sortBy, String sortDir) {
    logger.debug("Searching tasks with term: {}", searchTerm);
        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Task> taskPage = (searchTerm == null || searchTerm.isEmpty())
            ? taskRepository.findAll(pageable)
            : taskRepository.searchTasks(searchTerm, pageable);

        logger.info("Found {} tasks", taskPage.getTotalElements());
        return taskPage;
  }

  public void deleteTask(UUID id) {
    logger.debug("Deleting task with ID: {}", id);
    if (taskRepository.existsById(id)) {
        taskRepository.deleteById(id);
        logger.info("Task deleted successfully with ID: {}", id);
    } else {
        logger.error("Task not found with ID: {}", id);
        throw new TaskNotFoundException("Task not found with id: " + id);
    }
  }

  public Task partialUpdate(UUID id, Map<String, Object> updates) {
    logger.debug("Partially updating task with ID: {}", id);
    Task task = taskRepository.findById(id)
        .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + id));

    updates.forEach((key, value) -> {
        logger.debug("Updating field: {} with value: {}", key, value);
        switch (key) {
            case "title":
                task.setTitle((String) value);
                break;
            case "description":
                task.setDescription((String) value);
                break;
            case "completed":
                task.setCompleted((Boolean) value);
                break;
            default:
                logger.warn("Unknown field: {}", key);
        }
    });

    Task updatedTask = taskRepository.save(task);
    logger.info("Task partially updated successfully with ID: {}", updatedTask.getId());
    return updatedTask;
  }
}