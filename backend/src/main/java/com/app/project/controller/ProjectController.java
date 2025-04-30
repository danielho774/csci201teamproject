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
    public Project createProject(@RequestBody Project project, @RequestParam int memberID) {
        return projectService.createProject(project, memberID);
    }

    @GetMapping("/{projectID}")
    public Optional<Project> getProject(@PathVariable int projectID) {
        return projectService.getProject(projectID);
    }

    @PostMapping("/{projectID}/addMember")
    public void addMember(@PathVariable Project project, @RequestParam int memberID) {
        User member = userService.getUserByID(memberID);
        projectService.addMember(project, member);
    }

    @PostMapping("/{projectID}/leave")
    public void leaveProject(@PathVariable int projectID, @RequestParam int memberID) {
        projectMemberService.leaveProject(memberID);
    }

    @PostMapping("/{projectID}/transferOwnership")
    public void transferOwnership(@PathVariable int projectID, @RequestParam int newOwnerMemberID) {
        projectService.transferOwnership(projectID, newOwnerMemberID);
    }


    @GetMapping("/{projectID}/progress")
    public double getProjectProgress(@PathVariable int projectID) {
        return projectService.calculateProjectProgress(projectID);
    }
    @DeleteMapping("/{projectID}")
    public void deleteProject(@PathVariable int projectID) {
        projectService.deleteProject(projectID);
    }
}
