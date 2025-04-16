package com.app.project.repository; 

import com.app.project.model.ProjectMember; 
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Integer> {
    
}
