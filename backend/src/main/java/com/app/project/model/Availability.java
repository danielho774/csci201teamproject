package com.app.project.model;

import jakarta.persistence.*;
import java.time.LocalTime; 

@Entity
@Table(name = "Availability") 
public class Availability {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "avail_id")
    private int availID; 

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) 
    private User user; 

    @ManyToOne 
    @JoinColumn(name = "project_id", nullable = false) 
    private Project project; 

    @Column(name = "day", nullable = false) 
    private String day; 

    @Column(name = "start_time", nullable = false) 
    private LocalTime startTime; 

    @Column(name = "end_time", nullable = false) 
    private LocalTime endTime; 

    public Availability(User user, Project project, String day, LocalTime startTime, LocalTime endTime) { 
        this.user = user; 
        this.project = project; 
        this.day = day; 
        this.startTime = startTime; 
        this.endTime = endTime; 
    }

    //empty constructor for JPA
    public Availability() {}

    // Getters and Setters 
    public int getAvailID() {
        return availID; 
    }
    public void setAvailID(int availID) {
        this.availID = availID; 
    }

    public User getUser() {
        return user; 
    }
    public void setUser(User user) {
        this.user = user; 
    }

    public String getDay() {
        return day; 
    }
    public void setDay(String day) {
        this.day = day; 
    }

    public LocalTime getStartTime() {
        return startTime; 
    }
    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime; 
    }

    public LocalTime getEndTime() {
        return endTime; 
    }
    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime; 
    }
}