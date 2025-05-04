package com.app.project.service;
import org.springframework.stereotype.Service;
import com.app.project.model.Availability;
import java.util.List;
import java.util.Optional;

@Service
public interface AvailabilityService {
    // basic CRUD operations
    Availability saveAvailability(Availability availability);
    void deleteAvailability(int availID);
    Optional<Availability> getAvailabilityByID(int availID);
    List<Availability> getAllAvailabilities();

    // custom methods
    List<Availability> getAllAvailabilitiesByProjectID(int projectID); 
    List<Availability> getAllAvailabilitiesByUserID(int userID); 
    List<Availability> getAllAvailabilitiesByProjectIDAndUserID(int projectID, int userID);
    List<Availability> getAllAvailabilitiesByProjectIDAndDate(int projectID, String date);
    List<Availability> getAllAvailabilitiesByProjectIDAndDateAndUserID(int projectID, String date, int userID);

}
