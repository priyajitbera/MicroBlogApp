package com.priyajit.microblogapp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.priyajit.microblogapp.entity.User;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {

    // public Optional<User> save(User user);

    public Optional<User> findByHandle(String userName);

    public Optional<User> findByEmail(String email);

    public Optional<User> findByEmailOrHandle(String email, String handle);

}
