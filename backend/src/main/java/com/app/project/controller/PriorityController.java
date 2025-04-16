package com.app.project.controller;

import com.app.project.model.Priority;
import com.app.project.service.PriorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/priorities")
public class PriorityController {

    @Autowired
    private PriorityService priorityService;

    @GetMapping("/{id}")
    public ResponseEntity<Priority> getPriorityById(@PathVariable long id) {
        Priority priority = priorityService.getPriorityById(id);
        if (priority != null) {
            return new ResponseEntity<>(priority, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<List<Priority>> getAllPriorities() {
        List<Priority> priorities = priorityService.getAllPriorities();
        return new ResponseEntity<>(priorities, HttpStatus.OK);
    }
}