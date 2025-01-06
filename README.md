# TaskApplication

This is a simple task management system that allows users to create, update, search, and delete tasks. The project is built with Spring Boot and uses JPA for database operations. The API supports CRUD operations for tasks, with validation on input fields and detailed error messages.

## Table of Contents

- [Technology Stack](#technology-stack)
- [API Endpoints](#api-endpoints)
  - [Search Tasks](#search-tasks)
  - [Create a New Task](#create-a-new-task)
  - [Update a Task](#update-a-task)
  - [Delete a Task](#delete-a-task)
  - [Partial Update a Task](#partial-update-a-task)
- [Running the Project](#running-the-project)
- [Testing](#testing)
- [License](#license)

## Technology Stack

- **Spring Boot**: The framework for building the API
- **Hibernate/JPA**: For ORM and database interactions
- **Spring Data JPA**: For repository management
- **JUnit**: For testing the application
- **Mockito**: For mocking dependencies in tests

## API Endpoints

### Search Tasks

- **Endpoint**: `GET /api/tasks`
- **Description**: Allows searching for tasks with pagination and sorting options
- **Query Parameters**:
  - `search`: Search term (optional)
  - `page`: Page number (default: 0)
  - `size`: Number of items per page (default: 10)
  - `sortBy`: Field to sort by (default: id)
  - `sortDir`: Sort direction (ASC or DESC, default: DESC)
- **Response**: Returns a paginated list of tasks
- **Success Example**:
  ```json
  {
    "content": [
      {
        "id": "c9bdf5c8-01f2-4e1e-8be7-91fe8a5c338a",
        "title": "Search Task",
        "description": "Task for search",
        "completed": false
      }
    ]
  }
  ```

### Create a New Task

- **Endpoint**: `POST /api/tasks`
- **Description**: Creates a new task
- **Request Body**:
  ```json
  {
    "title": "New Task",
    "description": "This is a new task",
    "completed": false
  }
  ```

### Update a Task

- **Endpoint**: `PUT /api/tasks/{id}`
- **Description**: Updates the details of an existing task
- **Request Body**:
  ```json
  {
    "title": "Updated Task",
    "description": "Updated task description",
    "completed": true
  }
  ```
- **Response**: Returns the updated task with a 200 status code
- **Success Example**:
  ```json
  {
    "id": "c9bdf5c8-01f2-4e1e-8be7-91fe8a5c338a",
    "title": "Updated Task",
    "description": "Updated task description",
    "completed": true
  }
  ```

### Delete a Task

- **Endpoint**: `DELETE /api/tasks/{id}`
- **Description**: Deletes a task by its ID
- **Response**: Returns a 204 status code for successful deletion

### Partial Update a Task

- **Endpoint**: `PATCH /api/tasks/{id}`
- **Description**: Allows partial updates to a task
- **Request Body**:
  ```json
  {
    "title": "Partially Updated Task",
    "completed": true
  }
  ```
- **Response**: Returns the updated task
- **Success Example**:
  ```json
  {
    "id": "c9bdf5c8-01f2-4e1e-8be7-91fe8a5c338a",
    "title": "Partially Updated Task",
    "completed": true
  }
  ```

## Running the Project

1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/taskapp.git
   cd taskapp
   ```

2. Build the project:
   ```bash
   ./mvnw clean install
   ```

3. Run the project:
   ```bash
   ./mvnw spring-boot:run
   ```
The API will start on http://localhost:8081. You can access:
  - **API endpoints at http://localhost:8081**
  - **Swagger UI documentation at http://localhost:8081/swagger-ui/index.html#/**

## Testing

This project includes tests for both the controller and service layers. You can run the tests with:

```bash
./mvnw test
```

The tests use JUnit and Mockito to ensure the functionality of your API endpoints and service logic.
