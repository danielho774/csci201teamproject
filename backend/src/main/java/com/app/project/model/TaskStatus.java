package com.app.project.model;

import java.util.List;

import jakarta.persistence.*;

public class TaskStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "status_id")
    private int statusID;

    //connect to task table
    @OneToMany(mappedBy = "task")
    private List<Task> tasks;

    @Column(name = "status_name", nullable = false)
    private String statusName;
}

