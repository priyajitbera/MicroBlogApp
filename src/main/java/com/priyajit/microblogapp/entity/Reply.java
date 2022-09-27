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
public class Reply implements EntityOwnerDetails {
    public static final int REPLY_CHAR_LIMIT = 300;
    @Id
    @SequenceGenerator(name = "reply_sequence", sequenceName = "reply_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reply_sequence")
    private Long replyId;

    @Column(length = REPLY_CHAR_LIMIT)
    private String reply;

    @Column(nullable = false)
    private Date creationDate = new Date();

    @Column(nullable = false)
    private Boolean edited = false;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "post_id", referencedColumnName = "postId", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "userId", nullable = false)
    private User user;

    // @OneToMany(fetch = FetchType.EAGER, mappedBy = "reply")
    // private List<Reaction> reactions;

    public Long getReplyId() {
        return replyId;
    }

    public void setReplyId(Long replyId) {
        this.replyId = replyId;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
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

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    @Override
    public String toString() {
        return "Reply [creationDate=" + creationDate + ", edited=" + edited + ", reply=" + reply + ", replyId="
                + replyId + ", user=" + user + "]";
    }

    // ~ Implementation of the methods of the OwnerDetails interface
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
