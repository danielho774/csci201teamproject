package com.app.project.controller;

import com.app.project.model.Task;
import com.app.project.model.TaskStatus;
import com.app.project.model.User; // Import User model
import com.app.project.service.TaskService;
import com.app.project.service.UserService; // Import UserService
import com.app.project.service.ProjectService;
import com.app.project.model.Project;
import com.app.project.model.TaskAssignments;
import com.app.project.repository.TaskAssignmentsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private TaskService taskService;

    // Inject UserService to fetch User objects by ID
    @Autowired
    private UserService userService;

    @Autowired
    private TaskAssignmentsRepository taskAssignmentsRepository;

    // Create task
    @PostMapping
    public ResponseEntity<?> saveTask(@RequestBody Task task) {
        Task savedTask = taskService.saveTask(task);
        
        // Return a simplified response to avoid serialization issues
        Map<String, Object> response = new HashMap<>();
        response.put("taskID", savedTask.getTaskID());
        response.put("taskName", savedTask.getTaskName());
        response.put("projectID", savedTask.getProject().getProjectID());
        response.put("statusID", savedTask.getStatus().getStatusID());
        response.put("message", "Task created successfully");
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Get all tasks
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllTasks() {
        List<Task> tasks = taskService.getAllTasks();
        List<Map<String, Object>> simplifiedTasks = tasks.stream()
            .map(task -> {
                Map<String, Object> taskDto = new HashMap<>();
                taskDto.put("taskID", task.getTaskID());
                taskDto.put("taskName", task.getTaskName());
                taskDto.put("description", task.getTaskDescrip());
                taskDto.put("projectID", task.getProject().getProjectID());
                taskDto.put("projectName", task.getProject().getProjectName());
                taskDto.put("statusID", task.getStatus().getStatusID());
                taskDto.put("statusName", task.getStatus().getStatusName());
                taskDto.put("startDate", task.getStartDate());
                taskDto.put("endDate", task.getEndDate());
                taskDto.put("duration", task.getDuration());
                taskDto.put("assigned", task.isAssigned());
                return taskDto;
            })
            .collect(Collectors.toList());
        
        return new ResponseEntity<>(simplifiedTasks, HttpStatus.OK);
    }

    // Get task by ID
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getTaskById(@PathVariable("id") long taskId) {
        try {
            Task task = taskService.getTaskById(taskId);
            
            // Create a simplified task DTO
            Map<String, Object> taskDto = new HashMap<>();
            taskDto.put("taskID", task.getTaskID());
            taskDto.put("taskName", task.getTaskName());
            taskDto.put("description", task.getTaskDescrip());
            taskDto.put("projectID", task.getProject().getProjectID());
            taskDto.put("projectName", task.getProject().getProjectName());
            taskDto.put("statusID", task.getStatus().getStatusID());
            taskDto.put("statusName", task.getStatus().getStatusName());
            taskDto.put("startDate", task.getStartDate());
            taskDto.put("endDate", task.getEndDate());
            taskDto.put("duration", task.getDuration());
            taskDto.put("assigned", task.isAssigned());
            
            return new ResponseEntity<>(taskDto, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    // Update task
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateTask(@PathVariable("id") long id, @RequestBody Task task) {
        try {
            Task updatedTask = taskService.updateTask(task, id);
            
            // Create a simplified response
            Map<String, Object> response = new HashMap<>();
            response.put("taskID", updatedTask.getTaskID());
            response.put("taskName", updatedTask.getTaskName());
            response.put("description", updatedTask.getTaskDescrip());
            response.put("projectID", updatedTask.getProject().getProjectID());
            response.put("projectName", updatedTask.getProject().getProjectName());
            response.put("statusID", updatedTask.getStatus().getStatusID());
            response.put("statusName", updatedTask.getStatus().getStatusName());
            response.put("startDate", updatedTask.getStartDate());
            response.put("endDate", updatedTask.getEndDate());
            response.put("duration", updatedTask.getDuration());
            response.put("assigned", updatedTask.isAssigned());
            response.put("message", "Task updated successfully");
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to update task: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    // Delete task
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable("id") long id) {
        taskService.deleteTask(id);
        return new ResponseEntity<>("Task deleted successfully", HttpStatus.OK);
    }

    // Get tasks by project
    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<Map<String, Object>>> getTasksByProject(@PathVariable("projectId") long projectId) {
        try {
            // First, get the Project object using the projectId
            Project project = projectService.getProjectByID((int)projectId);
            
            // Then pass the Project object to the service method
            List<Task> tasks = taskService.getTasksByProject(project);
            
            // Map tasks to DTOs
            List<Map<String, Object>> taskDtos = tasks.stream()
                .map(task -> {
                    Map<String, Object> dto = new HashMap<>();
                    dto.put("taskID", task.getTaskID());
                    dto.put("taskName", task.getTaskName());
                    dto.put("description", task.getTaskDescrip());
                    dto.put("projectID", task.getProject().getProjectID());
                    dto.put("projectName", task.getProject().getProjectName());
                    dto.put("statusID", task.getStatus().getStatusID());
                    dto.put("statusName", task.getStatus().getStatusName());
                    dto.put("startDate", task.getStartDate());
                    dto.put("endDate", task.getEndDate());
                    dto.put("duration", task.getDuration());
                    dto.put("assigned", task.isAssigned());
                    
                    // Find who is assigned to this task (if anyone)
                    int assignedUserId = -1;
                    try {
                        // Get all assignments for this task
                        List<TaskAssignments> assignments = taskAssignmentsRepository.findByTaskTaskID(task.getTaskID());
                        if (!assignments.isEmpty()) {
                            // Get the first assignment's ProjectMember's User ID
                            assignedUserId = assignments.get(0).getMember().getUserID();
                        }
                    } catch (Exception e) {
                        System.out.println("Error getting assigned user: " + e.getMessage());
                    }
                    
                    dto.put("assignedUserId", assignedUserId);
                    
                    return dto;
                })
                .collect(Collectors.toList());
            
            return new ResponseEntity<>(taskDtos, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Get tasks assigned to a specific user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Map<String, Object>>> getTasksByUser(@PathVariable("userId") int userId) {
        try {
            // Fetch the User object using the injected UserService
            User user = userService.getUserByID(userId);
            
            // Call the service method to get tasks assigned to user
            List<Task> tasks = taskService.getTasksAssignedToUser(user);
            
            // Map tasks to DTOs
            List<Map<String, Object>> taskDtos = tasks.stream()
                .map(task -> {
                    Map<String, Object> dto = new HashMap<>();
                    dto.put("taskID", task.getTaskID());
                    dto.put("taskName", task.getTaskName());
                    dto.put("description", task.getTaskDescrip());
                    dto.put("projectID", task.getProject().getProjectID());
                    dto.put("projectName", task.getProject().getProjectName());
                    dto.put("statusID", task.getStatus().getStatusID());
                    dto.put("statusName", task.getStatus().getStatusName());
                    dto.put("startDate", task.getStartDate());
                    dto.put("endDate", task.getEndDate());
                    dto.put("duration", task.getDuration());
                    dto.put("assigned", task.isAssigned());
                    return dto;
                })
                .collect(Collectors.toList());
            
            return new ResponseEntity<>(taskDtos, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Assign task to user
    @PostMapping("/{taskId}/assign/{userId}")
    public ResponseEntity<String> assignTaskToUser(
            @PathVariable("taskId") long taskId,
            @PathVariable("userId") int userId) {
        try {
             boolean success = taskService.assignTaskToUser(taskId, userId);
             if (success) {
                 return new ResponseEntity<>("Task assigned successfully", HttpStatus.OK);
             } else {
                 // This might indicate the user was already assigned or user is not a project member
                 return new ResponseEntity<>("Task assignment did not complete (may already be assigned or user not project member)", HttpStatus.OK);
             }
        } catch (RuntimeException e) { // Catch exceptions like User/Task/ProjectMember not found
            // Log the exception e.getMessage()
            return new ResponseEntity<>("Failed to assign task: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // Remove user from task - Corrected Path
    @DeleteMapping("/{taskId}/remove/{userId}") // CHANGED PATH from /assign/ to /remove/
    public ResponseEntity<String> removeUserFromTask(
            @PathVariable("taskId") long taskId,
            @PathVariable("userId") int userId) {
         try {
            boolean success = taskService.removeUserFromTask(taskId, userId);
            if (success) {
                return new ResponseEntity<>("User removed from task successfully", HttpStatus.OK);
            } else {
                // This might indicate the user wasn't assigned in the first place
                return new ResponseEntity<>("User removal did not complete (user may not have been assigned)", HttpStatus.OK);
            }
         } catch (RuntimeException e) { // Catch exceptions like User/Task/ProjectMember not found
             // Log the exception e.getMessage()
             return new ResponseEntity<>("Failed to remove user from task: " + e.getMessage(), HttpStatus.BAD_REQUEST);
         }
    }

    // Update task status
    @PutMapping("/{taskId}/status")
    public ResponseEntity<Map<String, Object>> updateTaskStatus(
            @PathVariable("taskId") long taskId,
            @RequestBody TaskStatus status) {
        try {
            System.out.println("Received status update request for task " + taskId + ", status ID: " + status.getStatusID());
            
            boolean success = taskService.updateTaskStatus(taskId, status);
            
            if (success) {
                // Get the updated task to return the current state
                Task updatedTask = taskService.getTaskById(taskId);
                Map<String, Object> response = new HashMap<>();
                response.put("taskID", updatedTask.getTaskID());
                response.put("statusID", updatedTask.getStatus().getStatusID());
                response.put("statusName", updatedTask.getStatus().getStatusName());
                response.put("message", "Task status updated successfully");
                
                System.out.println("Task status updated successfully: " + updatedTask.getStatus().getStatusName());
                
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("error", "Failed to update task status");
                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            System.out.println("Error in controller updating task status: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error updating task status: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get tasks by status
    @GetMapping("/status/{statusName}")
    public ResponseEntity<List<Map<String, Object>>> getTasksByStatus(@PathVariable("statusName") String statusName) {
        List<Task> tasks = taskService.getTasksByStatus(statusName);
        
        List<Map<String, Object>> taskDtos = tasks.stream()
            .map(task -> {
                Map<String, Object> dto = new HashMap<>();
                dto.put("taskID", task.getTaskID());
                dto.put("taskName", task.getTaskName());
                dto.put("description", task.getTaskDescrip());
                dto.put("projectID", task.getProject().getProjectID());
                dto.put("projectName", task.getProject().getProjectName());
                dto.put("statusID", task.getStatus().getStatusID());
                dto.put("statusName", task.getStatus().getStatusName());
                dto.put("startDate", task.getStartDate());
                dto.put("endDate", task.getEndDate());
                dto.put("duration", task.getDuration());
                dto.put("assigned", task.isAssigned());
                return dto;
            })
            .collect(Collectors.toList());
        
        return new ResponseEntity<>(taskDtos, HttpStatus.OK);
    }
}