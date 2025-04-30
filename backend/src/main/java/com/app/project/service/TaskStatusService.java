package com.app.project.service;

import com.app.project.model.TaskStatus;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface TaskStatusService {
    TaskStatus getStatusById(long id);
    List<TaskStatus> getAllStatus();
}
