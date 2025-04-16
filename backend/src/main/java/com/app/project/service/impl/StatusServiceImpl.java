package com.app.project.service.impl;

import com.app.project.model.Status;
import com.app.project.repository.StatusRepository;
import com.app.project.service.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatusServiceImpl implements StatusService {

    @Autowired
    private StatusRepository StatusRepository;

    @Override
    public Status getStatusById(long id) {
        return StatusRepository.findById((int) id).orElseThrow(
                () -> new RuntimeException("Status not found with id: " + id)
        );
    }

    @Override
    public List<Status> getAllStatus() {
        return StatusRepository.findAll();
    }
}