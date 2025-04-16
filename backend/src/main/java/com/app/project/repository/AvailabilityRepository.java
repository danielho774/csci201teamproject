package com.app.project.repository; 

import com.app.project.model.Availability; 
import org.springframework.data.jpa.repository.JpaRepository;

public interface AvailabilityRepository extends JpaRepository<Availability, Integer> {
    
}
