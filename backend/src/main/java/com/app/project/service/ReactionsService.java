package com.app.project.service;

import com.app.project.model.Reactions;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface ReactionsService {
    Reactions getReactionsById(long id);
    List<Reactions> getAllReactions();
}
