CREATE DATABASE IF NOT EXISTS project201; 
USE project201; 

-- USERS 
CREATE TABLE Users (
	user_id INT AUTO_INCREMENT PRIMARY KEY, 
    	username VARCHAR(25), 
    	email VARCHAR(50), 
    	password VARCHAR(50), 
    	first_name VARCHAR(25), 
    	last_name VARCHAR(25), 
    	is_guest BOOLEAN
); 

-- PROJECTS
CREATE TABLE Projects (
	project_id INT AUTO_INCREMENT PRIMARY KEY, 
    	project_name VARCHAR(25), 
    	project_descrip VARCHAR(150), 
    	owner_id INT, 
    	start_date DATE, 
    	end_date DATE, 
    	progress FLOAT,
		share_code VARCHAR(25) UNIQUE,
    	FOREIGN KEY (owner_id) REFERENCES Users(user_id)
);

-- PROJECT MEMBERS  
CREATE TABLE project_member (
	project_id INT, 
    	user_id INT, 
    	member_id INT AUTO_INCREMENT PRIMARY KEY, 
    	role BOOLEAN, 
    	FOREIGN KEY (project_id) REFERENCES Projects(project_id), 
    	FOREIGN KEY (user_id) REFERENCES Users(user_id)
); 

-- AVAILABILITY 
CREATE TABLE Availability (
	avail_id INT AUTO_INCREMENT PRIMARY KEY, 
    	user_id INT, 
    	project_id INT,
    	date DATE, 
    	start_time TIME, 
    	end_time TIME,
    	FOREIGN KEY (user_id) REFERENCES Users(user_id), 
    	FOREIGN KEY (project_id) REFERENCES Projects(project_id)
); 

-- STATUS 
CREATE TABLE Status (
	status_id INT AUTO_INCREMENT PRIMARY KEY, 
    	status_name VARCHAR(25)
); 
INSERT INTO Status(status_name) VALUES 
	('Incomplete'), 
    	('In Progress'), 
    	('Complete'); 


-- TASKS 
CREATE TABLE Tasks (
	task_id INT AUTO_INCREMENT PRIMARY KEY, 
    	project_id INT, 
    	task_name VARCHAR(25),
    	task_descrip VARCHAR(150), 
    	status_type INT, 
    	start_date DATE, 
    	end_date DATE, 
    	duration INT, 
    	assigned BOOLEAN, 
    	FOREIGN KEY (project_id) REFERENCES Projects(project_id), 
    	FOREIGN KEY (status_type) REFERENCES Status(status_id)
);

-- TASK ASSIGNMENTS 
CREATE TABLE TaskAssignments (
	assignment_id INT AUTO_INCREMENT PRIMARY KEY, 
    	task_id INT, 
    	member_id INT, 
    	FOREIGN KEY (task_id) REFERENCES Tasks(task_id), 
    	FOREIGN KEY (member_id) REFERENCES project_member(member_id)
); 