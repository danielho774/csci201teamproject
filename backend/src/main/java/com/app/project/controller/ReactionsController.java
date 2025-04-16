package com.app.project.controller;

import com.app.project.model.Reactions;
import com.app.project.service.ReactionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reactions")
public class ReactionsController {

    @Autowired
    private ReactionsService reactionsService;

    @GetMapping("/{id}")
    public ResponseEntity<Reactions> getReactionsById(@PathVariable long id) {
        Reactions reactions = reactionsService.getReactionsById(id);
        if (reactions != null) {
            return new ResponseEntity<>(reactions, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<List<Reactions>> getAllReactions() {
        List<Reactions> reactions = reactionsService.getAllReactions();
        return new ResponseEntity<>(reactions, HttpStatus.OK);
    }
}
