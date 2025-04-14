package com.app.project.controller;

import com.app.project.model.Task;
import com.app.project.service.TaskService;  

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    // @Autowired
    // private TaskService taskService;

    // @PostMapping
    // public ResponseEntity<Task> saveTask(@RequestBody Task task){
    //     return new ResponseEntity<Task>(taskService.saveTask(task), HttpStatus.CREATED);
    // }
    // //GetAll Rest Api
    // @GetMapping
    // public List<Task> getAllTask(){
    //     return TaskService.getAllTask();
    // }

    // //Get by Id Rest Api
    // @GetMapping("{id}")
    // // localhost:8080/api/tasks/1
    // public ResponseEntity<Task> getTaskById(@PathVariable("id") long taskID){
    //     return new ResponseEntity<Task>(taskService.getTaskById(taskID),HttpStatus.OK);
    // }

    // //Update Rest Api
    // @PutMapping("{id}")
    // public ResponseEntity<Task> updateTask(@PathVariable("id") long id,
    //                                                @RequestBody Task task){
    //     return new ResponseEntity<Task>(taskService.updateTask(task,id),HttpStatus.OK);
    // }

    // //Delete Rest Api
    // @DeleteMapping("{id}")
    // public ResponseEntity<String> deleteTask(@PathVariable("id") long id){
    //     //delete Task from db
    //     taskService.deleteTask(id);
    //     return new ResponseEntity<String>("Task deleted Successfully.",HttpStatus.OK);
    // }

}