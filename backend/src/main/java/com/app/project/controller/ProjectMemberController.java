package com.app.project.controller;

import com.app.project.service.impl.ProjectMemberServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/members")
public class ProjectMemberController {

    private final ProjectMemberServiceImpl projectMemberServiceImpl;

    @Autowired
    private ProjectMemberServiceImpl projectMemberService;

    ProjectMemberController(ProjectMemberServiceImpl projectMemberServiceImpl) {
        this.projectMemberServiceImpl = projectMemberServiceImpl;
    }

    @DeleteMapping("/{memberID}/leave")
    public void leaveProject(@PathVariable int memberID) {
        projectMemberServiceImpl.leaveProject(memberID);
    }

    //owner leaving project
    @PostMapping("/{ownerMemberID}/transferAndLeave")
    public void ownerLeaveAndTransfer(@PathVariable int ownerMemberID, @RequestParam int newOwnerMemberID) {
        projectMemberServiceImpl.ownerLeaveAndTransfer(ownerMemberID, newOwnerMemberID);
    }

    // Owner deletes project
    @DeleteMapping("/{ownerMemberID}/deleteProject")
    public void ownerDeleteProject(@PathVariable int ownerMemberID) {
        projectMemberServiceImpl.ownerDeleteProject(ownerMemberID);
    }
}
