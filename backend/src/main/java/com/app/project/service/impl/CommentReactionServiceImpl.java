package com.app.project.service.impl;
import com.app.project.model.CommentReaction;
import com.app.project.repository.CommentReactionRepository;
import com.app.project.service.CommentReactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CommentReactionServiceImpl implements CommentReactionService {

    @Autowired
    private CommentReactionRepository CommentReactionRepository;

    @Override
    public CommentReaction getCommentReactionById(long id) {
        return CommentReactionRepository.findById((int) id).orElseThrow(
                () -> new RuntimeException("CommentReaction not found with id: " + id)
        );
    }


    @Override
    public List<CommentReaction> getAllCommentReactions() {
        return CommentReactionRepository.findAll();
    }



    @Override
    public List<CommentReaction> getCommentReactionsByCommentId(int commentId) {
        return CommentReactionRepository.findByCommentCommentID(commentId);
    }

    @Override
    public List<CommentReaction> getCommentReactionsByMemberId(int memberId) {
        return CommentReactionRepository.findByMemberMemberID(memberId);
    }
    

    @Override
    public CommentReaction saveCommentReaction(CommentReaction commentReaction) {
        return CommentReactionRepository.save(commentReaction);
    }

    @Override
    public void deleteCommentReaction(long id) {
        CommentReactionRepository.findById((int) id).orElseThrow(
                () -> new RuntimeException("CommentReaction not found with id: " + id)
        );
        CommentReactionRepository.deleteById((int) id);
    }
}