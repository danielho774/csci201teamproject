package com.app.project.model;

import jakarta.persistence.*;

public class Status {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "status_id")
    private int statusID;

    @Column(name = "status_name", nullable = false)
    private String statusName;
}

