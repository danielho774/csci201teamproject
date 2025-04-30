package com.app.project.service;
import com.app.project.model.Comment;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface CommentService {
    Comment getCommentById(long id);
    List<Comment> getAllComments();
    List<Comment> getCommentsByTaskId(int taskId);
    List<Comment> getCommentsByMemberId(int memberId);
    Comment saveComment(Comment comment);
    void deleteComment(long id);
}