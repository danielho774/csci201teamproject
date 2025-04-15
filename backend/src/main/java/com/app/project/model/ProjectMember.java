package com.app.project.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "ProjectMember")
public class ProjectMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private int memberID; 

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false) 
    private Project project; 

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; 

    @Column(name = "role")
	private boolean role;

    @ManyToMany(mappedBy = "assignees") 
    private List<Task> assignedTasks; 

    public ProjectMember(Project project, User user, boolean role) {
        this.project = project; 
        this.user = user; 
        this.role = role; 
    }

    public List<Task> getAssignedTasks() {
        return assignedTasks; 
    }

    // Getters and Setters 
    public int getMemberID() {
        return memberID; 
    }
    public void setMemberID(int memberID) {
        this. memberID = memberID; 
    }

    public Project getProject() {
        return project; 
    }
    public void setProject(Project project) {
        this.project = project; 
    }

    public User getUser() {
        return user; 
    }
    public void setUser(User user) {
        this.user = user; 
    }

    public boolean isRole() {
        return role; 
    }
    public void setRole(boolean role) {
        this.role = role; 
    }
}
