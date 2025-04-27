package com.app.project.service.impl;

import com.app.project.model.TaskPriority;
import com.app.project.repository.PriorityRepository;
import com.app.project.service.TaskPriorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PriorityServiceImpl implements TaskPriorityService {

    @Autowired
    private PriorityRepository PriorityRepository;

    @Override
    public TaskPriority getPriorityById(long id) {
        return PriorityRepository.findById((int) id).orElseThrow(
                () -> new RuntimeException("Priority not found with id: " + id)
        );
    }

    @Override
    public List<TaskPriority> getAllPriorities() {
        return PriorityRepository.findAll();
    }
}