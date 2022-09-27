package com.priyajit.microblogapp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.priyajit.microblogapp.entity.Post;
import com.priyajit.microblogapp.entity.Reaction;
import com.priyajit.microblogapp.entity.Reply;
import com.priyajit.microblogapp.entity.User;

@Repository
public interface ReactionRepo extends JpaRepository<Reaction, Long> {

    public List<Reaction> findByPost(Post post);

    public List<Reaction> findByReply(Reply reply);

    public Optional<Reaction> findByUserAndPost(User user, Post post);

    public Optional<Reaction> findByUserAndReply(User user, Reply reply);

}
