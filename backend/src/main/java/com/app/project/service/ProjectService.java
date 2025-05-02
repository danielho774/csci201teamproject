package com.app.project.service;

import com.app.project.model.Project;
import com.app.project.model.ProjectMember;
import com.app.project.model.User;
import org.springframework.stereotype.Service;
import java.util.Optional;

import java.util.List;

@Service
public interface ProjectService {
    /*public void addMember(Long projectId, String userEmail);
    public void leaveProject(Long projectId, String userEmail);
    public void deleteProject(Long projectId, String ownerEmail);
    public void reassignOwner(Long projectId, String currentOwnerEmail, String newOwnerEmail);
    public void ownerLeaving(Long projectId, String currentOwnerEmail, String newOwnerEmail);*/

    public Project getProjectById(int projectID);
    public double updateProjectProgress(int projectID);
    // public Project createProject(Project project, int memberID);

    public Optional<Project> getProject(int projectID);
    public Project saveProject(Project project);
    public void deleteProject(int projectID);
    public void addMember(Project project, User user);
    public void removeMember(ProjectMember member);
    public List<ProjectMember> getProjectMembers(int projectID);
    public void transferOwnership(int projectID, int newOwnerMemberID);
  
}

