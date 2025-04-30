package com.app.project.service;
import com.app.project.model.CommentReaction;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface CommentReactionService {
    CommentReaction getCommentReactionById(long id);
    List<CommentReaction> getAllCommentReactions();
    List<CommentReaction> getCommentReactionsByCommentId(int commentId);
    List<CommentReaction> getCommentReactionsByMemberId(int memberId);
    CommentReaction saveCommentReaction(CommentReaction commentReaction);
    void deleteCommentReaction(long id);
}