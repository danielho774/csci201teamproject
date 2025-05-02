package com.app.project.repository;

import com.app.project.model.Project;
import com.app.project.model.ProjectMember;
import com.app.project.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository; // Add if missing
import java.util.List; // Add if needed
import java.util.Optional; // Add if needed

@Repository // Add @Repository annotation if missing
public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Integer> {

    // Add method to find a member by User ID and Project ID
    Optional<ProjectMember> findByUserUserIDAndProjectProjectID(int userId, int projectId);

    // Add method to find all memberships for a user
    List<ProjectMember> findByUserUserID(int userId);
    List<ProjectMember> findByProject_ProjectID(int projectID);
    //Role of user in a project
    Optional<ProjectMember> findByProjectProjectIDAndRoleTrue(int projectId);
    

}