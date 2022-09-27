package com.priyajit.microblogapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.priyajit.microblogapp.entity.Follow;
import com.priyajit.microblogapp.entity.User;

@Repository
public interface FollowRepo extends JpaRepository<Follow, Long> {

    public List<Follow> findByFollowee(User followee);

    public List<Follow> findByFollower(User follower);

    public Follow findByFolloweeAndFollower(User followee, User follower);
}
