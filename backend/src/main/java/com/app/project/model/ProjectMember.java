package com.app.project.model;

import jakarta.persistence.*;

import java.util.List;
import java.util.ArrayList; 

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
    //can change to user if needed
    private User user; 

    @Column(name = "role")//owner is true, member is false
	private boolean role;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL) 
    private List<TaskAssignments> assignments = new ArrayList<>(); 

    public ProjectMember() {}

    public ProjectMember(Project project, User user, boolean role) {
        this.project = project; 
        this.user = user; 
        this.role = role; 
    }

    // Getters and Setters 
    //ids can be changed to user if needed or string if email
    public int getMemberID() {
        return memberID; 
    }
    public void setMemberID(int memberID) {
        this. memberID = memberID; 
    }

    public int getProjectID() {
        return project.getProjectID(); 
    }
    public void setProject(Project project) {
        this.project = project; 
    }

    public int getUserID() {
        return user.getUserID(); 
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

    public List<TaskAssignments> getAssignments() {
        return assignments; 
    }
    public void setAssignments(List<TaskAssignments> assignments) {
        this.assignments = assignments; 
    }
}
