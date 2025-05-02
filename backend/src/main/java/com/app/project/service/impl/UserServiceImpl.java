package com.app.project.service.impl; 

import com.app.project.model.User; 
import com.app.project.repository.UserRepository;
import com.app.project.service.UserService; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List; 
import java.util.Optional;

@Service 
public class UserServiceImpl implements UserService {
    @Autowired 
    private UserRepository userRepository; 

    // save user in database 
    public User saveUser(User user) {
        return userRepository.save(user); 
    }

    public List<User> getAllUsers() {
        return userRepository.findAll(); 
    }

    public User getUserByID(int id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return user.get(); 
        }
        else {
            return null;
        }
    }

    public void deleteUser(int id) {
        userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found with ID: " + id)); 
        userRepository.deleteById(id); 
    }
    
    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
    
    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
    
    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
