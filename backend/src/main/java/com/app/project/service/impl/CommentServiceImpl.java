package com.app.project.service.impl;
import com.app.project.model.Comment;
import com.app.project.repository.CommentRepository;
import com.app.project.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository CommentRepository;

    @Override
    public Comment getCommentById(long id) {
        return CommentRepository.findById((int) id).orElseThrow(
                () -> new RuntimeException("Comment not found with id: " + id)
        );
    }

    @Override
    public List<Comment> getAllComments() {
        return CommentRepository.findAll();
    }

    @Override
    public List<Comment> getCommentsByTaskId(int taskId) {
        return CommentRepository.findByTaskTaskID(taskId);
        
    }

    @Override
    public List<Comment> getCommentsByMemberId(int memberId) {
        return CommentRepository.findByMemberMemberID(memberId);
    }

    @Override
    public Comment saveComment(Comment comment) {
        return CommentRepository.save(comment);
    }

    @Override
    public void deleteComment(long id) {
        CommentRepository.findById((int) id).orElseThrow(
                () -> new RuntimeException("Comment not found with id: " + id)
        );
        CommentRepository.deleteById((int) id);
    }
}