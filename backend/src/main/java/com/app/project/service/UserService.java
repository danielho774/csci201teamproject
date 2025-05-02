package com.app.project.service; 

import com.app.project.model.User; 
import java.util.List; 

import org.springframework.stereotype.Service;

@Service
public interface UserService {
    User saveUser(User user); 
    List<User> getAllUsers(); 
    User getUserByID(int id); 
    void deleteUser(int id); 
    
    // Add these methods
    User getUserByUsername(String username);
    User getUserByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}