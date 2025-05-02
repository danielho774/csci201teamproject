package com.app.project.service.impl;

import com.app.project.model.Project;
import com.app.project.model.ProjectMember;
import com.app.project.model.Task;
import com.app.project.model.User;
import com.app.project.repository.ProjectMemberRepository;
import com.app.project.repository.ProjectRepository;
import com.app.project.repository.StatusRepository;
import com.app.project.repository.UserRepository;
import com.app.project.service.ProjectMemberService;
import com.app.project.service.ProjectService;
import com.app.project.service.TaskStatusService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.io.ObjectInputFilter.Status;
import java.util.List;


@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectMemberRepository projectMemberRepository;

    @Autowired
    private UserRepository userRepository;

    public Project getProjectById(int projectID) {
        return projectRepository.findById(projectID)
                .orElseThrow(() -> new RuntimeException("Project not found"));
    }

    //assuming that taskStatus is set to complete when task is complete
    public double updateProjectProgress(int projectID) {
        Project project = getProjectById(projectID);

        if (project.getTasks().isEmpty()) {
            return 0.0;
        }

        int completedTasks = 0;

        for (Task task : project.getTasks()) {
            if (task.getStatus().getStatusName() == "Completed") { 
                completedTasks++;
            }
        }

        project.setProgress(completedTasks / (double) project.getTasks().size() * 100.0);
        projectRepository.save(project);

        return project.getProgress();
    }

    //creates a new project and project member for the owner
    //returns the saved project
    public Project createProject(
        int userID,
        String name,
        String description,
        String end_date,
        String start_date) {

        // find user by userID
        User owner = userRepository.findById(userID).orElseThrow(() -> new RuntimeException("User not found"));

        Project project = new Project(name, description, end_date, start_date, owner);
        System.out.println("Project created with name: " + name);
        System.out.println("Project created with description: " + description);
        System.out.println("Project created with end date: " + end_date);
        System.out.println("Project created with start date: " + start_date);
        System.out.println("Project created with owner: " + owner.getUsername());
            
        Project savedProject = saveProject(project);

        // create project member object to store the relationship between project and owner
        ProjectMember projectMember = new ProjectMember(savedProject, owner, true);

        // save project member in database
        projectMemberRepository.save(projectMember);

        return savedProject;
    }
    
    public Optional<Project> getProject(int projectID) {
        return projectRepository.findById(projectID);
    }

    public Project saveProject(Project project) {
        return projectRepository.save(project);
    }

    public void deleteProject(int projectID) {
        projectRepository.deleteById(projectID);
    }

    public void addMember(Project project, User user) {
        ProjectMember member = new ProjectMember(project,user, false);
        projectMemberRepository.save(member);
    }

    public void removeMember(ProjectMember member) {
        projectMemberRepository.delete(member);
    }

    public List<ProjectMember> getProjectMembers(int projectID) {
        return projectMemberRepository.findByProject_ProjectID(projectID);
    }

    public void transferOwnership(int projectID, int newOwnerUserID) {
        Project project = projectRepository.findById(projectID).orElseThrow();// get project

        User newOwner = userRepository.findById(newOwnerUserID).orElseThrow();// get new owner

        int currentOwnerID = project.getOwner().getUserID();// get current owner

        //create a project member object to store the relationship between project and new owner if it doesn't exist
        if (projectMemberRepository.findByUserUserIDAndProjectProjectID(newOwner.getUserID(), projectID) == null) {
            ProjectMember projectMember = new ProjectMember(project, newOwner, true);
            projectMemberRepository.save(projectMember);
        } else {
            ProjectMember projectMember = projectMemberRepository.findByUserUserIDAndProjectProjectID(newOwner.getUserID(), projectID).orElse(null);
            projectMember.setRole(true);
            projectMemberRepository.save(projectMember);
        }

        //update the project member object to store the relationship between project and current owner
        ProjectMember currentOwnerProjectMember = projectMemberRepository.findByUserUserIDAndProjectProjectID(currentOwnerID, projectID).orElse(null);
        currentOwnerProjectMember.setRole(false);
        projectMemberRepository.save(currentOwnerProjectMember);

        //update the project owner
        project.setOwner(newOwner);
        projectRepository.save(project);
    }
  
}
