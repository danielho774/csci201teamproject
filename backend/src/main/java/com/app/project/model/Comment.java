package com.app.project.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "Comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private int commentID;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private ProjectMember member;

    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @Column(name = "comment", length = 1000, nullable = false)
    private String comment;

    @Column(name = "date_created", nullable = false)
    private LocalDate dateCreated;

    @Column(name = "archived", nullable = false)
    private boolean archived;

    @Column(name = "resolved", nullable = false)
    private boolean resolved;

    public Comment() {}

    public Comment(ProjectMember member, Task task, String comment, LocalDate dateCreated, boolean archived, boolean resolved) {
        this.member = member;
        this.task = task;
        this.comment = comment;
        this.dateCreated = dateCreated;
        this.archived = archived;
        this.resolved = resolved;
    }

    public int getCommentID() {
        return commentID;
    }

    public void setCommentID(int commentID) {
        this.commentID = commentID;
    }

    public ProjectMember getMember() {
        return member;
    }

    public void setMember(ProjectMember member) {
        this.member = member;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDate getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }

    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    public boolean isResolved() {
        return resolved;
    }

    public void setResolved(boolean resolved) {
        this.resolved = resolved;
    }
    
}
