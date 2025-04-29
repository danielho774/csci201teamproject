package com.app.project.service.impl;

import com.app.project.model.Task;
import com.app.project.model.Project;
import com.app.project.model.User;
import com.app.project.model.Comment;
import com.app.project.model.TaskStatus;
import com.app.project.repository.TaskRepository;
import com.app.project.repository.UserRepository;
import com.app.project.service.TaskService;
import com.app.project.repository.TaskAssignmentsRepository;
import com.app.project.repository.ProjectMemberRepository;
import com.app.project.model.ProjectMember;
import com.app.project.model.TaskAssignments;
import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskAssignmentsRepository taskAssignmentsRepository;

    @Autowired
    private ProjectMemberRepository projectMemberRepository;

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
    @Transactional // Use transaction to ensure atomicity
    public boolean assignTaskToUser(long taskId, long userId) {
        Task task = getTaskById(taskId); // Finds task or throws exception
        User user = userRepository.findById(userId).orElseThrow(
                () -> new RuntimeException("User not found with id: " + userId)
        );

        // Find the ProjectMember link
        // Assuming Task has getProject() and Project has getProjectID()
        int projectId = task.getProject().getProjectID();
        ProjectMember member = projectMemberRepository.findByUserUserIDAndProjectProjectID((int) userId, projectId)
                .orElseThrow(() -> new RuntimeException("User " + userId + " is not a member of project " + projectId));

        // Check if already assigned
        if (taskAssignmentsRepository.existsByTaskTaskIDAndMemberMemberID(task.getTaskID(), member.getMemberID())) {
            return false; // Already assigned
        }

        // Create and save the assignment
        TaskAssignments assignment = new TaskAssignments(task, member);
        taskAssignmentsRepository.save(assignment);

        // Update task's assigned flag if it wasn't already
        if (!task.isAssigned()) {
            task.setAssigned(true);
            taskRepository.save(task);
        }
        return true;
    }
    
    @Override
    @Transactional // Use transaction
    public boolean removeUserFromTask(long taskId, long userId) {
        Task task = getTaskById(taskId); // Finds task or throws exception
        User user = userRepository.findById(userId).orElseThrow(
            () -> new RuntimeException("User not found with id: " + userId)
        );

        // Find the ProjectMember link
        int projectId = task.getProject().getProjectID();
        ProjectMember member = projectMemberRepository.findByUserUserIDAndProjectProjectID((int) userId, projectId)
            .orElseThrow(() -> new RuntimeException("User " + userId + " is not a member of project " + projectId));

        // Find and delete the assignment
        TaskAssignments assignment = taskAssignmentsRepository.findByTaskTaskIDAndMemberMemberID(task.getTaskID(), member.getMemberID())
            .orElse(null); // Find the specific assignment

        if (assignment != null) {
            taskAssignmentsRepository.delete(assignment);

            // Check if any assignments remain for this task
            boolean stillAssigned = taskAssignmentsRepository.existsByTaskTaskID(task.getTaskID());
            if (task.isAssigned() != stillAssigned) {
                task.setAssigned(stillAssigned);
                taskRepository.save(task);
            }
            return true; // Successfully removed
        }
        return false; // Assignment didn't exist
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
    public List<Task> getTasksAssignedToUser(User user) {
    // Find all project memberships for the user
    List<ProjectMember> memberships = projectMemberRepository.findByUserUserID(user.getUserID());

    // Get all assignment entities for those memberships
    List<TaskAssignments> allAssignments = memberships.stream()
            .flatMap(member -> taskAssignmentsRepository.findByMemberMemberID(member.getMemberID()).stream())
            .collect(Collectors.toList());

    // Extract the unique tasks from the assignments
    return allAssignments.stream()
            .map(TaskAssignments::getTask)
            .distinct()
            .collect(Collectors.toList());
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