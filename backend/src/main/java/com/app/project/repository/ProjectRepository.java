package com.app.project.repository;
import java.util.List; 
import com.app.project.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {
    // Optional: find all projects for a user (owner or member)
    List<Project> findByOwnerEmail(String ownerEmail);

    @Query("SELECT p FROM Project p JOIN p.members m WHERE m.email = :email")
    List<Project> findByMemberEmail(@Param("email") String email);
}
