package com.priyajit.microblogapp.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity
public class Credential {

    @Id
    @SequenceGenerator(name = "credential_sequece", sequenceName = "credential_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "credential_sequece")
    private Long credentialId;

    @Column(nullable = false)
    private String password;

    public Long getCredentialId() {
        return credentialId;
    }

    public void setCredentialId(Long credentialId) {
        this.credentialId = credentialId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Credential [credentialId=" + credentialId + ", password=" + password + "]";
    }

    // ~ Override the inherited methods of the Object class
    // --------------------------------------------------------------------------------------------------//

    @Override
    public boolean equals(Object obj) {
        Credential other = (Credential) obj;
        return this.credentialId.equals(other.getCredentialId())
                && this.password.equals(other.getPassword());
    }
}
