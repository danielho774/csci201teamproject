package com.app.project.repository; 

import com.app.project.model.TaskStatus; 
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusRepository extends JpaRepository<TaskStatus, Integer> {
    
}

