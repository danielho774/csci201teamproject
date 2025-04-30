package com.app.project.model;

import java.util.List;

import jakarta.persistence.*;


@Entity
@Table(name = "Reactions")
public class Reactions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reaction_id")
    private int reactionID;

    @Column(name = "reaction_type", nullable = false)
    private String reactionType;

    @OneToMany(mappedBy = "reaction")
    private List<CommentReaction> commentReactions;

    //empty constructor for JPA
    public Reactions() {}
}


