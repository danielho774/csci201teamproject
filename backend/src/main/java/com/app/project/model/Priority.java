package com.app.project.model;

import jakarta.persistence.*;

public class Priority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "priority_id")
    private int priorityID;

    @Column(name = "priority_name", nullable = false)
    private String priorityName;
}

