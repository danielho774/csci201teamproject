package com.app.project.controller;

import com.app.project.model.Status;
import com.app.project.service.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/status")
public class StatusController {

    @Autowired
    private StatusService statusService;

    @GetMapping("/{id}")
    public ResponseEntity<Status> getStatusById(@PathVariable long id) {
        Status status = statusService.getStatusById(id);
        if (status != null) {
            return new ResponseEntity<>(status, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<List<Status>> getAllStatus() {
        List<Status> status = statusService.getAllStatus();
        return new ResponseEntity<>(status, HttpStatus.OK);
    }
}
