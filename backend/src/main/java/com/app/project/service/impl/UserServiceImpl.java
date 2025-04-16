package com.app.project.service.impl; 

import com.app.project.model.User; 
import com.app.project.repository.UserRepository;
import com.app.project.service.UserService; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List; 
import java.util.Optional;

@Service 
public class UserServiceImpl implements User {
    @Autowired 
    private UserRepository userRepository; 

    // save user in database 
    public User saveUser(User user) {
        return userRepository.save(user); 
    }

    public List<User> getAllUsers() {
        return userRepository.findAll(); 
    }

    public User getUserById(long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return user.get(); 
        }
        else {
            throw new RuntimeException(); 
        }
    }

    public void deleteUser(long id) {
        userRepository.findById(id).orElseThrow(() -> new RuntimeException()); 
        userRepository.deleteById(id); 
    }
}
