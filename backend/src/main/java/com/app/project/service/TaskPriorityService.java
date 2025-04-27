package com.app.project.service;

import com.app.project.model.TaskPriority;
import java.util.List;

public interface TaskPriorityService {
    TaskPriority getPriorityById(long id);
    List<TaskPriority> getAllPriorities();
}
