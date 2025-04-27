package com.app.project.service.impl;

import com.app.project.model.Task;
import com.app.project.model.Project;
import com.app.project.model.User;
import com.app.project.model.Comment;
import com.app.project.model.TaskStatus;
import com.app.project.repository.TaskRepository;
import com.app.project.repository.UserRepository;
import com.app.project.service.TaskService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository taskRepository;
    
    @Autowired
    private UserRepository userRepository;

    @Override
    public Task saveTask(Task task) {
        return taskRepository.save(task);
    }

    @Override
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @Override
    public Task getTaskById(long id) {
        return taskRepository.findById((int) id).orElseThrow(
            () -> new RuntimeException("Task not found with id: " + id)
        );
    }

    @Override
    public Task updateTask(Task task, long id) {
        Task existingTask = getTaskById(id);
        
        existingTask.setTaskName(task.getTaskName());
        existingTask.setTaskDescrip(task.getTaskDescrip());
        existingTask.setStatus(task.getStatus());
        existingTask.setPriority(task.getPriority());
        existingTask.setStartDate(task.getStartDate());
        existingTask.setEndDate(task.getEndDate());
        existingTask.setDuration(task.getDuration());
        existingTask.setAssigned(task.isAssigned());
        
        return taskRepository.save(existingTask);
    }

    @Override
    public void deleteTask(long id) {
        getTaskById(id); // Check if exists
        taskRepository.deleteById((int) id);
    }
    
    @Override
    public List<Task> getTasksByProject(Project project) {
        return taskRepository.findByProject(project);
    }
    
    @Override
    public List<Task> getTasksByUser(User user) {
        return taskRepository.findByAssigneesContaining(user);
    }
    
    @Override
    public boolean assignTaskToUser(long taskId, long userId) {
        Task task = getTaskById(taskId);
        User user = userRepository.findById(userId).orElseThrow(
            () -> new RuntimeException("User not found with id: " + userId)
        );
        
        boolean result = task.addAssignee(user);
        if (result) {
            taskRepository.save(task);
        }
        return result;
    }
    
    @Override
    public boolean removeUserFromTask(long taskId, long userId) {
        Task task = getTaskById(taskId);
        User user = userRepository.findById(userId).orElseThrow(
            () -> new RuntimeException("User not found with id: " + userId)
        );
        
        boolean result = task.removeAssignee(user);
        if (result) {
            taskRepository.save(task);
        }
        return result;
    }
    
    @Override
    public boolean updateTaskStatus(long taskId, TaskStatus status) {
        Task task = getTaskById(taskId);
        boolean result = task.updateStatus(status);
        if (result) {
            taskRepository.save(task);
        }
        return result;
    }
    
    @Override
    public List<Task> getTasksByStatus(String statusName) {
        return taskRepository.findByStatus_StatusName(statusName);
    }
    
    @Override
    public List<Task> getTasksByPriority(String priorityName) {
        return taskRepository.findByPriority_PriorityName(priorityName);
    }
    
    @Override
    public boolean addCommentToTask(long taskId, Comment comment) {
        Task task = getTaskById(taskId);
        boolean result = task.addComment(comment);
        if (result) {
            taskRepository.save(task);
        }
        return result;
    }
}