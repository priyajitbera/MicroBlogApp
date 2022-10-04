package com.priyajit.microblogapp.entity;

import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class User implements UserDetails, EntityOwnerDetails {
    public static final String INDIVIDUAL = "individual";
    public static final String ORGANAIZATION = "organization";

    // ~ Fields
    // --------------------------------------------------------------------------------------------------//
    @Id
    @SequenceGenerator(name = "user_sequence", sequenceName = "user_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_sequence")
    private Long userId;

    @Column(nullable = false, unique = true)
    private String handle; // unique to each User

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email; // unique to each User

    @Column(nullable = false)
    private Boolean verified = false;

    @Column(nullable = false)
    private String type = User.INDIVIDUAL; // DEFAULT

    // ~ Fields with mappings
    // --------------------------------------------------------------------------------------------------//

    @JsonIgnore // while mapping User object to JSON ignore child entity Credential
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "credential_id", referencedColumnName = "credentialId", foreignKey = @ForeignKey(name = "fk_user_credential"))
    private Credential credential;

    // ~ Getter Setters and toString method
    // --------------------------------------------------------------------------------------------------//
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Credential getCredential() {
        return credential;
    }

    public void setCredential(Credential credential) {
        this.credential = credential;
    }

    @Override
    public String toString() {
        return "User [email=" + email + ", firstName=" + firstName + ", handle=" + handle
                + ", lastName=" + lastName + ", type=" + type + ", userId=" + userId + ", verified=" + verified + "]";
    }

    // ~ Implementation of the methods of the EntityOwnerDetails interface
    // --------------------------------------------------------------------------------------------------//
    @JsonIgnore
    @Override
    public String getOwner() {
        return getEmail();
    }

    @Override
    public boolean matchOwnerWithAutenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String authenticatedUsername = (String) auth.getPrincipal();
        return getOwner().equals(authenticatedUsername);
    }

    // ~ Implementation of the methods of the UserDetails interface
    // --------------------------------------------------------------------------------------------------//
    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // TODO Auto-generated method stub
        return null;
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return getCredential().getPassword();
    }

    @Override
    public String getUsername() {
        return getEmail();
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        // TODO Auto-generated method stub
        return false;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        // TODO Auto-generated method stub
        return false;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        // TODO Auto-generated method stub
        return false;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        // TODO Auto-generated method stub
        return false;
    }

    public Object thenReturn(User user) {
        return null;
    }

    // ~ Override the inherited methods of the Object class
    // --------------------------------------------------------------------------------------------------//

    @Override
    public boolean equals(Object obj) {
        User other = (User) obj;
        return this.userId.equals(other.getUserId())
                && this.email.equals(other.getEmail())
                && this.handle.equals(other.getHandle())
                && this.firstName.equals(other.getFirstName())
                && this.lastName.equals(other.getLastName())
                && this.type.equals(other.getType())
                && this.verified.equals(other.getVerified()
                        && this.credential.equals(other.getCredential()));
    }

}
