package com.app.project.controller;

import com.app.project.model.CommentReaction;
import com.app.project.service.CommentReactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comment-reactions")
public class CommentReactionController {

    @Autowired
    private CommentReactionService commentReactionService;

    @GetMapping("/{id}")
    public ResponseEntity<CommentReaction> getCommentReactionById(@PathVariable long id) {
        CommentReaction commentReaction = commentReactionService.getCommentReactionById(id);

        return new ResponseEntity<>(commentReaction, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<CommentReaction>> getAllCommentReactions() {
        List<CommentReaction> commentReactions = commentReactionService.getAllCommentReactions();
        return new ResponseEntity<>(commentReactions, HttpStatus.OK);
    }

    @GetMapping("/comment/{commentId}")
    public ResponseEntity<List<CommentReaction>> getCommentReactionsByCommentId(@PathVariable int commentId) {
        List<CommentReaction> commentReactions = commentReactionService.getCommentReactionsByCommentId(commentId);
        return new ResponseEntity<>(commentReactions, HttpStatus.OK);

    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<CommentReaction>> getCommentReactionsByMemberId(@PathVariable int memberId) {
        List<CommentReaction> commentReactions = commentReactionService.getCommentReactionsByMemberId(memberId);
        return new ResponseEntity<>(commentReactions, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CommentReaction> createCommentReaction(@RequestBody CommentReaction commentReaction) {
        CommentReaction savedCommentReaction = commentReactionService.saveCommentReaction(commentReaction);
        return new ResponseEntity<>(savedCommentReaction, HttpStatus.OK);


    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCommentReaction(@PathVariable long id) {
        commentReactionService.deleteCommentReaction(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}