package com.priyajit.microblogapp.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.priyajit.microblogapp.dto.FollowModel;
import com.priyajit.microblogapp.dto.UserModel;
import com.priyajit.microblogapp.entity.Follow;
import com.priyajit.microblogapp.entity.RequestTracker;
import com.priyajit.microblogapp.entity.User;
import com.priyajit.microblogapp.exception.EntityOwnerMismatchException;
import com.priyajit.microblogapp.exception.InvalidRequestException;
import com.priyajit.microblogapp.exception.UserNotFoundException;
import com.priyajit.microblogapp.service.follow.FollowService;
import com.priyajit.microblogapp.service.user.UserService;

@RestController
@RequestMapping("/rest/user")
public class UserController {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    UserService userService;

    @Autowired
    FollowService followService;

    @GetMapping("/login")
    public ResponseEntity<Object> login() {
        logger.info("requestId : " + RequestTracker.getCurrentRequestId());
        return ResponseBuilder.buildResponse(null, HttpStatus.OK, "User logged in successfully!");
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody UserModel userModel) {

        User user = userService.register(userModel);
        return ResponseBuilder.buildResponse(user, HttpStatus.CREATED, "User registered successfully!");
    }

    @GetMapping("/byId/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable(name = "id") Long userId) throws UserNotFoundException {

        User user = userService.findByUserId(userId);
        return ResponseBuilder.buildResponse(user, HttpStatus.OK, null);
    }

    @GetMapping("/byEmail/{email}")
    public ResponseEntity<Object> getUserByEmail(@PathVariable(name = "email") String email)
            throws UserNotFoundException {

        User user = userService.findByEmail(email);
        return ResponseBuilder.buildResponse(user, HttpStatus.OK, null);
    }

    @GetMapping("/byHandle/{handle}")
    public ResponseEntity<Object> getUserByHandle(@PathVariable(name = "handle") String handle)
            throws UserNotFoundException {

        User user = userService.findByHandle(handle);
        return ResponseBuilder.buildResponse(user, HttpStatus.OK, "");
    }

    @PatchMapping("/update")
    public ResponseEntity<Object> updateUser(@RequestBody UserModel userModel)
            throws UserNotFoundException, EntityOwnerMismatchException {

        User user = userService.updateUser(userModel);
        return ResponseBuilder.buildResponse(user, HttpStatus.OK, null);
    }

    @PatchMapping("/changePassword")
    public ResponseEntity<Object> updatePassword(@RequestBody UserModel userModel)
            throws UserNotFoundException, EntityOwnerMismatchException {

        userService.changePassword(userModel);
        return ResponseBuilder.buildResponse(null, HttpStatus.OK, "Password changed Successfully");
    }

    @GetMapping("/followers/{userId}")
    public ResponseEntity<Object> getFollowersById(@PathVariable(name = "userId") Long userId)
            throws UserNotFoundException {

        List<User> followers = followService.findFollowersByFolloweeId(userId);
        return ResponseBuilder.buildResponse(followers, HttpStatus.OK, null);
    }

    @GetMapping("/followees/{userId}")
    public ResponseEntity<Object> getFolloweesById(@PathVariable(name = "userId") Long userId)
            throws UserNotFoundException {

        List<User> followees = followService.findFolloweesByFollowerId(userId);
        return ResponseBuilder.buildResponse(followees, HttpStatus.OK, null);
    }

    @PostMapping("/follow")
    public ResponseEntity<Object> follow(@RequestBody FollowModel followModel)
            throws UserNotFoundException, InvalidRequestException {

        Follow follow = followService.follow(followModel);
        return ResponseBuilder.buildResponse(follow, HttpStatus.OK, null);
    }

    @PostMapping("/unfollow")
    public ResponseEntity<Object> unfollow(@RequestBody FollowModel followModel)
            throws UserNotFoundException, EntityOwnerMismatchException, InvalidRequestException {

        followService.unfollow(followModel);
        return ResponseBuilder.buildResponse(null, HttpStatus.OK, "Unfollowed successfully!");
    }
}
