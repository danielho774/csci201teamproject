package com.app.project.controller;

import com.app.project.model.TaskPriority;
import com.app.project.service.TaskPriorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import java.util.List;

@RestController
@RequestMapping("/api/priorities")
public class PriorityController {

    @Autowired
    private TaskPriorityService priorityService;

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getPriorityById(@PathVariable long id) {
        TaskPriority priority = priorityService.getPriorityById(id);
        if (priority != null) {
            Map<String, Object> priorityDto = new HashMap<>();
            priorityDto.put("priorityID", priority.getPriorityID());
            priorityDto.put("priorityName", priority.getPriorityName());
            return new ResponseEntity<>(priorityDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllPriorities() {
        List<TaskPriority> priorities = priorityService.getAllPriorities();
        
        List<Map<String, Object>> priorityDtos = priorities.stream()
            .map(priority -> {
                Map<String, Object> dto = new HashMap<>();
                dto.put("priorityID", priority.getPriorityID());
                dto.put("priorityName", priority.getPriorityName());
                return dto;
            })
            .collect(Collectors.toList());
        
        return new ResponseEntity<>(priorityDtos, HttpStatus.OK);
    }
}