package com.app.project.repository;
 
import com.app.project.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {
    //caused errors and unused, commenting out for now 
    
    // Optional: find all projects for a user (owner or member)
    //List<Project> findByOwnerEmail(String ownerEmail);

    //@Query("SELECT p FROM Project p JOIN p.members m WHERE m.email = :email")
    //List<Project> findByMemberEmail(@Param("email") String email);
}
