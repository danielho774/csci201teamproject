package com.app.project.controller;

import com.app.project.model.User;
import com.app.project.service.UserService;  

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

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

}