package com.app.project.controller;

import com.app.project.service.ProjectMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/members")
public class ProjectMemberController {
    @Autowired
    private ProjectMemberService projectMemberService;

    ProjectMemberController(ProjectMemberService projectMemberServiceImpl) {
        this.projectMemberService = projectMemberServiceImpl;
    }

    @DeleteMapping("/{memberID}/leave")
    public void leaveProject(@PathVariable int memberID) {
        projectMemberService.leaveProject(memberID);
    }

    //owner leaving project
    @PostMapping("/{ownerMemberID}/transferAndLeave")
    public void ownerLeaveAndTransfer(@PathVariable int ownerMemberID, @RequestParam int newOwnerMemberID) {
        projectMemberService.ownerLeaveAndTransfer(ownerMemberID, newOwnerMemberID);
    }

    // Owner deletes project
    @DeleteMapping("/{ownerMemberID}/deleteProject")
    public void ownerDeleteProject(@PathVariable int ownerMemberID) {
        projectMemberService.ownerDeleteProject(ownerMemberID);
    }
}
