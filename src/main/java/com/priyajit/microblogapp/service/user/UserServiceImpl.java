package com.priyajit.microblogapp.service.user;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.priyajit.microblogapp.dto.UserModel;
import com.priyajit.microblogapp.entity.Credential;
import com.priyajit.microblogapp.entity.RequestTracker;
import com.priyajit.microblogapp.entity.User;
import com.priyajit.microblogapp.exception.EntityOwnerMismatchException;
import com.priyajit.microblogapp.exception.UserNotFoundException;
import com.priyajit.microblogapp.repository.CredentialRepo;
import com.priyajit.microblogapp.repository.UserRepo;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    UserRepo userRepo;

    @Autowired
    CredentialRepo credentialRepo;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public User register(UserModel userModel) {

        User user = new User();
        user.setEmail(userModel.getEmail());
        user.setHandle(userModel.getHandle());
        user.setFirstName(userModel.getFirstName());
        user.setLastName(userModel.getLastName());
        user.setType(userModel.getType());

        Credential credential = new Credential();
        credential.setPassword(passwordEncoder.encode(userModel.getPassword())); // must encode with
                                                                                 // passwordEncoder
        user.setCredential(credential);

        return userRepo.save(user);

    }

    @Override
    public User findByEmail(String email) throws UserNotFoundException {
        Optional<User> userOpt = userRepo.findByEmail(email);
        if (!userOpt.isPresent()) {
            logger.error("requestId: " + RequestTracker.getCurrentRequestId()); // logging
            logger.error("Error while saving in DB");
            throw new UserNotFoundException(email);
        }
        return userOpt.get();
    }

    @Override
    public User findByHandle(String handle) throws UserNotFoundException {
        Optional<User> userOpt = userRepo.findByHandle(handle);
        if (!userOpt.isPresent()) {
            // logger.info("Loggin from UserServiceImpl");
            throw new UserNotFoundException(handle);
        }
        return userOpt.get();
    }

    @Override
    public User findByEmailOrHandle(String email, String handle) throws UserNotFoundException {
        Optional<User> userOpt = userRepo.findByEmailOrHandle(email, handle);
        if (!userOpt.isPresent()) {
            throw new UserNotFoundException(handle);
        }
        return userOpt.get();
    }

    @Override
    public User findByUserId(Long userId) throws UserNotFoundException {
        Optional<User> userOpt = userRepo.findById(userId);
        if (!userOpt.isPresent()) {
            throw new UserNotFoundException(userId);
        }
        return userOpt.get();
    }

    @Override
    public User updateUser(UserModel userModel)
            throws UserNotFoundException, EntityOwnerMismatchException {

        Optional<User> userOpt = userRepo.findById(userModel.getUserId());

        // validate if user present with given userId
        if (!userOpt.isPresent()) {
            throw new UserNotFoundException(userModel.getUserId());
        }

        User user = userOpt.get();

        // validate whether the entity owner is same as the current authenticated user
        if (!user.matchOwnerWithAutenticatedUser()) {
            throw new EntityOwnerMismatchException();
        }

        // only update provided details
        if (userModel.getEmail() != null)
            user.setEmail(userModel.getEmail());

        if (userModel.getHandle() != null)
            user.setHandle(userModel.getHandle());

        if (userModel.getFirstName() != null)
            user.setFirstName(userModel.getFirstName());

        if (userModel.getLastName() != null)
            user.setLastName(userModel.getLastName());

        if (userModel.getType() != null)
            user.setType(userModel.getType());

        return userRepo.save(user);
    }

    public void changePassword(UserModel userModel)
            throws UserNotFoundException, EntityOwnerMismatchException {

        // validate if user present with given userId
        Optional<User> userOpt = userRepo.findById(userModel.getUserId());
        if (!userOpt.isPresent()) {
            throw new UserNotFoundException(userModel.getUserId());
        }

        User user = userOpt.get();

        // validate whether the entity owner is same as the current authenticated user
        if (!user.matchOwnerWithAutenticatedUser()) {
            throw new EntityOwnerMismatchException();
        }

        Credential credential = user.getCredential();

        // validate the existing password with the provided password
        String oldPassword = userModel.getPassword();
        Boolean match = passwordEncoder.matches(oldPassword, credential.getPassword());
        if (!match) {
            throw new BadCredentialsException("username/password do not match");
        }

        // set the new password
        credential.setPassword(passwordEncoder.encode(userModel.getNewPassword())); // must encode with
                                                                                    // passwordEncoder
        userRepo.save(user);
    }

    // ~ Implementation of the methods of the UserDetails interface
    // --------------------------------------------------------------------------------------------------//
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // given username can be email or handle
        User user;
        try {
            user = findByEmailOrHandle(username, username);
        } catch (Exception e) {
            e.printStackTrace();
            throw new UsernameNotFoundException(e.getMessage());
        }
        return user;
    }
}
