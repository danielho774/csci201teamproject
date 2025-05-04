package com.app.project.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.app.project.model.Availability;
import com.app.project.model.Project;
import com.app.project.model.User;
import com.app.project.service.AvailabilityService;
import com.app.project.service.ProjectService;
import com.app.project.service.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/availability")
public class AvailabilityController {

    ///* AVAILABILITY SERVICE ROUTES
    /// 
    /// (create availability for a user in a project) -> /add/user/{userID}/project/{projectID} + Body:{ "date":{date}, "startTime":{startTime}, "endTime":{endTime} } 
    /// 
    /// (get all availabilities for a project) -> /get/all/project/{projectID}
    /// 
    /// (get all availabilities for a user in a project) -> /get/all/user/{userID}/project/{projectID}
    /// 
    /// (get availability for a user in a project on a specific date) -> /get/user/{userID}/project/{projectID} + Body:{ "date":{date} }
    /// 
    /// (delete availability for a user in a project on a specific date) -> /delete/user/{userID}/project/{projectID} + Body:{ "date":{date} }

    @Autowired
    private AvailabilityService availabilityService;

    @Autowired 
    private UserService userService;

    @Autowired
    private ProjectService projectService;

    // create availability for a user in a project
    @PostMapping("/add/user/{userID}/project/{projectID}")
    public ResponseEntity<?> addAvailability(@RequestBody Availability request, @PathVariable int userID, @PathVariable int projectID) {
        String date = request.getDate();
        String startTime = request.getStartTime();
        String endTime = request.getEndTime();

        //check if userID and projectID are valid
        User user = userService.getUserByID(userID);
        if (user == null) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "User not found with ID: " + userID);
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        Project project = projectService.getProjectByID(projectID);
        if (project == null) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Project not found with ID: " + projectID);
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        // check if times are in "hh:mm:ss" format
        if (!startTime.matches("\\d{2}:\\d{2}:\\d{2}") || !endTime.matches("\\d{2}:\\d{2}:\\d{2}")) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Invalid time format. Use 'hh:mm:ss'");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        // check if date is in "yyyy-MM-dd" format
        if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Invalid date format. Use 'yyyy-MM-dd'");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        // check if start time is before end time
        if (startTime.compareTo(endTime) >= 0) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Invalid time range. Start time must be before end time");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        Availability availability = new Availability(user, project, date, startTime, endTime);

        Availability savedAvailability = availabilityService.saveAvailability(availability);
        if (savedAvailability == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(savedAvailability, HttpStatus.CREATED);
    }

    // get all availabilities for a project
    @GetMapping("/get/all/project/{projectID}")
    public ResponseEntity<?> getAvailabilityByProject(@PathVariable int projectID) {
        Project project = projectService.getProjectByID(projectID);
        if (project == null) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Project not found with ID: " + projectID);
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        List<Availability> availabilities = availabilityService.getAllAvailabilitiesByProjectID(projectID);
        if (availabilities.isEmpty()) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "No availabilities found for the project");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
        
        return new ResponseEntity<>(availabilities, HttpStatus.OK);
    }

    // get all availabilities for a user in a project
    @GetMapping("/get/all/user/{userID}/project/{projectID}")
    public ResponseEntity<?> getAvailabilityByUserAndProject(@PathVariable int userID, @PathVariable int projectID) {
        User user = userService.getUserByID(userID);
        if (user == null) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "User not found with ID: " + userID);
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        Project project = projectService.getProjectByID(projectID);
        if (project == null) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Project not found with ID: " + projectID);
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        List<Availability> availabilities = availabilityService.getAllAvailabilitiesByProjectIDAndUserID(projectID, userID);
        if (availabilities.isEmpty()) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "No availabilities found for the user and project");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
        
        return new ResponseEntity<>(availabilities, HttpStatus.OK);
    }

    // get all availabilities for a user in a project on a specific date
    @GetMapping("/get/user/{userID}/project/{projectID}")
    public ResponseEntity<?> getAvailabilityByUserAndProjectAndDate(@PathVariable int userID, @PathVariable int projectID, @RequestParam String date) {
        User user = userService.getUserByID(userID);
        if (user == null) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "User not found with ID: " + userID);
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        Project project = projectService.getProjectByID(projectID);
        if (project == null) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Project not found with ID: " + projectID);
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        // check if date is in "yyyy-MM-dd" format
        if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Invalid date format. Use 'yyyy-MM-dd'");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        List<Availability> availabilities = availabilityService.getAllAvailabilitiesByProjectIDAndDateAndUserID(projectID, date, userID);
        if (availabilities.isEmpty()) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "No availabilities found for the user and project on the same date");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
        
        return new ResponseEntity<>(availabilities, HttpStatus.OK);
    }

    // delete availability for a user in a project on a specific date
    @DeleteMapping("/delete/user/{userID}/project/{projectID}")
    public ResponseEntity<?> deleteAvailability(@PathVariable int userID, @PathVariable int projectID, @RequestParam String date) {
        User user = userService.getUserByID(userID);
        if (user == null) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "User not found with ID: " + userID);
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        Project project = projectService.getProjectByID(projectID);
        if (project == null) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Project not found with ID: " + projectID);
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        // check if date is in "yyyy-MM-dd" format
        if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Invalid date format. Use 'yyyy-MM-dd'");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        List<Availability> availabilities = availabilityService.getAllAvailabilitiesByProjectIDAndDateAndUserID(projectID, date, userID);
        if (availabilities.isEmpty()) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "No availabilities found for the user and project on the same date");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        for (Availability availability : availabilities) {
            availabilityService.deleteAvailability(availability.getAvailID());
        }
        
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
