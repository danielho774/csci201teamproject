package com.app.project.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "Tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private int taskID; 

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false) 
    private Project project; 

    @Column(name = "task_name", length = 25, nullable = false) 
    private String taskName; 

    @Column(name = "task_descrip", length = 150)
    private String taskDescrip; 

    @ManyToOne
    @JoinColumn(name = "status_type")
    private TaskStatus status; 

    @Column(name = "start_date")
    private LocalDate startDate; 

    @Column(name = "end_date")
    private LocalDate endDate; 

    @Column(name = "duration")
    private int duration; 

    @Column(name = "assigned")
    private boolean assigned;

    // Constructor, getters, and setters
    
    public Task() {
        // Default constructor
    }
    
    public Task(Project project, String taskName, String taskDescrip, 
                TaskStatus status, LocalDate startDate, 
                LocalDate endDate, int duration, boolean assigned) {
        this.project = project; 
        this.taskName = taskName; 
        this.taskDescrip = taskDescrip; 
        this.status = status; 
        this.startDate = startDate; 
        this.endDate = endDate; 
        this.duration = duration; 
        this.assigned = assigned; 
    }
    
    // Required methods
    public boolean updateStatus(TaskStatus status) {
        this.status = status;
        return true;
    }
    
    // Getters and Setters
    public int getTaskID() {
        return taskID;
    }
    
    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }
    
    public Project getProject() {
        return project;
    }
    
    public void setProject(Project project) {
        this.project = project;
    }
    
    public String getTaskName() {
        return taskName;
    }
    
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }
    
    public String getTaskDescrip() {
        return taskDescrip;
    }
    
    public void setTaskDescrip(String taskDescrip) {
        this.taskDescrip = taskDescrip;
    }
    
    public TaskStatus getStatus() {
        return status;
    }
    
    public void setStatus(TaskStatus status) {
        this.status = status;
    }
    
    public LocalDate getStartDate() {
        return startDate;
    }
    
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
    
    public LocalDate getEndDate() {
        return endDate;
    }
    
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
    
    public int getDuration() {
        return duration;
    }
    
    public void setDuration(int duration) {
        this.duration = duration;
    }
    
    public boolean isAssigned() {
        return assigned;
    }
    
    public void setAssigned(boolean assigned) {
        this.assigned = assigned;
    }
}