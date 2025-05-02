package com.app.project.service;

import com.app.project.model.Project;
import com.app.project.model.ProjectMember;
import com.app.project.model.User;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ProjectMemberService {

    public ProjectMember getMember(int memberID);
    
    public List<ProjectMember> getAllMembers(int projectID);
    
    public ProjectMember getMemberByUserIDProjectID(int userID, int projectID);

    public void leaveProject(int memberID);

    public void ownerLeaveAndTransfer(int ownerMemberID, int newOwnerMemberID);

    public void ownerDeleteProject(int ownerMemberID);

    public ProjectMember createProjectMember(Project project, User user, boolean isOwner);

}
