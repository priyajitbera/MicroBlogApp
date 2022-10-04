package com.priyajit.microblogapp.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(uniqueConstraints = {
        // to ensure mulple reactions are not created for one post by one user
        @UniqueConstraint(name = "UniqueUserIdAndPostId", columnNames = { "user_id", "post_id" }),
        // to ensure mulple reactions are not created for one reply by one user
        @UniqueConstraint(name = "UniqeUserIdAndReplyId", columnNames = { "user_id", "reply_id" })
})
public class Reaction implements EntityOwnerDetails {
    public static final String LIKE = "like";
    public static final String CELEBRATE = "celebrate";
    public static final String LOVE = "love";

    @Id
    @SequenceGenerator(name = "reaction_sequence", sequenceName = "reaction_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reaction_sequence")
    private Long reactionId;

    @Column(nullable = false)
    private String type = Reaction.LIKE; // DEFAULT

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "userId", nullable = false, foreignKey = @ForeignKey(name = "fk_reaction_user"))
    private User user;

    @JsonIgnore // when ever mapping Reaction obj to JSON ignore Parent entity Post
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", referencedColumnName = "postId", foreignKey = @ForeignKey(name = "fk_reaction_post"))
    private Post post;

    @JsonIgnore // when ever mapping Reaction obj to JSON ignore child entity Credential
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reply_id", referencedColumnName = "replyId", foreignKey = @ForeignKey(name = "fk_reaction_reply"))
    private Reply reply;

    public Long getReactionId() {
        return reactionId;
    }

    public void setReactionId(Long reactionId) {
        this.reactionId = reactionId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Reply getReply() {
        return reply;
    }

    public void setReply(Reply reply) {
        this.reply = reply;
    }

    @Override
    public String toString() {
        return "Reaction [reactionId=" + reactionId + ", type=" + type + "]";
    }

    // ~ Implementation of the methods of the EntityOwnerDetails interface
    @JsonIgnore
    @Override
    public String getOwner() {
        return getUser().getEmail();
    }

    @Override
    public boolean matchOwnerWithAutenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String authenticatedUsername = (String) auth.getPrincipal();
        return getOwner().equals(authenticatedUsername);
    }
}
