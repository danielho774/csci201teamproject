package com.app.project.repository;

import com.app.project.model.Task;
import com.app.project.model.Project;
import com.app.project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
    // Find tasks by project
    List<Task> findByProject(Project project);
    
    // Find tasks by status
    List<Task> findByStatus_StatusName(String statusName);
    
    // Find tasks by priority
    List<Task> findByPriority_PriorityName(String priorityName);
}