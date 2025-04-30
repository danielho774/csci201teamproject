package com.app.project.service;

import com.app.project.model.Task;
import com.app.project.model.Project;
import com.app.project.model.User;
import com.app.project.model.Comment;
import com.app.project.model.TaskStatus;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface TaskService {
    // Basic CRUD
    Task saveTask(Task task);
    List<Task> getAllTasks();
    Task getTaskById(long id);
    Task updateTask(Task task, long id);
    void deleteTask(long id);
    
    // Project related
    List<Task> getTasksByProject(Project project);
    
    // User related
    List<Task> getTasksAssignedToUser(User user); // Consider renaming for clarity
    boolean assignTaskToUser(long taskId, int userId);
    boolean removeUserFromTask(long taskId, int userId); // Keep this signature
    
    // Status related
    boolean updateTaskStatus(long taskId, TaskStatus status);
    List<Task> getTasksByStatus(String statusName);
    
    // Priority related
    List<Task> getTasksByPriority(String priorityName);
    
    // Comment related
    boolean addCommentToTask(long taskId, Comment comment);
}