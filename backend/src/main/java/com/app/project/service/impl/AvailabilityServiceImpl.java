package com.app.project.service.impl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.app.project.model.Availability;
import com.app.project.service.AvailabilityService;
import com.app.project.repository.AvailabilityRepository;
import java.util.Optional;

import java.util.List;

@Service
public class AvailabilityServiceImpl implements AvailabilityService {

    @Autowired
    private AvailabilityRepository availabilityRepository;

    // basic CRUD operations
    @Override
    public Availability saveAvailability(Availability availability) {
        return availabilityRepository.save(availability);
    }

    @Override
    public void deleteAvailability(int availID) {
        availabilityRepository.deleteById(availID);
    }

    @Override
    public Optional<Availability> getAvailabilityByID(int availID) {
        return availabilityRepository.findById(availID);
    }

    @Override
    public List<Availability> getAllAvailabilities() {
        return availabilityRepository.findAll();
    }

    // custom methods

    @Override
    public List<Availability> getAllAvailabilitiesByProjectID(int projectID) {
        return availabilityRepository.findAllByProjectProjectID(projectID);
    }

    @Override
    public List<Availability> getAllAvailabilitiesByUserID(int userID) {
        return availabilityRepository.findAllByUserUserID(userID);
    }

    @Override
    public List<Availability> getAllAvailabilitiesByProjectIDAndUserID(int projectID, int userID) {
        return availabilityRepository.findAllByProjectProjectIDAndUserUserID(projectID, userID);
    }

    @Override
    public List<Availability> getAllAvailabilitiesByProjectIDAndDate(int projectID, String date) {
        return availabilityRepository.findAllByProjectProjectIDAndDate(projectID, date);
    }

    @Override
    public List<Availability> getAllAvailabilitiesByProjectIDAndDateAndUserID(int projectID, String date, int userID) {
        return availabilityRepository.findAllByProjectProjectIDAndDateAndUserUserID(projectID, date, userID);
    }

}
