package com.app.project.service; 

import com.app.project.model.User; 
import java.util.List; 

public interface UserService {
    User saveUser(User user); 
    List<User> getAllUsers(); 
    User getUserByID(int id); 
    void deleteUser(int id); 
}