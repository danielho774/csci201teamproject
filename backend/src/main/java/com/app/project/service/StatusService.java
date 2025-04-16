package com.app.project.service;

import com.app.project.model.Status;
import java.util.List;

public interface StatusService {
    Status getStatusById(long id);
    List<Status> getAllStatus();
}
