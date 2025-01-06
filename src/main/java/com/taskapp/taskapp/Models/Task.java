package com.taskapp.taskapp.Models;

import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class Task {
  @Id
  @UuidGenerator
  @Column(name = "id", updatable = false, nullable = false, columnDefinition = "BINARY(16)")
  private UUID id;

  @NotBlank(message = "Title is required")
  @NotNull(message = "Title cannot be null")
  @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
  @Column(nullable = false)
  private String title;

  @Size(max = 500, message = "Description cannot exceed 500 characters")
  private String description;

  @NotNull(message = "Completed status must be specified")
  @Column(nullable = false)
  private Boolean completed = false;

  public UUID getId() {
    return id;
  }
  public void setId(UUID id) { 
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public boolean isCompleted() {
    return completed;
  }

  public void setCompleted(boolean completed) {
    this.completed = completed;
  }



}
