package com.app.project.repository; 

import com.app.project.model.User; 
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    // create findByID
}
