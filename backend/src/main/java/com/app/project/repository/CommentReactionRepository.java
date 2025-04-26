package com.app.project.repository;
import com.app.project.model.CommentReaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CommentReactionRepository extends JpaRepository<CommentReaction, Integer> {
    List<CommentReaction> findByCommentCommentID(int commentId);
    List<CommentReaction> findByMemberMemberID(int memberId);
}