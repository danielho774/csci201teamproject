package com.app.project.model;

import jakarta.persistence.*;

public class Reactions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reaction_id")
    private int reactionID;

    @Column(name = "reaction_type", nullable = false)
    private String reactionType;
}


