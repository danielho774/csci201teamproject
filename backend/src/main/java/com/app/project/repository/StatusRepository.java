package com.app.project.repository; 

import com.app.project.model.Status; 
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusRepository extends JpaRepository<Status, Integer> {
    
}

