package com.priyajit.microblogapp.exception;

public class PostNotFoundException extends Exception {

    public PostNotFoundException(Long postId) {
        super("No post found with userId=" + postId);
    }
}
