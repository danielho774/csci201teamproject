package com.app.project.service.impl;

import com.app.project.model.Project;
import com.app.project.model.ProjectMember;
import com.app.project.model.Task;
import com.app.project.model.User;
import com.app.project.repository.ProjectMemberRepository;
import com.app.project.repository.ProjectRepository;
import com.app.project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

import java.util.List;


@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectMemberRepository projectMemberRepository;

    public Project getProjectById(int projectID) {
        return projectRepository.findById(projectID)
                .orElseThrow(() -> new RuntimeException("Project not found"));
    }

    //change baised on task 
    /*public double calculateProjectProgress(int projectID) {
        Project project = getProjectById(projectID);

        if (project.getTasks().isEmpty()) {
            return 0.0;
        }

        long completedTasks = project.getTasks().stream()
                .filter(Task::isCompleted) 
                .count();

        return (double) completedTasks / project.getTasks().size() * 100.0;
    }*/

    //can change to user if needed
    public Project createProject(Project project, int memberID) {
        Project savedProject = projectRepository.save(project);
        ProjectMember owner = projectMemberRepository.findById(memberID).orElseThrow();
        owner.setProject(savedProject);
        owner.setRole(true); // Set as owner
        projectMemberRepository.save(owner);
        return savedProject;
    }

   

    
    public Optional<Project> getProject(int projectID) {
        return projectRepository.findById(projectID);
    }

    public void saveProject(Project project) {
        projectRepository.save(project);
    }

    public void deleteProject(int projectID) {
        projectRepository.deleteById(projectID);
    }

    public void addMember(Project project, int userID) {
        ProjectMember member = new ProjectMember(project,userID, false);
        projectMemberRepository.save(member);
    }

    public void removeMember(ProjectMember member) {
        projectMemberRepository.delete(member);
    }

    public List<ProjectMember> getProjectMembers(int projectID) {
        return projectMemberRepository.findByProject_ProjectID(projectID);
    }

    public void transferOwnership(int projectID, int newOwnerMemberID) {
        Project project = projectRepository.findById(projectID).orElseThrow();
        List<ProjectMember> members = projectMemberRepository.findByProject_ProjectID(projectID);
        for (ProjectMember m : members) {
            if (m.isRole()) {
                m.setRole(false);
                projectMemberRepository.save(m);
            }
        }
        ProjectMember newOwner = projectMemberRepository.findById(newOwnerMemberID).orElseThrow();
        newOwner.setRole(true);
        projectMemberRepository.save(newOwner);
    }
  
}
