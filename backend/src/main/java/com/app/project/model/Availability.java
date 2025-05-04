package com.app.project.model;

import jakarta.persistence.*;

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

    @Column(name = "date", nullable = false) 
    private String date; 

    // "HH:mm:ss" format
    @Column(name = "start_time", nullable = false) 
    private String startTime; 

    // "HH:mm:ss" format
    @Column(name = "end_time", nullable = false) 
    private String endTime; 

    public Availability(User user, Project project, String date, String startTime, String endTime) { 
        this.user = user; 
        this.project = project; 
        this.date = date; 
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

    public int getUserID() {
        return user.getUserID(); 
    }
    public void setUser(User user) {
        this.user = user; 
    }

    public int getProjectID() {
        return project.getProjectID(); 
    }
    public void setProject(Project project) {
        this.project = project; 
    }

    public String getDate() {
        return date; 
    }
    public void setDay(String date) {
        this.date = date; 
    }

    public String getStartTime() {
        return startTime; 
    }
    public void setStartTime(String startTime) {
        this.startTime = startTime; 
    }

    public String getEndTime() {
        return endTime; 
    }
    public void setEndTime(String endTime) {
        this.endTime = endTime; 
    }
}