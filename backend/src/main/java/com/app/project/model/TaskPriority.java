package com.app.project.model;

import java.util.List;

import jakarta.persistence.*;

public class TaskPriority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "priority_id")
    private int priorityID;

    //connect to task table
    @OneToMany(mappedBy = "priority")
    private List<Task> tasks;

    @Column(name = "priority_name", nullable = false)
    private String priorityName;
}

