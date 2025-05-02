package com.app.project.service.impl;

import com.app.project.model.Project;
import com.app.project.model.ProjectMember;
import com.app.project.model.Task;
import com.app.project.model.User;
import com.app.project.repository.ProjectMemberRepository;
import com.app.project.repository.ProjectRepository;
import com.app.project.repository.StatusRepository;
import com.app.project.repository.UserRepository;
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
    private TaskStatusService taskStatusService;

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

    // //can change to user if needed
    // public Project createProject(Project project, int memberID) {
    //     Project savedProject = projectRepository.save(project);
    //     ProjectMember owner = projectMemberRepository.findById(memberID).orElseThrow();
    //     owner.setProject(savedProject);
    //     owner.setRole(true); // Set as owner
    //     projectMemberRepository.save(owner);
    //     return savedProject;
    // }
    
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

    public void transferOwnership(int projectID, int newOwnerMemberID) {
        Project project = projectRepository.findById(projectID).orElseThrow();// get project

        ProjectMember previousOwner = projectMemberRepository.findById(project.getOwnerID()).orElseThrow();
        previousOwner.setRole(false); // set the current owner to member
        projectMemberRepository.save(previousOwner);

        ProjectMember newOwner = projectMemberRepository.findById(newOwnerMemberID).orElseThrow();// get new owner
        newOwner.setRole(true); // set the new owner to owner
        projectMemberRepository.save(newOwner);

        project.setOwner(previousOwner);
        projectRepository.save(project);
    }
  
}
