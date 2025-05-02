package com.app.project.controller;

import com.app.project.model.TaskStatus;
import com.app.project.service.TaskStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/status")
public class StatusController {

    @Autowired
    private TaskStatusService statusService;

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getStatusById(@PathVariable long id) {
        TaskStatus status = statusService.getStatusById(id);
        if (status != null) {
            Map<String, Object> statusDto = new HashMap<>();
            statusDto.put("statusID", status.getStatusID());
            statusDto.put("statusName", status.getStatusName());
            return new ResponseEntity<>(statusDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllStatus() {
        List<TaskStatus> statuses = statusService.getAllStatus();
        
        List<Map<String, Object>> statusDtos = statuses.stream()
            .map(status -> {
                Map<String, Object> dto = new HashMap<>();
                dto.put("statusID", status.getStatusID());
                dto.put("statusName", status.getStatusName());
                return dto;
            })
            .collect(Collectors.toList());
        
        return new ResponseEntity<>(statusDtos, HttpStatus.OK);
    }
}
