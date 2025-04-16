package com.app.project.model;

import java.time.LocalDate;
import java.util.List;

import jakarta.annotation.Priority;
import jakarta.persistence.*;

@Entity
@Table(name = "Tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private int taskID; 

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false) 
    private Project project; 

    @Column(name = "task_name", length = 25, nullable = false) 
    private String taskName; 

    @Column(name = "task_decrip", length = 150)
    private String taskDescrip; 

    @ManyToOne
    @JoinColumn(name = "status")
    private Status status; 

    @ManyToOne
    @JoinColumn(name = "priority") 
    private Priority priority; 

    @Column(name = "start_date")
    private LocalDate startDate; 

    @Column(name = "end_date")
    private LocalDate endDate; 

    @Column(name = "duration")
    private int duration; 

    @Column(name = "assigned")
    private boolean assigned; 

	// ManyToMany --> member can have multiple tasks
	// 			  --> multiple tasks can be assigned to multiple different
    //              members
	// Foreign key storage
	// joinColumns --> task 'owns' this table, tells JPA to 
	// 				store member that are associated with a Project
	// inverseJoinColumns --> the other entity of the table, tells 
	// 						JPA to store which Project is associated
	// 
    @ManyToMany 
    @JoinTable(
        name = "TaskAssignments", 
        joinColumns = @JoinColumn(name = "task_id"), 
        inverseJoinColumns = @JoinColumn(name = "member_id")
    )
    private List<User> assignees; 

    public Task(Project project, String taskName, String taskDescrip, 
                Status status, Priority priority, LocalDate startDate, 
                LocalDate endDate, int duration, boolean assigned) {
        
        this.project = project; 
        this.taskName = taskName; 
        this.taskDescrip = taskDescrip; 
        this.status = status; 
        this.priority = priority; 
        this.startDate = startDate; 
        this.endDate = endDate; 
        this.duration = duration; 
        this.assigned = assigned; 
    }
}

