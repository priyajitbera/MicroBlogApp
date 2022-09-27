package com.priyajit.microblogapp.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Post implements EntityOwnerDetails {
    public static final int CAPTION_CHAR_LIMIT = 300;
    @Id
    @SequenceGenerator(name = "post_sequence", sequenceName = "post_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "post_sequence")
    private Long postId;

    @Column(length = CAPTION_CHAR_LIMIT)
    private String caption; // caption is allowed to be kept empty or null

    @Column(nullable = false)
    private Date creationDate = new Date();

    @Column(nullable = false)
    private Boolean edited = false;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "userId", nullable = false)
    private User user;

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Boolean getEdited() {
        return edited;
    }

    public void setEdited(Boolean edited) {
        this.edited = edited;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // public List<Reaction> getReactions() {
    // return reactions;
    // }

    // public void setReactions(List<Reaction> reactions) {
    // this.reactions = reactions;
    // }

    @Override
    public String toString() {
        return "Post [caption=" + caption + ", creationDate=" + creationDate + ", edited=" + edited + ", postId="
                + postId + ", user=" + user + "]";
    }

    // ~ Implementation of the methods of the EntityOwnerDetails interface
    // --------------------------------------------------------------------------------------------------//
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
