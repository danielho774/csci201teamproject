package com.app.project.controller;

import com.app.project.model.Project;
import com.app.project.service.ProjectMemberService;
import com.app.project.service.ProjectService;
import com.app.project.service.UserService;
import com.app.project.model.User;
import com.app.project.repository.ProjectMemberRepository;
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

        // Validate the request parameters
        if (name == null || name.isEmpty() || description == null || description.isEmpty() ||
            end_date == null || end_date.isEmpty() || start_date == null || start_date.isEmpty()) {
            return new ResponseEntity<>("All fields are required", HttpStatus.BAD_REQUEST);
        }

        // Check if user exists
        User owner = userService.getUserByID(userID);
        if (owner == null) {
            return new ResponseEntity<>("User not found with ID: " + userID, HttpStatus.NOT_FOUND);
        }

        // create project
        Project newProject = projectService.createProject(userID, name, description, end_date, start_date);
        if (newProject == null) {
            return new ResponseEntity<>("Failed to create project", HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return new ResponseEntity<>(newProject, HttpStatus.CREATED);
        }


    }

    @GetMapping("/{projectID}")
    public ResponseEntity<?> getProject(@PathVariable int projectID) {
        // Check if project exists
        Project project = projectService.getProjectByID(projectID);
        if (project == null) {
            return new ResponseEntity<>("Project not found with ID: " + projectID, HttpStatus.NOT_FOUND);
        }
        // return project details
        return new ResponseEntity<>(project, HttpStatus.OK);
    }

    @GetMapping("/{projectID}/members")
    public List<ProjectMember> getProjectMembers(@PathVariable int projectID) {
        return projectService.getProjectMembers(projectID);
    }

    @PostMapping("/{projectID}/addMember")
    public ResponseEntity<?> addMember(@PathVariable int projectID, @RequestParam int userID) {
        // Check if user exists
        User user = userService.getUserByID(userID);
        if (user == null) {
            return new ResponseEntity<>("User not found with ID: " + userID, HttpStatus.NOT_FOUND);
        }
        // Check if project exists
        Project project = projectService.getProjectByID(projectID);
        if (project == null) {
            return new ResponseEntity<>("Project not found with ID: " + projectID, HttpStatus.NOT_FOUND);
        }
        // Check if user is already a member of the project
        ProjectMember existingMember = projectMemberService.getMemberByUserIDProjectID(userID, projectID);
        if (existingMember != null) {
            return new ResponseEntity<>("User is already a member of the project", HttpStatus.CONFLICT);
        }

        // Create project member
        ProjectMember projectMember = projectMemberService.createProjectMember(project, user, false);
        try {
            projectMemberRepository.save(projectMember);
            return new ResponseEntity<>("Member added successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to add member: " + e.getMessage(), 
                                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{projectID}/removeMember")
    public ResponseEntity<?> removeMember(@PathVariable int projectID, @RequestParam int userID) {
        try {
            // Check if project exists
            Project project = projectService.getProjectByID(projectID);
            if (project == null) {
                return new ResponseEntity<>("Project not found with ID: " + projectID, HttpStatus.NOT_FOUND);
            }

            // Check if member exists
            ProjectMember member = projectMemberService.getMemberByUserIDProjectID(userID, projectID);
            if (member == null) {
                return new ResponseEntity<>("Member not found with User ID: " + userID, HttpStatus.NOT_FOUND);
            }

            //check if member is owner
            if (member.isRole()) {
                return new ResponseEntity<>("Owner cannot be removed without transferring ownership or deleting the project.", 
                                        HttpStatus.FORBIDDEN);
            }
            projectService.removeMember(member);
            return new ResponseEntity<>("Member removed successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to remove member: " + e.getMessage(), 
                                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{projectID}/transferOwnership")
    public ResponseEntity<?> transferOwnership(
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

    @GetMapping("/{projectID}/progress")
    public double getProjectProgress(@PathVariable int projectID) {
        return projectService.updateProjectProgress(projectID);
    }

    @DeleteMapping("/{projectID}")
    public void deleteProject(@PathVariable int projectID) {
        projectService.deleteProjectByID(projectID);
    }
}
