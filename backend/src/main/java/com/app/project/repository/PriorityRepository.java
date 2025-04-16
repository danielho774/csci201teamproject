package com.app.project.repository; 

import com.app.project.model.Priority; 
import org.springframework.data.jpa.repository.JpaRepository;

public interface PriorityRepository extends JpaRepository<Priority, Integer> {
    
}

