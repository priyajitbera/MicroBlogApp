package com.priyajit.microblogapp.exception;

public class UserNotFoundException extends Exception {

    public UserNotFoundException(Long userId) {
        super("No user found with userId=" + userId);
    }

    public UserNotFoundException(String emailOrHandle) {
        super("No user found with email or handle=" + emailOrHandle);
    }
}
