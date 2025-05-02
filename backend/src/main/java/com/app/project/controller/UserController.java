package com.app.project.controller;

import com.app.project.model.Project;
import com.app.project.model.User;
import com.app.project.service.ProjectMemberService;
import com.app.project.service.ProjectService;
import com.app.project.service.UserService;  

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectMemberService projectMemberService;

    @Autowired
    private ProjectService projectService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        // Basic validation
        if (user.getUsername() == null || user.getUsername().isEmpty() ||
            user.getEmail() == null || user.getEmail().isEmpty() ||
            user.getPassword() == null || user.getPassword().isEmpty() ||
            user.getFirstName() == null || user.getFirstName().isEmpty() ||
            user.getLastName() == null || user.getLastName().isEmpty()) {
            
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "All fields are required");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        
        try {
            // Check if email already exists
            if (userService.existsByEmail(user.getEmail())) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Email already in use");
                return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
            }
            
            // Check if username already exists
            if (userService.existsByUsername(user.getUsername())) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Username already taken");
                return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
            }
            
            User savedUser = userService.saveUser(user);
            return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to register user: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> credentials) {
        String usernameOrEmail = credentials.get("username");
        String password = credentials.get("password");
        
        if (usernameOrEmail == null || password == null) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Username/email and password are required");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        
        try {
            // First try to find by username
            User user = userService.getUserByUsername(usernameOrEmail);
            
            // If not found, try by email
            if (user == null) {
                user = userService.getUserByEmail(usernameOrEmail);
            }
            
            // If user found, check password
            if (user != null && user.verify(usernameOrEmail, usernameOrEmail, password)) {
                // Create response with user info but exclude sensitive data
                Map<String, Object> response = new HashMap<>();
                response.put("userId", user.getUserID());
                response.put("username", user.getUsername());
                response.put("email", user.getEmail());
                response.put("firstName", user.getFirstName());
                response.put("lastName", user.getLastName());
                response.put("message", "Login successful");
                
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Invalid username/email or password");
                return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Login failed: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<User> saveUser(@RequestBody User user){
        return new ResponseEntity<User>(userService.saveUser(user), HttpStatus.CREATED);
    }
    //GetAll Rest Api
    @GetMapping
    public List<User> getAllUser(){
        return userService.getAllUsers();
    }

    //Get by Id Rest Api
    @GetMapping("{id}")
    // localhost:8080/api/Users/1
    public ResponseEntity<User> getUserById(@PathVariable("id") int userID){
        return new ResponseEntity<User>(userService.getUserByID(userID),HttpStatus.OK);
    }

    //Delete Rest Api
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") int id){
        //delete User from db
        userService.deleteUser(id);
        return new ResponseEntity<String>("User deleted Successfully.",HttpStatus.OK);
    }

    // get all projects user is a member of
    @GetMapping("/{userID}/projects")
    public ResponseEntity<?> getProjectsByUserId(@PathVariable int userID) {
        User user = userService.getUserByID(userID);
        if (user == null) {
            return new ResponseEntity<>("User not found with ID: " + userID, HttpStatus.NOT_FOUND);
        }
        List<Integer> projectIDs = user.getProjectIDs();
        if (projectIDs.isEmpty()) {
            return new ResponseEntity<>("User is not a member of any projects", HttpStatus.NOT_FOUND);
        }

        List<Project> projects = new ArrayList<>();
        for (int projectID : projectIDs) {
            Optional<Project> project = projectService.getProject(projectID);
            if (project.isPresent()) {
                projects.add(project.get());
            }
        }
        if (projects.isEmpty()) {
            return new ResponseEntity<>("No projects found for user with ID: " + userID, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }
}
