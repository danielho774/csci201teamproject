package com.app.project.repository; 

import com.app.project.model.Availability;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AvailabilityRepository extends JpaRepository<Availability, Integer> {
    // custom query methods
    List<Availability> findAllByProjectProjectID(int projectID);
    List<Availability> findAllByUserUserID(int userID);
    List<Availability> findAllByProjectProjectIDAndUserUserID(int projectID, int userID);
    List<Availability> findAllByProjectProjectIDAndDate(int projectID, String date);
    List<Availability> findAllByProjectProjectIDAndDateAndUserUserID(int projectID, String date, int userID);
}
