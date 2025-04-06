-- CREATE DATABASE project201; 
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
    progress INT,
    FOREIGN KEY (owner_id) REFERENCES Users(user_id)
);

-- PROJECT MEMBERS  
CREATE TABLE ProjectMembers (
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
    day VARCHAR(10), 
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

-- PRIORITY 
CREATE TABLE Priority (
	priority_id INT AUTO_INCREMENT PRIMARY KEY, 
    priority_name VARCHAR(25)
); 
INSERT INTO Priority(priority_name) VALUES 
	('Low'), 
    ('Medium'), 
    ('High'); 

-- TASKS 
CREATE TABLE Tasks (
	task_id INT AUTO_INCREMENT PRIMARY KEY, 
    project_id INT, 
    task_name VARCHAR(25),
    task_descrip VARCHAR(150), 
    status_type INT, 
    priority INT, 
    start_date DATE, 
    end_date DATE, 
    duration INT, 
    assigned BOOLEAN, 
    FOREIGN KEY (project_id) REFERENCES Projects(project_id), 
    FOREIGN KEY (status_type) REFERENCES Status(status_id), 
    FOREIGN KEY (priority) REFERENCES Priority(priority_id)
);

-- TASK ASSIGNMENTS 
CREATE TABLE TaskAssignments (
	assignment_id INT AUTO_INCREMENT PRIMARY KEY, 
    task_id INT, 
    member_id INT, 
    FOREIGN KEY (task_id) REFERENCES Tasks(task_id), 
    FOREIGN KEY (member_id) REFERENCES ProjectMembers(member_id)
); 

-- REACTIONS 
CREATE TABLE Reactions (
	reaction_id INT AUTO_INCREMENT PRIMARY KEY, 
    reaction_type VARCHAR(25)
); 
INSERT INTO Reactions(reaction_type) VALUES 
    ('heart'), 
    ('thumbs_up'), 
    ('fire'), 
    ('laugh'), 
    ('thumbs_down'); 

-- COMMENTS 
CREATE TABLE Comments (
	comment_id INT AUTO_INCREMENT PRIMARY KEY,
    member_id INT, 
    comment VARCHAR(150), 
    date_created DATE, 
    archived BOOLEAN, 
    resolved BOOLEAN,
    reaction INT, 
    FOREIGN KEY (member_id) REFERENCES ProjectMembers(member_id), 
    FOREIGN KEY (reaction) REFERENCES Reactions(reaction_id)
); 

-- NOTIFICATION 
CREATE TABLE Notification (
	notif_id INT AUTO_INCREMENT PRIMARY KEY, 
    notif_type VARCHAR(150)
); 
INSERT INTO Notification (notif_type) VALUES 
	('Unseen'), 
    ('Pending'), 
    ('Resolved'); 

-- HISTORY 
CREATE TABLE History (
	change_id INT AUTO_INCREMENT PRIMARY KEY, 
    task_id INT, 
    member_id INT, 
    change_date DATE, 
    old_data VARCHAR(150), 
    new_data VARCHAR(150), 
    notif_status INT, 
    field_changed VARCHAR(50), 
    FOREIGN KEY (task_id) REFERENCES Tasks(task_id), 
    FOREIGN KEY (member_id) REFERENCES ProjectMembers(member_id), 
    FOREIGN KEY (notif_status) REFERENCES Notification(notif_id)
); 