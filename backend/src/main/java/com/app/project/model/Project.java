package com.app.project.model;

import jakarta.persistence.*;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "Projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private int projectID;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "end_date", nullable = false)
    private String end_date;

    @Column(name = "start_data", nullable = false)
    private String start_date;

    @Column(name = "progress")
    private double progress;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private ProjectMember owner;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectMember> members = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> tasks = new ArrayList<>();

    public Project(){}
    public Project(String name, String description) {
        this.name = name;
        this.description = description;
    }
    // Getters and Setters
    
    public int getProjectID() {
        return projectID;
    }

    public void setProjectID(int projectID) {
        this.projectID = projectID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        if (tasks.isEmpty()) {
            return 0.0;
        }
        int completedTasks = 0;
        for(int i = 0; i < tasks.size(); i++) {
            if ((tasks.get(i).getStatus() != null) &&(tasks.get(i).getStatus().getStatusName().equalsIgnoreCase("complete"))) {
                completedTasks++;
            }
        }
        this.progress = (double) completedTasks / tasks.size() * 100.0;
        return this.progress;
    }
    

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public List<ProjectMember> getMembers() {
        return members;
    }
    public void setMembers(List<ProjectMember> members) {
        this.members = members;
    }
    public void addMember(ProjectMember member) {
        members.add(member);
        member.setProject(this);
    }
    public void removeMember(ProjectMember member) {
        members.remove(member);
        member.setProject(null);
    }

    public ProjectMember getOwner() {
        return owner;
    }

    public int getOwnerID() {
        return owner.getMemberID();
    }

    public void setOwner(ProjectMember owner) {
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
}