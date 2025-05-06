package com.app.project.controller;

import com.app.project.model.Project;
import com.app.project.model.ProjectMember;
import com.app.project.model.User;
import com.app.project.repository.ProjectMemberRepository;
import com.app.project.service.ProjectMemberService;
import com.app.project.service.ProjectService;
import com.app.project.service.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/members")
public class ProjectMemberController {
    @Autowired
    private ProjectMemberService projectMemberService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ProjectMemberRepository projectMemberRepository;

    @GetMapping("/{memberID}")
    public ProjectMember getMember(@PathVariable int memberID) {
        return projectMemberService.getMember(memberID);
    }

    @GetMapping("/{userID}/getOwnership/{projectID}")
    public ResponseEntity<?> getOwnership(@PathVariable int userID, @PathVariable int projectID) {

        User user = userService.getUserByID(userID);
        if (user == null) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "User not found with ID: " + userID);
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
        // Check if project exists
        Project project = projectService.getProjectByID(projectID);
        if (project == null) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Project not found with ID: " + projectID);
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        // check that user is a member of the project
        ProjectMember projectMember = projectMemberService.getMemberByUserIDProjectID(userID,projectID);
        if(projectMember == null) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "User with ID: " + userID + " is not a member of Project with ID: " + projectID);
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        //
        Map<String, Boolean> successResponse = new HashMap<>();
        successResponse.put("role", projectMember.isRole());
        return new ResponseEntity<>(successResponse, HttpStatus.OK);
    }

    @GetMapping("/project/{projectID}")
    public List<ProjectMember> getAllMembers(@PathVariable int projectID) {
        return projectMemberService.getAllMembers(projectID);
    }

    @DeleteMapping("/{userID}/leave/{projectID}")
    public ResponseEntity<?> leaveProject(@PathVariable int userID, @PathVariable int projectID) {
        // Check if user exists
        User user = userService.getUserByID(userID);
        if (user == null) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "User not found with ID: " + userID);
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
        // Check if project exists
        Project project = projectService.getProjectByID(projectID);
        if (project == null) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Project not found with ID: " + projectID);
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        // check if user is a member of project
        ProjectMember projectMember = projectMemberService.getMemberByUserIDProjectID(userID, projectID);
        if(projectMember == null) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "User with ID: " + userID + " is not a member of Project with ID: " + projectID);
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        projectMemberService.leaveProject(userID, projectID);
        Map<String, String> successResponse = new HashMap<>();
        successResponse.put("message", "Member successfully removed");
        return new ResponseEntity<>(successResponse, HttpStatus.OK);
    }

    //owner leaving project
    @PostMapping("/{ownerUserID}/transferAndLeave/{projectID}")
    public void ownerLeaveAndTransfer(@PathVariable int ownerUserID, @RequestParam int newOwnerUserID, @PathVariable int projectID) {
        projectMemberService.ownerLeaveAndTransfer(ownerUserID, newOwnerUserID, projectID);
    }

    // Owner deletes project
    @DeleteMapping("/{ownerMemberID}/deleteProject")
    public void ownerDeleteProject(@PathVariable int ownerMemberID) {
        projectMemberService.ownerDeleteProject(ownerMemberID);
    }

    @GetMapping("/{memberID}/getUserIDByMemberID") 
    public ResponseEntity<?> getUserIDByMemberID(@PathVariable int memberID) {
        ProjectMember projectMember = projectMemberService.getMember(memberID); 

        if (projectMember == null) {
            Map<String, String> errorResponse = new HashMap<>(); 
            errorResponse.put("error", "Member not found with ID: " + memberID); 
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND); 
        }

        Map<String, Integer> response = new HashMap<>(); 
        response.put("userID", projectMember.getUserID()); 
        return new ResponseEntity<>(response, HttpStatus.OK); 
    }
}
