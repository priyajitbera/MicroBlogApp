package com.priyajit.microblogapp.service.user;

import com.priyajit.microblogapp.dto.UserModel;
import com.priyajit.microblogapp.entity.User;
import com.priyajit.microblogapp.exception.DBException;
import com.priyajit.microblogapp.exception.EntityOwnerMismatchException;
import com.priyajit.microblogapp.exception.UserNotFoundException;

public interface UserService {

    public User register(UserModel registrationModel) throws DBException;

    public User findByUserId(Long userId) throws UserNotFoundException;

    public User findByEmail(String email) throws UserNotFoundException;

    public User findByHandle(String handle) throws UserNotFoundException;

    public User findByEmailOrHandle(String email, String handle) throws UserNotFoundException;

    public User updateUser(UserModel registrationModel)
            throws UserNotFoundException, EntityOwnerMismatchException, DBException;

    public void changePassword(UserModel userModel)
            throws UserNotFoundException, EntityOwnerMismatchException, DBException;
}
