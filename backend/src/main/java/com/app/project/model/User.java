package com.app.project.model;

// import com.app.project.model.Project; 

import jakarta.persistence.*;

import java.util.List;
import java.util.ArrayList; 

@Entity
@Table(name = "Users")
public class User {
	@Id // PRIAMARY KEY
	@GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO-INCREMENT
	@Column(name = "user_id", nullable = false)
	private int userID; 

	@Column(name = "username", nullable = false, unique = true)
	private String username; 

	@Column(name = "email", nullable = false, unique = true)
	private String email; 

	@Column(name = "is_guest")
	private boolean isGuest; 

	// for projects the user owns 
	// OneToMany --> one user can have multiple projects they own
	// CascadeType.ALL --> if there are any updates in Project, pass changes
	// 					to the owner as well 
	@OneToMany(mappedBy = "owner_id", cascade = CascadeType.ALL)
	private List<Project> ownedProjects = new ArrayList<>(); 

	// for projects the user is a member of 
	// ManyToMany --> user can be a member of multiple projects 
	// 			  --> project can have multiple members 
	// Foreign key storage
	// joinColumns --> user 'owns' this table, tells JPA to 
	// 				store users that are associated with a Project
	// inverseJoinColumns --> the other entity of the table, tells 
	// 						JPA to store which Project is associated
	// 						with a User
	@ManyToMany
	@JoinTable(
		name = "ProjectMembers", 
		joinColumns = @JoinColumn(name = "user_id"), 
		inverseJoinColumns = @JoinColumn(name = "project_id")
	)
	private List<Project> memberOfProjects = new ArrayList<>(); 
	
	public User(int userID, String username, String email, boolean isGuest) {
		this.userID = userID; 
		this.username = username; 
		this.email = email; 
		this.isGuest = isGuest; 
	}

	public boolean login(String username, String password) {

		
		return false; 
	}

	public void logout() {
		
	}

	public List<Project> getProjects() {
		List<Project> allProjects = new ArrayList<>(ownedProjects); 
		
		return null; 
	}

	public boolean updateAvailability() {
		
		
		return false; 
	}

	public List<Task> getAssignedTasks() {
		
		
		return null; 
	}
	
//	Getters and Setters 
	public int getUserID() {
		return userID; 
	}
	public void setUserID(int userID) {
		this.userID = userID; 
	}
	
	public String getUsername() {
		return username; 
	}
	public void setUsername(String username) {
		this.username = username; 
	}
	
	public String getEmail() {
		return email; 
	}
	public void setEmail(String email) {
		this.email = email; 
	}
	
	public boolean isGuest() {
		return isGuest; 
	}
	public void setGuest(boolean isGuest) {
		this.isGuest = isGuest;
	}
}

