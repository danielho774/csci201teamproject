package com.app.project.repository; 

import com.app.project.model.TaskPriority; 
import org.springframework.data.jpa.repository.JpaRepository;

public interface PriorityRepository extends JpaRepository<TaskPriority, Integer> {
    
}

