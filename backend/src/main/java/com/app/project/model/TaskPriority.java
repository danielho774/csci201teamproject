package com.app.project.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity // Add @Entity annotation
@Table(name = "Priority") // Map to the "Priority" table in schema.sql
public class TaskPriority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "priority_id") // Maps to priority_id column
    private int priorityID;

    @Column(name = "priority_name", nullable = false, length = 25) // Maps to priority_name column
    private String priorityName;

    // One Priority can be applied to many Tasks
    // 'mappedBy = "priority"' refers to the 'priority' field in the Task entity
    @OneToMany(mappedBy = "priority", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> tasks = new ArrayList<>();

    // JPA requires a no-arg constructor
    public TaskPriority() {
    }

    // Optional: Constructor with fields
    public TaskPriority(String priorityName) {
        this.priorityName = priorityName;
    }

    // --- Getters and Setters ---

    public int getPriorityID() {
        return priorityID;
    }

    public void setPriorityID(int priorityID) {
        this.priorityID = priorityID;
    }

    public String getPriorityName() {
        return priorityName;
    }

    public void setPriorityName(String priorityName) {
        this.priorityName = priorityName;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    // Optional: toString, equals, hashCode methods
}