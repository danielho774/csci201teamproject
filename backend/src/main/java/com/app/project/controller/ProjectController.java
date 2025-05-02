package com.app.project.controller;

import com.app.project.model.Project;
import com.app.project.model.ProjectMember;
import com.app.project.service.impl.ProjectMemberServiceImpl;
import com.app.project.service.ProjectService;
import com.app.project.service.UserService;
import com.app.project.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import java.util.List;


@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ProjectMemberServiceImpl projectMemberService;

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public Project createProject(
        @RequestParam int userID,
        @RequestParam String name,
        @RequestParam String description,
        @RequestParam String end_date,
        @RequestParam String start_date) {
        Project project = new Project(name, description, end_date, start_date);
        int projectID = projectService.saveProject(project).getProjectID();
        int memberID = projectMemberService.createProjectMember(projectID, userID, true);

        ProjectMember owner = projectMemberService.getMember(memberID).orElseThrow();
        project.setOwner(owner);
        return projectService.saveProject(project);
    }

    @GetMapping("/{projectID}")
    public Optional<Project> getProject(@PathVariable int projectID) {
        return projectService.getProject(projectID);
    }

    @PostMapping("/addMember/{projectID}")
    public void addMember(@PathVariable int projectID, @RequestParam int memberID) {
        Project project = projectService.getProjectById(projectID);
        User member = userService.getUserByID(memberID);
        projectService.addMember(project, member);
    }

    @PostMapping("/transferOwnership/{projectID}")
    public void transferOwnership(@PathVariable int projectID, @RequestParam int newOwnerMemberID) {
        projectService.transferOwnership(projectID, newOwnerMemberID);
    }

    @GetMapping("/progress/{projectID}")
    public double getProjectProgress(@PathVariable int projectID) {
        return projectService.calculateProjectProgress(projectID);
    }

    @DeleteMapping("/{projectID}")
    public void deleteProject(@PathVariable int projectID) {
        projectService.deleteProject(projectID);
    }
}
