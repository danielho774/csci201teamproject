package com.app.project.service;

import com.app.project.model.Project;
import com.app.project.model.ProjectMember;
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
    private ProjectRepository projectRepo;

    @Autowired
    private UserRepository userRepo;

    
    public void addMember(Long projectId, String userEmail){}

    public void leaveProject(Long projectId, String userEmail) {}
    

    public void deleteProject(Long projectId, String ownerEmail) {}
       
    public void reassignOwner(Long projectId, String currentOwnerEmail, String newOwnerEmail){}

    public void ownerLeaving(Long projectId, String currentOwnerEmail, String newOwnerEmail){}
}

