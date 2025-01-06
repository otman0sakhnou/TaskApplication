# TaskApplication

This is a simple task management system that allows users to create, update, search, and delete tasks. The project is built with Spring Boot and uses JPA for database operations. The API supports CRUD operations for tasks, with validation on input fields and detailed error messages.

## Table of Contents

- [Technology Stack](#technology-stack)
- [API Endpoints](#api-endpoints)
  - [Create a New Task](#create-a-new-task)
  - [Update a Task](#update-a-task)
  - [Search Tasks](#search-tasks)
  - [Delete a Task](#delete-a-task)
  - [Partial Update a Task](#partial-update-a-task)
- [Running the Project](#running-the-project)
- [Testing](#testing)
- [License](#license)

## Technology Stack

- **Spring Boot**: The framework for building the API.
- **Hibernate/JPA**: For ORM and database interactions.
- **Spring Data JPA**: For repository management.
- **JUnit**: For testing the application.
- **Mockito**: For mocking dependencies in tests.

## API Endpoints

### Create a New Task

- **Endpoint**: `POST /api/tasks`
- **Description**: Creates a new task.
- **Request Body**:
  ```json
  {
    "title": "New Task",
    "description": "This is a new task",
    "completed": false
  }
