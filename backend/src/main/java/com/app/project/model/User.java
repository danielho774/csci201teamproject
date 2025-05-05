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

	// for projects the user is a member of 
	// ManyToMany --> user can be a member of multiple projects 
	// 			  --> project can have multiple members 
	// Foreign key storage
	// joinColumns --> user 'owns' this table, tells JPA to 
	// 				store users that are associated with a Project
	// inverseJoinColumns --> the other entity of the table, tells 
	// 						JPA to store which Project is associated
	// 						with a User
	// @ManyToMany
	// @JoinTable(
	// 	name = "ProjectMembers", 
	// 	joinColumns = @JoinColumn(name = "user"), 
	// 	inverseJoinColumns = @JoinColumn(name = "project")
	// )
	// private List<Project> projects = new ArrayList<>(); 

	// one user can have multiple availabilities 
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL) 
	private List<Availability> availability = new ArrayList<>(); 

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<ProjectMember> memberships = new ArrayList<>(); 
	
	public User(int userID, String username, String email, boolean isGuest) {
		this.userID = userID; 
		this.username = username; 
		this.email = email; 
		this.isGuest = isGuest; 
	}

	// empty constructor for JPA
	public User() {}

	@Transient // Not stored in the database directly
    public List<Integer> getProjectIDs() {
        List<Integer> projects = new ArrayList<>();
        for (ProjectMember membership : memberships) {
            projects.add(membership.getProjectID());
        }
        return projects;
    }

	public boolean verify(String username, String email, String password) {
		if (this.username.equals(username) || this.email.equals(email)) {
			if (this.password.equals(password)) {
				return true; 
			}
		}
		return false;  
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

	// public List<Task> getAssignedTasks() {
	// 	List<Task> allTasks = new ArrayList<>(); 
	// 	List<Integer> projectIDs = getProjectIDs();
	// 	List<Project> projects = new ArrayList<>();
	// 	for (int i = 0; i < projectIDs.size(); i++) {
	// 		Project project = Project.getProjectByID(projectIDs.get(i)); 
	// 		if (project != null) {
	// 			projects.add(project); 
	// 		}
	// 	}
		
	// 	if (projects != null) {
	// 		for (int i = 0; i < projects.size(); i++) {
	// 			ProjectMember member = memberships.get(i); 
	// 			List<TaskAssignments> assignments = member.getAssignments();  
	// 			if (assignments != null) {
	// 				for (int j = 0; j < assignments.size(); j++) {
	// 					TaskAssignments assignment = assignments.get(j); 
	// 					if (assignment.getTask() != null && !allTasks.contains(assignment.getTask())) {
	// 						allTasks.add(assignment.getTask()); 
	// 					}
	// 				}
	// 			}
	// 		}
	// 	}

	// 	return allTasks; 
	// }
	
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
}

