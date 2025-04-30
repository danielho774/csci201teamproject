package com.app.project.service;

import com.app.project.model.TaskPriority;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface TaskPriorityService {
    TaskPriority getPriorityById(long id);
    List<TaskPriority> getAllPriorities();
}
