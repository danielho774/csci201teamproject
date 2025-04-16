package com.app.project.model;

import jakarta.persistence.*;

@Entity
@Table(name = "TaskAssignments")
public class TaskAssignments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "assignment_id")
    private int assignmentID;

    @ManyToOne 
    @JoinColumn(name = "task_id", nullable = false)
    private Task task; 

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false) 
    private ProjectMember member; 

    public TaskAssignments(Task task, ProjectMember member) {
        this.task = task; 
        this.member = member; 
    }

    // Getters and Setters 
    public int getAssignmentID() {
        return assignmentID; 
    }
    public void setAssignmentID(int assignmentID) {
        this.assignmentID = assignmentID; 
    }

    public Task getTask() {
        return task; 
    }
    public void setTask(Task task) {
        this.task = task; 
    }

    public ProjectMember getMember() {
        return member; 
    }
    public void setMember(ProjectMember member) {
        this.member = member; 
    }
}
