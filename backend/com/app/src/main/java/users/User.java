package users;

import java.util.List;

import project; 
import tasks; 

public class User {
	private int userID; 
	private String username; 
	private String email; 
	private boolean isGuest; 
	
	public User(int userId, String username, String email, boolean isGuest) {
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

