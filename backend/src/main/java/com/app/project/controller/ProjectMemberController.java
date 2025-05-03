package com.app.project.controller;

import com.app.project.model.ProjectMember;
import com.app.project.service.ProjectMemberService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
public class ProjectMemberController {
    @Autowired
    private ProjectMemberService projectMemberService;

    @GetMapping("/{memberID}")
    public ProjectMember getMember(@PathVariable int memberID) {
        return projectMemberService.getMember(memberID);
    }

    @GetMapping("/project/{projectID}")
    public List<ProjectMember> getAllMembers(@PathVariable int projectID) {
        return projectMemberService.getAllMembers(projectID);
    }

    @DeleteMapping("/{memberID}/leave/{projectID}")
    public void leaveProject(@PathVariable int memberID, @PathVariable int projectID) {
        projectMemberService.leaveProject(memberID, projectID);
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
}
