package com.app.project.service;

import com.app.project.model.Notification;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface NotificationService {
    Notification getNotificationById(long id);
    List<Notification> getAllNotifications();
}
