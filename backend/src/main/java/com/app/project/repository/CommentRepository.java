package com.app.project.repository;

import com.app.project.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findByTaskTaskID(int taskId);
    List<Comment> findByMemberMemberID(int memberId);
}