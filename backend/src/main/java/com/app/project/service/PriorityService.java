package com.app.project.service;

import com.app.project.model.Priority;
import java.util.List;

public interface PriorityService {
    Priority getPriorityById(long id);
    List<Priority> getAllPriorities();
}
