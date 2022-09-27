package com.priyajit.microblogapp.service.follow;

import java.util.List;

import com.priyajit.microblogapp.dto.FollowModel;
import com.priyajit.microblogapp.entity.Follow;
import com.priyajit.microblogapp.entity.User;
import com.priyajit.microblogapp.exception.EntityOwnerMismatchException;
import com.priyajit.microblogapp.exception.InvalidRequestException;
import com.priyajit.microblogapp.exception.UserNotFoundException;

public interface FollowService {

    public Follow follow(FollowModel followModel) throws UserNotFoundException, InvalidRequestException;

    public void unfollow(FollowModel followModel)
            throws UserNotFoundException, EntityOwnerMismatchException, InvalidRequestException,
            com.priyajit.microblogapp.exception.EntityOwnerMismatchException;

    public List<User> findFollowersByFolloweeId(Long followeeId) throws UserNotFoundException;

    public List<User> findFolloweesByFollowerId(Long followerId) throws UserNotFoundException;

}
