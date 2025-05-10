package com.app.project.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "Projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private int projectID;

    @Column(name = "project_name", nullable = false)
    private String projectName;

    @Column(name = "project_descrip")
    private String projectDescription;

    @Column(name = "end_date", nullable = false)
    private String end_date;

    @Column(name = "start_date", nullable = false)
    private String start_date;

    @Column(name = "progress")
    private double progress;

    @Column(name = "share_code", nullable = false, unique = true)
    private String shareCode;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @JsonIgnore
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> tasks = new ArrayList<>();

    //Delete project members when project is deleted so delete fuctionality works
    @JsonIgnore
    @OneToMany(mappedBy = "project", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<ProjectMember> members;

    @JsonIgnore
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Availability> availabilities = new ArrayList<>();

    public Project(){}
    public Project(String name, String description, String end_date, String start_date, User owner, String shareCode) {
        this.projectName = name;
        this.projectDescription = description;
        this.start_date = start_date;
        this.end_date = end_date;
        this.progress = 0.0;
        this.owner = owner;
        this.shareCode = shareCode;
    }
    // Getters and Setters
    
    public int getProjectID() {
        return projectID;
    }

    public void setProjectID(int projectID) {
        this.projectID = projectID;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String name) {
        this.projectName = name;
    }

    public String getEndDate() {
        return end_date;
    }

    public void setEndDate(String end_date) {
        this.end_date = end_date;
    }

    public String getStartDate() {
        return start_date;
    }

    public void setStartDate(String start_date) {
        this.start_date = start_date;
    }
    
    public void setProgress(double progress) {
        this.progress = progress;
    }
    public double getProgress() {
        return this.progress;
    }
    

    public String getProjectDescription() {
        return projectDescription;
    }
    public void setProjectDescription(String description) {
        this.projectDescription = description;
    }

    public User getOwner() {
        return owner;
    }

    public int getOwnerID() {
        return owner != null ? owner.getUserID() : 0;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public List<Task> getTasks() {
        return tasks;
    }
    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
    public void addTask(Task task) {
        tasks.add(task);
        task.setProject(this);
    }
    public void removeTask(Task task) {
        tasks.remove(task);
        task.setProject(null);
    }

    // public List<Integer> getMemberIDs() {
    //     return members.stream()
    //                   .map(ProjectMember::getMemberID)
    //                   .toList();
    // }

    public List<ProjectMember> getMembers() {
        return members; 
    }

    public void setMembers(List<ProjectMember> members) {
        this.members = members;
    }

    public List<Availability> getAvailabilities() {
        return availabilities;
    }
    public void setAvailabilities(List<Availability> availabilities) {
        this.availabilities = availabilities;
    }

    public String getShareCode() {
        return shareCode;
    }
    public void setShareCode(String shareCode) {
        this.shareCode = shareCode;
    }
}