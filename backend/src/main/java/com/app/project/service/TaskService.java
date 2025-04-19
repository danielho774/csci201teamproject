package com.app.project.service;

import com.app.project.model.Task;
import com.app.project.model.Project;
import com.app.project.model.User;
import com.app.project.model.Comment;
import com.app.project.model.Status;

import java.util.List;

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
    List<Task> getTasksByUser(User user);
    boolean assignTaskToUser(long taskId, long userId);
    boolean removeUserFromTask(long taskId, long userId);
    
    // Status related
    boolean updateTaskStatus(long taskId, Status status);
    List<Task> getTasksByStatus(String statusName);
    
    // Priority related
    List<Task> getTasksByPriority(String priorityName);
    
    // Comment related
    boolean addCommentToTask(long taskId, Comment comment);
}