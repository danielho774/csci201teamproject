package com.app.project.repository; 

import com.app.project.model.TaskAssignments;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskAssignmentsRepository extends JpaRepository<TaskAssignments, Integer> {
    
}
