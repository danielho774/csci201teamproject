package com.app.project.service.impl;

import com.app.project.model.TaskStatus;
import com.app.project.repository.StatusRepository;
import com.app.project.service.TaskStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatusServiceImpl implements TaskStatusService {

    @Autowired
    private StatusRepository StatusRepository;

    @Override
    public TaskStatus getStatusById(long id) {
        return StatusRepository.findById((int) id).orElseThrow(
                () -> new RuntimeException("Status not found with id: " + id)
        );
    }

    @Override
    public List<TaskStatus> getAllStatus() {
        return StatusRepository.findAll();
    }
}