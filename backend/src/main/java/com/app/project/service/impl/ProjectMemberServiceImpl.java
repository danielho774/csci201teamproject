package com.app.project.service.impl;

import com.app.project.model.Project;
import com.app.project.model.ProjectMember;
import com.app.project.model.User;
import com.app.project.repository.ProjectMemberRepository;
import com.app.project.repository.ProjectRepository;
import com.app.project.service.ProjectMemberService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.List;

@Service
public class ProjectMemberServiceImpl implements ProjectMemberService {

    @Autowired
    private ProjectMemberRepository projectMemberRepository;

    @Autowired
    private ProjectRepository projectRepository;

    public ProjectMember getMember(int memberID) {
        Optional<ProjectMember> member = projectMemberRepository.findById(memberID);
        if (member.isPresent()) {
            return member.get();
        } else {
            return null;
        }
    }

    public List<ProjectMember> getAllMembers(int projectID) {
        return projectMemberRepository.findByProject_ProjectID(projectID);
    }

    public ProjectMember getMemberByUserIDProjectID(int userID, int projectID) {
        Optional<ProjectMember> member = projectMemberRepository.findByUserUserIDAndProjectProjectID(userID, projectID);
        if (member.isPresent()) {
            return member.get();
        } else {
            return null;
        }
    }

    public void leaveProject(int userID, int projectID) {
        ProjectMember member = projectMemberRepository.findByUserUserIDAndProjectProjectID(userID, projectID)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        if (member.isRole()) {
            throw new RuntimeException("Owner cannot leave without transferring ownership or deleting the project.");
        } {
            projectMemberRepository.deleteByMemberID(member.getMemberID());
            System.out.println("Deleted member with ID: " + member.getMemberID());
        }
    }

    public void ownerLeaveAndTransfer(int ownerUserID, int newOwnerUserID, int projectID) {
        ProjectMember owner = projectMemberRepository.findByUserUserIDAndProjectProjectID(ownerUserID, projectID)
                .orElseThrow(() -> new RuntimeException("Owner not found"));
        
        if (!owner.isRole()) {
            throw new RuntimeException("Only the owner can transfer ownership.");
        }

        ProjectMember newOwner = projectMemberRepository.findByUserUserIDAndProjectProjectID(newOwnerUserID, projectID)
                .orElseThrow(() -> new RuntimeException("New owner not found"));

        // Remove owner role from current owner
        owner.setRole(false);
        projectMemberRepository.save(owner);

        // Assign owner role to new owner
        newOwner.setRole(true);
        projectMemberRepository.save(newOwner);

        // Now original owner can leave
        projectMemberRepository.deleteById(newOwner.getMemberID());
    }

    public void ownerDeleteProject(int ownerMemberID) {
        ProjectMember owner = projectMemberRepository.findById(ownerMemberID)
                .orElseThrow(() -> new RuntimeException("Owner not found"));

        if (!owner.isRole()) {
            throw new RuntimeException("Only the owner can delete the project.");
        }

        int projectID = owner.getProjectID();

        // Delete all members related to this project
        List<ProjectMember> members = projectMemberRepository.findByProject_ProjectID(projectID);
        projectMemberRepository.deleteAll(members);

        // Delete the project itself
        projectRepository.deleteById(projectID);
    }

    public ProjectMember createProjectMember(Project project, User user, boolean isOwner) {
        ProjectMember member = new ProjectMember(project, user, isOwner);
        ProjectMember savedMember = projectMemberRepository.save(member);
        return savedMember;
    }
    

}
