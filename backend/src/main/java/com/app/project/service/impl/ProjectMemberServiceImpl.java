package com.app.project.service.impl;

import com.app.project.model.Project;
import com.app.project.model.ProjectMember;
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

    public Optional<ProjectMember> getMember(int memberID) {
        return projectMemberRepository.findById(memberID);
    }

    public void leaveProject(int memberID) {
        ProjectMember member = projectMemberRepository.findById(memberID)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        if (member.isRole()) {
            throw new RuntimeException("Owner cannot leave without transferring ownership or deleting the project.");
        } else {
            projectMemberRepository.deleteById(memberID);
        }
    }

    public void ownerLeaveAndTransfer(int ownerMemberID, int newOwnerMemberID) {
        ProjectMember owner = projectMemberRepository.findById(ownerMemberID)
                .orElseThrow(() -> new RuntimeException("Owner not found"));
        
        if (!owner.isRole()) {
            throw new RuntimeException("Only the owner can transfer ownership.");
        }

        ProjectMember newOwner = projectMemberRepository.findById(newOwnerMemberID)
                .orElseThrow(() -> new RuntimeException("New owner not found"));

        // Remove owner role from current owner
        owner.setRole(false);
        projectMemberRepository.save(owner);

        // Assign owner role to new owner
        newOwner.setRole(true);
        projectMemberRepository.save(newOwner);

        // Now original owner can leave
        projectMemberRepository.deleteById(ownerMemberID);
    }

    public void ownerDeleteProject(int ownerMemberID) {
        ProjectMember owner = projectMemberRepository.findById(ownerMemberID)
                .orElseThrow(() -> new RuntimeException("Owner not found"));

        if (!owner.isRole()) {
            throw new RuntimeException("Only the owner can delete the project.");
        }

        Project project = owner.getProject();
        int projectID = project.getProjectID();

        // Delete all members related to this project
        List<ProjectMember> members = projectMemberRepository.findByProject_ProjectID(projectID);
        projectMemberRepository.deleteAll(members);

        // Delete the project itself
        projectRepository.deleteById(projectID);
    }
    

}
