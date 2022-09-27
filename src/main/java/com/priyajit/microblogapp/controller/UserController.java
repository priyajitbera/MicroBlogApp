package com.priyajit.microblogapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.priyajit.microblogapp.dto.FollowModel;
import com.priyajit.microblogapp.dto.UserModel;
import com.priyajit.microblogapp.entity.Follow;
import com.priyajit.microblogapp.entity.User;
import com.priyajit.microblogapp.exception.DBException;
import com.priyajit.microblogapp.exception.EntityOwnerMismatchException;
import com.priyajit.microblogapp.exception.InvalidRequestException;
import com.priyajit.microblogapp.exception.UserNotFoundException;
import com.priyajit.microblogapp.service.follow.FollowService;
import com.priyajit.microblogapp.service.user.UserService;

@RestController
@RequestMapping("/rest/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    FollowService followService;

    @GetMapping("/login")
    public void login() {
    }

    @PostMapping("/register")
    public User register(@RequestBody UserModel userModel) {
        try {
            return userService.register(userModel);
        } catch (DataIntegrityViolationException ex) {
            ex.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        } catch (DBException ex) {
            ex.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    @GetMapping("/byId/{id}")
    public User getUserById(@PathVariable(name = "id") Long userId) {
        if (userId == null)
            return null;
        try {
            return userService.findByUserId(userId);
        } catch (UserNotFoundException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/byEmail/{email}")
    public User getUserByEmail(@PathVariable(name = "email") String email) {
        if (email == null) {
            return null;
        }
        try {
            return userService.findByEmail(email);
        } catch (UserNotFoundException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/byHandle/{handle}")
    public User getUserByHandle(@PathVariable(name = "handle") String handle) {
        if (handle == null) {

        }
        try {
            return userService.findByHandle(handle);
        } catch (UserNotFoundException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PatchMapping("/update")
    public User updateUser(@RequestBody UserModel userModel) throws DBException {
        try {
            return userService.updateUser(userModel);
        } catch (UserNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (EntityOwnerMismatchException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        }
    }

    @PatchMapping("/changePassword")
    public void updatePassword(@RequestBody UserModel userModel) {
        try {
            userService.changePassword(userModel);
        } catch (UserNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (EntityOwnerMismatchException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        } catch (DBException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/followers/{userId}")
    public List<User> getFollowersById(@PathVariable(name = "userId") Long userId) {
        try {
            return followService.findFollowersByFolloweeId(userId);
        } catch (UserNotFoundException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/followees/{userId}")
    public List<User> getFolloweesById(@PathVariable(name = "userId") Long userId) {
        try {
            return followService.findFolloweesByFollowerId(userId);
        } catch (UserNotFoundException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping("/follow")
    public Follow follow(@RequestBody FollowModel followModel) {
        try {
            return followService.follow(followModel);
        } catch (UserNotFoundException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (InvalidRequestException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/unfollow")
    public void unfollow(@RequestBody FollowModel followModel) {
        try {
            followService.unfollow(followModel);
        } catch (UserNotFoundException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (EntityOwnerMismatchException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        } catch (InvalidRequestException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
