package com.app.project.repository; 

import com.app.project.model.TaskAssignments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository; // Add if missing
import java.util.List; // Add if using List-based methods
import java.util.Optional; // Add if using Optional-based methods

@Repository // Add @Repository annotation
public interface TaskAssignmentsRepository extends JpaRepository<TaskAssignments, Integer> {

    // Add necessary methods, for example:
    List<TaskAssignments> findByMemberMemberID(int memberId);

    List<TaskAssignments> findByTaskTaskID(int taskId);

    Optional<TaskAssignments> findByTaskTaskIDAndMemberMemberID(int taskId, int memberId);

    void deleteByTaskTaskIDAndMemberMemberID(int taskId, int memberId);

    boolean existsByTaskTaskID(int taskId); // Useful for checking if a task has any assignments

    boolean existsByTaskTaskIDAndMemberMemberID(int taskId, int memberId); // Useful for checking if already assigned

}