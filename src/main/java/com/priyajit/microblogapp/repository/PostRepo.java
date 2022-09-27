package com.priyajit.microblogapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.priyajit.microblogapp.entity.Post;
import com.priyajit.microblogapp.entity.User;

@Repository
public interface PostRepo extends JpaRepository<Post, Long> {

    public List<Post> findByUser(User user);
}
