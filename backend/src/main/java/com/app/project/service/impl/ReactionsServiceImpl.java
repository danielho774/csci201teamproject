package com.app.project.service.impl;

import com.app.project.model.Reactions;
import com.app.project.repository.ReactionsRepository;
import com.app.project.service.ReactionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReactionsServiceImpl implements ReactionsService {

    @Autowired
    private ReactionsRepository ReactionsRepository;

    @Override
    public Reactions getReactionsById(long id) {
        return ReactionsRepository.findById((int) id).orElseThrow(
                () -> new RuntimeException("Reactions not found with id: " + id)
        );
    }

    @Override
    public List<Reactions> getAllReactions() {
        return ReactionsRepository.findAll();
    }
}