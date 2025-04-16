package com.app.project.controller;

import com.app.project.model.Project;
import com.app.project.service.ProjectService;  

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    // @Autowired
    // private ProjectService projectService;

    // @PostMapping
    // public ResponseEntity<Project> saveProject(@RequestBody Project project){
    //     return new ResponseEntity<Project>(projectService.saveProject(project), HttpStatus.CREATED);
    // }
    // //GetAll Rest Api
    // @GetMapping
    // public List<Project> getAllProject(){
    //     return ProjectService.getAllProject();
    // }

    // //Get by Id Rest Api
    // @GetMapping("{id}")
    // // localhost:8080/api/projects/1
    // public ResponseEntity<Project> getProjectById(@PathVariable("id") long projectID){
    //     return new ResponseEntity<Project>(projectService.getProjectById(projectID),HttpStatus.OK);
    // }

    // //Update Rest Api
    // @PutMapping("{id}")
    // public ResponseEntity<Project> updateProject(@PathVariable("id") long id,
    //                                                @RequestBody Project project){
    //     return new ResponseEntity<Project>(projectService.updateProject(project,id),HttpStatus.OK);
    // }

    // //Delete Rest Api
    // @DeleteMapping("{id}")
    // public ResponseEntity<String> deleteProject(@PathVariable("id") long id){
    //     //delete project from db
    //     projectService.deleteProject(id);
    //     return new ResponseEntity<String>("Project deleted Successfully.",HttpStatus.OK);
    // }

}