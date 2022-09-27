package com.priyajit.microblogapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.priyajit.microblogapp.entity.Credential;

@Repository
public interface CredentialRepo extends JpaRepository<Credential, Long> {

    // Optional<Credential> findByUser(User user);
}
