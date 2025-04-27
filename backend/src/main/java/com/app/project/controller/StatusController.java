package com.app.project.controller;

import com.app.project.model.TaskStatus;
import com.app.project.service.TaskStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/status")
public class StatusController {

    @Autowired
    private TaskStatusService statusService;

    @GetMapping("/{id}")
    public ResponseEntity<TaskStatus> getStatusById(@PathVariable long id) {
        TaskStatus status = statusService.getStatusById(id);
        if (status != null) {
            return new ResponseEntity<>(status, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<List<TaskStatus>> getAllStatus() {
        List<TaskStatus> status = statusService.getAllStatus();
        return new ResponseEntity<>(status, HttpStatus.OK);
    }
}
