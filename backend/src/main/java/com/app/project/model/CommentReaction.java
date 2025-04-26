package com.app.project.model;

import jakarta.persistence.*;

@Entity
@Table(name = "CommentReactions")
public class CommentReaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_reaction_id")
    private int commentReactionID;

    @ManyToOne
    @JoinColumn(name = "comment_id", nullable = false)
    private Comment comment;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private ProjectMember member;

    @ManyToOne
    @JoinColumn(name = "reaction_id", nullable = false)
    private Reactions reaction;

    public CommentReaction() {}

    public CommentReaction(Comment comment, ProjectMember member, Reactions reaction) {
        this.comment = comment;
        this.member = member;
        this.reaction = reaction;
    }



    public int getCommentReactionID() {
        return commentReactionID;
    }

    public void setCommentReactionID(int commentReactionID) {
        this.commentReactionID = commentReactionID;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public ProjectMember getMember() {
        return member;
    }

    public void setMember(ProjectMember member) {
        this.member = member;
    }

    public Reactions getReaction() {
        return reaction;
    }

    public void setReaction(Reactions reaction) {
        this.reaction = reaction;
    }
}