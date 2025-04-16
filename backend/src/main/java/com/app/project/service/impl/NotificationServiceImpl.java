package com.app.project.service.impl;

import com.app.project.model.Notification;
import com.app.project.repository.NotificationRepository;
import com.app.project.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public Notification getNotificationById(long id) {
        return notificationRepository.findById((int) id).orElseThrow(
                () -> new RuntimeException("Notification not found with id: " + id)
        );
    }

    @Override
    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }
}