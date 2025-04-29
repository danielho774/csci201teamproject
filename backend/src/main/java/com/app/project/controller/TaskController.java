package com.app.project.controller;

import com.app.project.model.Task;
import com.app.project.model.Comment;
import com.app.project.model.TaskStatus;
import com.app.project.model.User; // Import User model
import com.app.project.service.TaskService;
import com.app.project.service.UserService; // Import UserService

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    // Inject UserService to fetch User objects by ID
    @Autowired
    private UserService userService;

    // Create task
    @PostMapping
    public ResponseEntity<Task> saveTask(@RequestBody Task task) {
        // Consider fetching Project, Status, Priority objects based on IDs if needed before saving
        // For example:
        // Project project = projectService.getProjectById(task.getProject().getProjectID());
        // TaskStatus status = statusService.getStatusById(task.getStatus().getStatusID());
        // TaskPriority priority = priorityService.getPriorityById(task.getPriority().getPriorityID());
        // task.setProject(project);
        // task.setStatus(status);
        // task.setPriority(priority);
        // // Error handling if related objects are not found
        return new ResponseEntity<>(taskService.saveTask(task), HttpStatus.CREATED);
    }

    // Get all tasks
    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        return new ResponseEntity<>(taskService.getAllTasks(), HttpStatus.OK);
    }

    // Get task by ID
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable("id") long taskId) {
        // Exception handling for not found is likely in the service layer
        return new ResponseEntity<>(taskService.getTaskById(taskId), HttpStatus.OK);
    }

    // Update task
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable("id") long id, @RequestBody Task task) {
        // Similar to create, ensure related objects are handled if IDs are passed in request body
        return new ResponseEntity<>(taskService.updateTask(task, id), HttpStatus.OK);
    }

    // Delete task
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable("id") long id) {
        taskService.deleteTask(id);
        return new ResponseEntity<>("Task deleted successfully", HttpStatus.OK);
    }

    // Get tasks by project
    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<Task>> getTasksByProject(@PathVariable("projectId") long projectId) {
        // This endpoint still needs full implementation
        // You'll need ProjectService injected to fetch the Project object first
        // Project project = projectService.getProjectById(projectId);
        // List<Task> tasks = taskService.getTasksByProject(project);
        // return new ResponseEntity<>(tasks, HttpStatus.OK);
        System.out.println("Warning: /api/tasks/project/{projectId} endpoint not fully implemented."); // Added warning
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED); // Kept as NOT_IMPLEMENTED for now
    }

    // Get tasks assigned to a specific user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Task>> getTasksByUser(@PathVariable("userId") long userId) {
        try {
            // Fetch the User object using the injected UserService
            User user = userService.getUserByID(userId); // Use getUserByID from UserService
            // Call the service method (potentially renamed, e.g., getTasksAssignedToUser)
            List<Task> tasks = taskService.getTasksByUser(user); // Assumes TaskService method name is getTasksByUser
            return new ResponseEntity<>(tasks, HttpStatus.OK);
        } catch (RuntimeException e) { // Catch potential exception if user not found
             // Log the exception e.getMessage()
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Or handle as appropriate
        }
    }

    // Assign task to user
    @PostMapping("/{taskId}/assign/{userId}")
    public ResponseEntity<String> assignTaskToUser(
            @PathVariable("taskId") long taskId,
            @PathVariable("userId") long userId) {
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
            @PathVariable("userId") long userId) {
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
    public ResponseEntity<String> updateTaskStatus(
            @PathVariable("taskId") long taskId,
            @RequestBody TaskStatus status) { // Assuming TaskStatus has an ID to find the real entity
        // Might need StatusService injected to fetch the full TaskStatus object first
        // TaskStatus fullStatus = statusService.getStatusById(status.getStatusID());
        // boolean success = taskService.updateTaskStatus(taskId, fullStatus);
        boolean success = taskService.updateTaskStatus(taskId, status); // Assuming service handles lookup
        if (success) {
            return new ResponseEntity<>("Task status updated successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Failed to update task status", HttpStatus.BAD_REQUEST);
        }
    }

    // Get tasks by status
    @GetMapping("/status/{statusName}")
    public ResponseEntity<List<Task>> getTasksByStatus(@PathVariable("statusName") String statusName) {
        return new ResponseEntity<>(taskService.getTasksByStatus(statusName), HttpStatus.OK);
    }

    // Get tasks by priority
    @GetMapping("/priority/{priorityName}")
    public ResponseEntity<List<Task>> getTasksByPriority(@PathVariable("priorityName") String priorityName) {
        return new ResponseEntity<>(taskService.getTasksByPriority(priorityName), HttpStatus.OK);
    }

    // Add comment to task
    @PostMapping("/{taskId}/comment")
    public ResponseEntity<String> addCommentToTask(
            @PathVariable("taskId") long taskId,
            @RequestBody Comment comment) {
         // Ensure comment has required fields set (e.g., Member/User info if needed)
        boolean success = taskService.addCommentToTask(taskId, comment);
        if (success) {
            return new ResponseEntity<>("Comment added successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Failed to add comment", HttpStatus.BAD_REQUEST);
        }
    }
}