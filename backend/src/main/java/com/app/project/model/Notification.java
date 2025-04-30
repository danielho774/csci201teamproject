package com.app.project.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Notification")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notif_id")
    private int notifID;

    @Column(name = "notif_type", nullable = false)
    private String notifType;
}

