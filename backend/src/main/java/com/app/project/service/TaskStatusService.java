package com.app.project.service;

import com.app.project.model.TaskStatus;
import java.util.List;

public interface TaskStatusService {
    TaskStatus getStatusById(long id);
    List<TaskStatus> getAllStatus();
}
