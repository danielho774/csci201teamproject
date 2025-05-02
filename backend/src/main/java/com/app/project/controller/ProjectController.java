package com.app.project.controller;

import com.app.project.model.Project;
import com.app.project.service.ProjectService;
import com.app.project.service.UserService;
import com.app.project.model.User;
import com.app.project.model.ProjectMember;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus; 

import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserService userService;

    @PostMapping("/{userID}/createProject")
    public Project createProject( @PathVariable int userID, @RequestBody Project projectRequest) {

        String name = projectRequest.getProjectName();
        String description = projectRequest.getProjectDescription();
        String end_date = projectRequest.getEndDate();
        String start_date = projectRequest.getStartDate();

        // Validate the request parameters
        if (name == null || name.isEmpty() || description == null || description.isEmpty() ||
            end_date == null || end_date.isEmpty() || start_date == null || start_date.isEmpty()) {
            throw new IllegalArgumentException("All fields are required");
        }

        // Check if user exists
        User owner = userService.getUserByID(userID);
        if (owner == null) {
            throw new IllegalArgumentException("User not found with ID: " + userID);
        }

        return projectService.createProject(userID, name, description, end_date, start_date);
    }

    @GetMapping("/{projectID}")
    public Optional<Project> getProject(@PathVariable int projectID) {
        return projectService.getProject(projectID);
    }

    @GetMapping("/{projectID}/members")
    public List<ProjectMember> getProjectMembers(@PathVariable int projectID) {
        return projectService.getProjectMembers(projectID);
    }

    @PostMapping("/addMember/{projectID}")
    public void addMember(@PathVariable int projectID, @RequestParam int memberID) {
        Project project = projectService.getProjectById(projectID);
        User member = userService.getUserByID(memberID);
        projectService.addMember(project, member);
    }

    @PostMapping("/transferOwnership/{projectID}")
    public ResponseEntity<String> transferOwnership(
            @PathVariable int projectID, 
            @RequestParam int newOwnerUserID) {
        try {
            projectService.transferOwnership(projectID, newOwnerUserID);
            return new ResponseEntity<>("Ownership transferred successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to transfer ownership: " + e.getMessage(), 
                                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/progress/{projectID}")
    public double getProjectProgress(@PathVariable int projectID) {
        return projectService.updateProjectProgress(projectID);
    }

    @DeleteMapping("/{projectID}")
    public void deleteProject(@PathVariable int projectID) {
        projectService.deleteProject(projectID);
    }
}
