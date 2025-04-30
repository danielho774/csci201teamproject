package com.app.project.controller;

import com.app.project.model.TaskPriority;
import com.app.project.service.TaskPriorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/priorities")
public class PriorityController {

    @Autowired
    private TaskPriorityService priorityService;

    @GetMapping("/{id}")
    public ResponseEntity<TaskPriority> getPriorityById(@PathVariable long id) {
        TaskPriority priority = priorityService.getPriorityById(id);
        if (priority != null) {
            return new ResponseEntity<>(priority, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<List<TaskPriority>> getAllPriorities() {
        List<TaskPriority> priorities = priorityService.getAllPriorities();
        return new ResponseEntity<>(priorities, HttpStatus.OK);
    }
}