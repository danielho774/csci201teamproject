package com.app.project.service.impl;

import com.app.project.model.Priority;
import com.app.project.repository.PriorityRepository;
import com.app.project.service.PriorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PriorityServiceImpl implements PriorityService {

    @Autowired
    private PriorityRepository PriorityRepository;

    @Override
    public Priority getPriorityById(long id) {
        return PriorityRepository.findById((int) id).orElseThrow(
                () -> new RuntimeException("Priority not found with id: " + id)
        );
    }

    @Override
    public List<Priority> getAllPriorities() {
        return PriorityRepository.findAll();
    }
}