package com.priyajit.microblogapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.priyajit.microblogapp.entity.Post;
import com.priyajit.microblogapp.entity.Reply;

@Repository
public interface ReplyRepo extends JpaRepository<Reply, Long> {

    public List<Reply> findByPost(Post post);

    public void deleteByPost(Post post);
}
