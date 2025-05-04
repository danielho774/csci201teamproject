package com.app.project.service.impl;

import com.app.project.model.Project;
import com.app.project.model.ProjectMember;
import com.app.project.model.Task;
import com.app.project.model.User;
import com.app.project.repository.ProjectMemberRepository;
import com.app.project.repository.ProjectRepository;
import com.app.project.repository.UserRepository;
import com.app.project.service.ProjectService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.List;


@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectMemberRepository projectMemberRepository;

    @Autowired
    private UserRepository userRepository;

    //assuming that taskStatus is set to complete when task is complete
    public double updateProjectProgress(int projectID) {
        Project project = getProjectByID(projectID);

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
        String start_date,
        String shareCode) {

        // find user by userID
        User owner = userRepository.findById(userID).orElseThrow(() -> new RuntimeException("User not found"));

        Project project = new Project(name, description, end_date, start_date, owner, shareCode);
            
        Project savedProject = saveProject(project);

        // create project member object to store the relationship between project and owner
        ProjectMember projectMember = new ProjectMember(savedProject, owner, true);

        // save project member in database
        projectMemberRepository.save(projectMember);

        return savedProject;
    }
    
    public Project getProjectByID(int projectID) {
        Optional<Project> project = projectRepository.findById(projectID);
        if (project.isPresent()) {
            return project.get();
        } else {
            return null;
        }
    }

    public Project saveProject(Project project) {
        return projectRepository.save(project);
    }

    public void deleteProjectByID(int projectID) {
        projectRepository.deleteById(projectID);
    }

    public void removeMember(ProjectMember member) {
        projectMemberRepository.deleteById(member.getMemberID());
    }

    public List<ProjectMember> getProjectMembers(int projectID) {
        return projectMemberRepository.findByProject_ProjectID(projectID);
    }

    public void transferOwnership(int projectID, int newOwnerUserID) {
        Project project = projectRepository.findById(projectID).orElseThrow(
            () -> new RuntimeException("Project not found with ID: " + projectID)
        );

        User newOwner = userRepository.findById(newOwnerUserID).orElseThrow(
            () -> new RuntimeException("User not found with ID: " + newOwnerUserID)
        );

        int currentOwnerID = project.getOwner().getUserID();

        //Check if the new owner is already a member
        Optional<ProjectMember> newOwnerMember = projectMemberRepository
            .findByUserUserIDAndProjectProjectID(newOwnerUserID, projectID);
            
        if (newOwnerMember.isEmpty()) {
            // Not a member yet, create new membership
            ProjectMember projectMember = new ProjectMember(project, newOwner, true);
            projectMemberRepository.save(projectMember);
        } else {
            // Already a member, update role
            ProjectMember projectMember = newOwnerMember.get();
            projectMember.setRole(true);
            projectMemberRepository.save(projectMember);
        }

        // Update the current owner's role
        Optional<ProjectMember> currentOwnerMember = projectMemberRepository
            .findByUserUserIDAndProjectProjectID(currentOwnerID, projectID);
        
        if (currentOwnerMember.isPresent()) {
            ProjectMember currentOwnerProjectMember = currentOwnerMember.get();
            currentOwnerProjectMember.setRole(false);
            projectMemberRepository.save(currentOwnerProjectMember);
        }

        // Update the project owner
        project.setOwner(newOwner);
        projectRepository.save(project);
    }
  
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Project getProjectByShareCode(String shareCode) {
        Project project = projectRepository.findByShareCode(shareCode);
        if (project != null) {
            return project;
        } else {
            return null;
        }
    }
}
