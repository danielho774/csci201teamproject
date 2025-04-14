package com.app.project.controller;

import com.app.project.model.Comment;
import com.app.project.service.CommentService;  

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/comments")
public class CommentController {

    // @Autowired
    // private CommentService commentService;

    // @PostMapping
    // public ResponseEntity<Comment> saveComment(@RequestBody Comment comment){
    //     return new ResponseEntity<Comment>(commentService.saveComment(comment), HttpStatus.CREATED);
    // }
    // //GetAll Rest Api
    // @GetMapping
    // public List<Comment> getAllComment(){
    //     return commentService.getAllComment();
    // }

    // //Get by Id Rest Api
    // @GetMapping("{id}")
    // // localhost:8080/api/comments/1
    // public ResponseEntity<Comment> getCommentById(@PathVariable("id") long commentID){
    //     return new ResponseEntity<Comment>(commentService.getCommentById(commentID),HttpStatus.OK);
    // }

    // //Update Rest Api
    // @PutMapping("{id}")
    // public ResponseEntity<Comment> updateComment(@PathVariable("id") long id,
    //                                                @RequestBody Comment comment){
    //     return new ResponseEntity<Comment>(commentService.updateComment(comment,id),HttpStatus.OK);
    // }

    // //Delete Rest Api
    // @DeleteMapping("{id}")
    // public ResponseEntity<String> deleteComment(@PathVariable("id") long id){
    //     //delete Comment from db
    //     commentService.deleteComment(id);
    //     return new ResponseEntity<String>("Comment deleted Successfully.",HttpStatus.OK);
    // }

}