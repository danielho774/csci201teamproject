package com.app.project.controller;

import com.app.project.model.Task;
import com.app.project.model.Comment;
import com.app.project.model.Status;
import com.app.project.service.TaskService;  

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

    // Create task
    @PostMapping
    public ResponseEntity<Task> saveTask(@RequestBody Task task) {
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
        return new ResponseEntity<>(taskService.getTaskById(taskId), HttpStatus.OK);
    }

    // Update task
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable("id") long id, @RequestBody Task task) {
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
        // You'll need to get the project first
        // Project project = projectService.getProjectById(projectId);
        // return new ResponseEntity<>(taskService.getTasksByProject(project), HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }
    
    // Get tasks by user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Task>> getTasksByUser(@PathVariable("userId") long userId) {
        // User user = userService.getUserById(userId);
        // return new ResponseEntity<>(taskService.getTasksByUser(user), HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }
    
    // Assign task to user
    @PostMapping("/{taskId}/assign/{userId}")
    public ResponseEntity<String> assignTaskToUser(
            @PathVariable("taskId") long taskId, 
            @PathVariable("userId") long userId) {
        boolean success = taskService.assignTaskToUser(taskId, userId);
        if (success) {
            return new ResponseEntity<>("Task assigned successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Failed to assign task", HttpStatus.BAD_REQUEST);
        }
    }
    
    // Remove user from task
    @DeleteMapping("/{taskId}/assign/{userId}")
    public ResponseEntity<String> removeUserFromTask(
            @PathVariable("taskId") long taskId, 
            @PathVariable("userId") long userId) {
        boolean success = taskService.removeUserFromTask(taskId, userId);
        if (success) {
            return new ResponseEntity<>("User removed from task successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Failed to remove user from task", HttpStatus.BAD_REQUEST);
        }
    }
    
    // Update task status
    @PutMapping("/{taskId}/status")
    public ResponseEntity<String> updateTaskStatus(
            @PathVariable("taskId") long taskId, 
            @RequestBody Status status) {
        boolean success = taskService.updateTaskStatus(taskId, status);
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
        boolean success = taskService.addCommentToTask(taskId, comment);
        if (success) {
            return new ResponseEntity<>("Comment added successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Failed to add comment", HttpStatus.BAD_REQUEST);
        }
    }
}