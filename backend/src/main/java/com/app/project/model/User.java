package com.app.project.model;
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

	@Column(name = "username", length = 25, nullable = false, unique = true)
	private String username; 

	@Column(name = "email", length = 50, nullable = false, unique = true)
	private String email; 

	@Column(name = "password", length = 50, nullable = false)
	private String password; 

	@Column(name = "first_name", length = 25, nullable = false)
	private String firstName; 

	@Column(name = "last_name", length = 25, nullable = false) 
	private String lastName; 

	@Column(name = "is_guest")
	private boolean isGuest; 

	private boolean loggedIn; 

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

	// one user can have multiple availabilities 
	@OneToMany(mappedBy = "user_id", cascade = CascadeType.ALL) 
	private List<Availability> availability = new ArrayList<>(); 

	@ManyToMany 
	@JoinTable(
		name = "TaskAssignments", 
		joinColumns = @JoinColumn(name = "member_id"), 
		inverseJoinColumns = @JoinColumn(name = "task_id")
	) 
	private List<Task> assignedTasks; 
	
	public User(int userID, String username, String email, boolean isGuest) {
		this.userID = userID; 
		this.username = username; 
		this.email = email; 
		this.isGuest = isGuest; 
	}

	public boolean login(String username, String email, String password) {
		if (this.username.equals(username) || this.email.equals(email)) {
			if (this.password.equals(password)) {
				this.loggedIn = true; 
				return true; 
			}
		}
		
		this.loggedIn = false; 
		return false;  
	}

	public void logout() {
		this.loggedIn = false; 
	}

	// returns a combined list of owned and member of projects
	public List<Project> getProjects() {
		List<Project> allProjects = new ArrayList<>(ownedProjects); 
		for (int i = 0; i < memberOfProjects.size(); i++) {
			if (!allProjects.contains(memberOfProjects.get(i))) {
				allProjects.add(memberOfProjects.get(i)); 
			} 
		}
		 
		return allProjects; 
	}

	public boolean addAvailability(Availability newAvail) {
		if (availability.contains(newAvail)) {
			return false; 
		}

		newAvail.setUser(this); 
		availability.add(newAvail); 
		return true; 
	}

	public boolean removeAvailability(Availability removeAvail) {
		if (availability.remove(removeAvail)) {
			removeAvail.setUser(null); 
			return true; 
		}

		return false; 
	}

	public List<Task> getAssignedTasks() {
		List<Task> allTasks = new ArrayList<>(); 
		
		
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

	public String getPassword() {
		return password; 
	}
	public void setPassword(String password) {
		this.password = password; 
	}

	public String getFirstName() {
		return firstName; 
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName; 
	}

	public String getLastName() {
		return lastName; 
	}
	public void setLastName(String lastName) {
		this.lastName = lastName; 
	}
	
	public boolean isGuest() {
		return isGuest; 
	}
	public void setGuest(boolean isGuest) {
		this.isGuest = isGuest;
	}

	public List<Availability> getAvailabilities() {
		return availability; 
	}
	public void setAvailability(List<Availability> availability) {
		this.availability = availability; 
	}

	public boolean isLoggedIn() {
		return loggedIn; 
	}
}

