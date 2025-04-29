package com.app.project.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity // Add @Entity annotation
@Table(name = "Status") // Map to the "Status" table in schema.sql
public class TaskStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "status_id") // Maps to status_id column
    private int statusID;

    @Column(name = "status_name", nullable = false, length = 25) // Maps to status_name column
    private String statusName;

    // One Status can be applied to many Tasks
    // 'mappedBy = "status"' refers to the 'status' field in the Task entity
    @OneToMany(mappedBy = "status", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> tasks = new ArrayList<>();

    // JPA requires a no-arg constructor
    public TaskStatus() {
    }

    // Optional: Constructor with fields
    public TaskStatus(String statusName) {
        this.statusName = statusName;
    }

    // --- Getters and Setters ---

    public int getStatusID() {
        return statusID;
    }

    public void setStatusID(int statusID) {
        this.statusID = statusID;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    // Optional: toString, equals, hashCode methods
}