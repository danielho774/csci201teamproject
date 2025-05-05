package com.app.project.controller;

import com.app.project.model.Project;
import com.app.project.service.ProjectMemberService;
import com.app.project.service.ProjectService;
import com.app.project.service.UserService;
import com.app.project.model.User;
import com.app.project.repository.ProjectMemberRepository;
import com.app.project.model.ProjectMember;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus; 

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectMemberService projectMemberService;

    @Autowired 
    private ProjectMemberRepository projectMemberRepository;

    @PostMapping("/createProject")
    public ResponseEntity<?> createProject( @RequestParam int userID, @RequestBody Project projectRequest) {

        String name = projectRequest.getProjectName();
        String description = projectRequest.getProjectDescription();
        String end_date = projectRequest.getEndDate();
        String start_date = projectRequest.getStartDate();
        String shareCode = projectRequest.getShareCode();

        // Validate the request parameters
        if (name == null || name.isEmpty() || description == null || description.isEmpty() ||
            end_date == null || end_date.isEmpty() || start_date == null || start_date.isEmpty() || 
            shareCode == null || shareCode.isEmpty()) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "All fields are required");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        // Check if user exists
        User owner = userService.getUserByID(userID);
        if (owner == null) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "User not found with ID: " + userID);
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        // Check if project with the same share code already exists
        Project existingProject = projectService.getProjectByShareCode(shareCode);
        if (existingProject != null) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Project with the same share code already exists");
            return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
        }

        // create project
        Project newProject = projectService.createProject(userID, name, description, end_date, start_date, shareCode);
        if (newProject == null) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to create project");
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return new ResponseEntity<>(newProject, HttpStatus.CREATED);
        }


    }

    @GetMapping("/{projectID}")
    public ResponseEntity<?> getProject(@PathVariable int projectID) {
        // Check if project exists
        Project project = projectService.getProjectByID(projectID);
        if (project == null) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Project not found with ID: " + projectID);
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
        // return project details
        return new ResponseEntity<>(project, HttpStatus.OK);
    }

    @GetMapping("/{projectID}/members")
    public List<ProjectMember> getProjectMembers(@PathVariable int projectID) {
        return projectService.getProjectMembers(projectID);
    }

    @PostMapping("/joinProject/{projectID}/user/{userID}")
    public ResponseEntity<?> joinProject(@PathVariable int projectID, @PathVariable int userID, @RequestParam String shareCode) {
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
        // Check if share code is valid
        if (!project.getShareCode().equals(shareCode)) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Invalid share code");
            return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
        }

        // Check if user is already a member of the project
        ProjectMember existingMember = projectMemberService.getMemberByUserIDProjectID(userID, projectID);
        if (existingMember != null) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "User is already a member of the project");
            return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
        }

        // Create project member
        ProjectMember projectMember = projectMemberService.createProjectMember(project, user, false);
        try {
            ProjectMember savedProjectMember = projectMemberRepository.save(projectMember);
            return new ResponseEntity<>(savedProjectMember, HttpStatus.CREATED);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to add member: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{projectID}/removeMember")
    public ResponseEntity<?> removeMember(@PathVariable int projectID, @RequestParam int userID) {
        try {
            // Check if project exists
            Project project = projectService.getProjectByID(projectID);
            if (project == null) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Project not found with ID: " + projectID);
                return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
            }

            // Check if member exists
            ProjectMember member = projectMemberService.getMemberByUserIDProjectID(userID, projectID);
            if (member == null) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Member not found with User ID: " + userID);
                return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
            }

            //check if member is owner
            if (member.isRole()) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Owner cannot be removed without transferring ownership or deleting the project.");
                return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
            }
            projectService.removeMember(member);
            Map<String, String> successResponse = new HashMap<>();
            successResponse.put("message", "Member removed successfully");
            return new ResponseEntity<>(successResponse, HttpStatus.OK);

        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to remove member: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{projectID}/transferOwnership")
    public ResponseEntity<?> transferOwnership(
            @PathVariable int projectID, 
            @RequestParam int newOwnerUserID) {

        //check that project exists
        Project project = projectService.getProjectByID(projectID);
        if (project == null) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Project not found with ID: " + projectID);
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        try {
            projectService.transferOwnership(projectID, newOwnerUserID);
            Project updatedProject = projectService.getProjectByID(projectID);
            return new ResponseEntity<>(updatedProject, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to transfer ownership: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{projectID}/progress")
    public ResponseEntity<?> getProjectProgress(@PathVariable int projectID) {
        // Check if project exists
        Project project = projectService.getProjectByID(projectID);
        if (project == null) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Project not found with ID: " + projectID);
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
        // return project progress
        Map<String, Double> progressResponse = new HashMap<>();
        progressResponse.put("progress", projectService.updateProjectProgress(projectID));
        return new ResponseEntity<>(progressResponse, HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{projectID}")
    public void deleteProject(@PathVariable int projectID) {
        projectService.deleteProjectByID(projectID);
    }
}
