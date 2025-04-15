package com.app.project.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Projects")
public class Project {


    

    public User getUser() {
        return user; 
    }
}