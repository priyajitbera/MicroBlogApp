package com.priyajit.microblogapp.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
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
        // to ensure no duplicate entry is added for two differnt users
        @UniqueConstraint(name = "UniqueFolloweeIdAndFoloweerId", columnNames = { "followee_id", "follower_id" })
})
public class Follow implements EntityOwnerDetails {

    @Id
    @SequenceGenerator(name = "follow_sequence", sequenceName = "follow_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "follow_sequence")
    private Long followId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "follower_id", referencedColumnName = "userId", nullable = false)
    private User follower; // who is following

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "followee_id", referencedColumnName = "userId", nullable = false)
    private User followee; // who is being followed

    public Long getFollowId() {
        return followId;
    }

    public void setFollowId(Long followId) {
        this.followId = followId;
    }

    public User getFollower() {
        return follower;
    }

    public void setFollower(User follower) {
        this.follower = follower;
    }

    public User getFollowee() {
        return followee;
    }

    public void setFollowee(User followee) {
        this.followee = followee;
    }

    @Override
    public String toString() {
        return "Follow [followId=" + followId + ", followee=" + followee + ", follower=" + follower + "]";
    }

    // ~ Implementation of the methods of the EntityOwnerDetails interface
    // --------------------------------------------------------------------------------------------------//
    @JsonIgnore
    @Override
    public String getOwner() {
        return getFollower().getEmail();
    }

    @Override
    public boolean matchOwnerWithAutenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String authenticatedUsername = (String) auth.getPrincipal();
        return getOwner().equals(authenticatedUsername);
    }

}
