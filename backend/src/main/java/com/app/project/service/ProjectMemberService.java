package com.app.project.service;

import com.app.project.model.ProjectMember;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public interface ProjectMemberService {

    public Optional<ProjectMember> getMember(int memberID);

    public void leaveProject(int memberID);

    public void ownerLeaveAndTransfer(int ownerMemberID, int newOwnerMemberID);

    public void ownerDeleteProject(int ownerMemberID);

    public int createProjectMember(int projectID, int userID, boolean isOwner);

}
