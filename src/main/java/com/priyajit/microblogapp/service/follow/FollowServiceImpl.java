package com.priyajit.microblogapp.service.follow;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.priyajit.microblogapp.dto.FollowModel;
import com.priyajit.microblogapp.entity.Follow;
import com.priyajit.microblogapp.entity.User;
import com.priyajit.microblogapp.exception.EntityOwnerMismatchException;
import com.priyajit.microblogapp.exception.InvalidRequestException;
import com.priyajit.microblogapp.exception.UserNotFoundException;
import com.priyajit.microblogapp.repository.FollowRepo;
import com.priyajit.microblogapp.service.user.UserService;

@Service
public class FollowServiceImpl implements FollowService {

    @Autowired
    private FollowRepo followRepo;

    @Autowired
    private UserService userService;

    // ~ Implementation of the methods of the FollowService interface
    // --------------------------------------------------------------------------------------------------//

    @Override
    public Follow follow(FollowModel followModel) throws UserNotFoundException, InvalidRequestException {
        // validate if followeeId and followerId is different
        if (followModel.getFolloweeId() == followModel.getFollowerId()) {
            throw new InvalidRequestException("followerId and followeeId can not be same");
        }

        User followee = userService.findByUserId(followModel.getFolloweeId());
        User follower = userService.findByUserId(followModel.getFollowerId());

        Follow follow = new Follow();
        follow.setFollowee(followee);
        follow.setFollower(follower);

        return followRepo.save(follow);
    }

    @Override
    public void unfollow(FollowModel followModel)
            throws UserNotFoundException, EntityOwnerMismatchException, InvalidRequestException {
        // validate if followeeId and followerId is different
        if (followModel.getFolloweeId() == followModel.getFollowerId()) {
            throw new InvalidRequestException("followerId and followeeId can not be same");
        }

        User followee = userService.findByUserId(followModel.getFolloweeId());
        User follower = userService.findByUserId(followModel.getFollowerId());

        Follow follow = followRepo.findByFolloweeAndFollower(followee, follower);

        // validate whether the entity owner is same as the current authenticated user
        if (!follow.matchOwnerWithAutenticatedUser()) {
            throw new EntityOwnerMismatchException();
        }

        followRepo.delete(follow);
    }

    @Override
    public List<User> findFollowersByFolloweeId(Long followeeId) throws UserNotFoundException {
        User followee = userService.findByUserId(followeeId);
        List<Follow> followList = followRepo.findByFollowee(followee);
        List<User> followers = new LinkedList<>();
        for (Follow follow : followList)
            followers.add(follow.getFollower());
        return followers;
    }

    @Override
    public List<User> findFolloweesByFollowerId(Long followerId) throws UserNotFoundException {
        User follower = userService.findByUserId(followerId);
        List<Follow> followList = followRepo.findByFollower(follower);
        List<User> followees = new LinkedList<>();
        for (Follow follow : followList)
            followees.add(follow.getFollowee());
        return followees;
    }

}
